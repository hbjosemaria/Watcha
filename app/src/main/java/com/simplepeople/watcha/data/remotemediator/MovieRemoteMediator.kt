package com.simplepeople.watcha.data.remotemediator

import androidx.compose.ui.text.intl.Locale
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import coil.network.HttpException
import com.simplepeople.watcha.data.model.local.MovieCategoryEntity
import com.simplepeople.watcha.data.model.local.MovieEntity
import com.simplepeople.watcha.data.model.local.RemoteKeysEntity
import com.simplepeople.watcha.data.pagingsource.TMDB_API_MAX_PAGES
import com.simplepeople.watcha.data.repository.CacheRepository
import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import com.simplepeople.watcha.data.repository.LocalMovieRepository
import com.simplepeople.watcha.data.repository.MovieCategoryRepository
import com.simplepeople.watcha.data.repository.RemoteKeysRepository
import com.simplepeople.watcha.data.services.WatchaDatabase
import com.simplepeople.watcha.ui.common.composables.topbar.HomeFilterOptions
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import okio.IOException
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator @AssistedInject constructor(
    private val apiService: ExternalMovieRepository,
    private val database: WatchaDatabase,
    private val localMovieRepository: LocalMovieRepository,
    private val remoteKeysRepository: RemoteKeysRepository,
    private val movieCategoryRepository: MovieCategoryRepository,
    private val cacheRepository: CacheRepository,
    @Assisted private val filterOption: HomeFilterOptions,
) : RemoteMediator<Int, MovieEntity>() {

    @AssistedFactory
    interface MovieRemoteMediatorFactory {
        fun create(filterOption: HomeFilterOptions): MovieRemoteMediator
    }

    override suspend fun initialize(): InitializeAction {

        val lastTimeoutCache = cacheRepository.loadMovieCacheExpiration(filterOption.categoryId)
        val cacheTimeout = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cacheTimeout.add(Calendar.DAY_OF_YEAR, 1)
        cacheTimeout.set(Calendar.HOUR_OF_DAY, 0)
        cacheTimeout.set(Calendar.MINUTE, 0)
        cacheTimeout.set(Calendar.SECOND, 0)
        cacheTimeout.set(Calendar.MILLISECOND, 0)
        val currentTime = System.currentTimeMillis()

        return when {
            lastTimeoutCache == null -> {
                cacheRepository.saveMovieCacheExpiration(
                    categoryId = filterOption.categoryId,
                    cacheExpiration = cacheTimeout.timeInMillis
                )
                InitializeAction.LAUNCH_INITIAL_REFRESH
            }

            currentTime > lastTimeoutCache -> {
                database.withTransaction {
                    cacheRepository.clearMovieCacheExpiration()
                    cacheRepository.saveMovieCacheExpiration(
                        categoryId = filterOption.categoryId,
                        cacheExpiration = cacheTimeout.timeInMillis
                    )
                    localMovieRepository.clearCachedMovies()
                    movieCategoryRepository.clearAll()
                    remoteKeysRepository.clearRemoteKeys()
                }
                InitializeAction.LAUNCH_INITIAL_REFRESH
            }

            currentTime <= lastTimeoutCache -> {
                InitializeAction.SKIP_INITIAL_REFRESH
            }

            else -> InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>,
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKey =
                        getRemoteKeyClosestToCurrentPosition(state, filterOption.categoryId)
                    remoteKey?.nextKey?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKey = getRemoteKeyForFirstItem(state, filterOption.categoryId)
                    remoteKey?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                }

                LoadType.APPEND -> {
                    val remoteKey = getRemoteKeyForLastItem(state, filterOption.categoryId)
                    remoteKey?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                }
            }

            val language = Locale.current.language

            val movies = when (filterOption) {
                HomeFilterOptions.NowPlaying -> apiService.getNowPlayingByPage(page, language)
                HomeFilterOptions.Popular -> apiService.getPopularByPage(page, language)
                HomeFilterOptions.TopRated -> apiService.getTopRatedByPage(page, language)
                HomeFilterOptions.Upcoming -> apiService.getUpcomingByPage(page, language)
            }.toEntity()

            val remoteKeys = movies.map { movieModel ->
                RemoteKeysEntity(
                    movieId = movieModel.movieId,
                    categoryId = filterOption.categoryId,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (page == TMDB_API_MAX_PAGES) null else page + 1
                )
            }

            val movieCategories = movies.mapIndexed { index, movieModel ->
                MovieCategoryEntity(
                    movieId = movieModel.movieId,
                    categoryId = filterOption.categoryId,
                    position = index + ((page - 1) * state.config.pageSize) + 1
                )
            }

            database.withTransaction {
                remoteKeysRepository.insertAll(remoteKeys)
                movieCategoryRepository.insertAll(movieCategories)
                localMovieRepository.insertAllMovies(movies)
            }

            MediatorResult.Success(endOfPaginationReached = page == TMDB_API_MAX_PAGES)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MovieEntity>,
        category: Int,
    ): RemoteKeysEntity? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data
            ?.lastOrNull()
            ?.let { movie ->
                remoteKeysRepository.getRemoteKey(
                    movieId = movie.movieId,
                    categoryId = category
                )
            }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, MovieEntity>,
        category: Int,
    ): RemoteKeysEntity? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data
            ?.firstOrNull()
            ?.let { movie ->
                remoteKeysRepository.getRemoteKey(
                    movieId = movie.movieId,
                    categoryId = category
                )
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieEntity>,
        category: Int,
    ): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.movieId?.let { movieId ->
                remoteKeysRepository.getRemoteKey(
                    movieId = movieId,
                    categoryId = category
                )
            }
        }
    }
}