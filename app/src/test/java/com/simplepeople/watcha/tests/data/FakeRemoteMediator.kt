package com.simplepeople.watcha.tests.data

import androidx.compose.ui.text.intl.Locale
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import coil.network.HttpException
import com.simplepeople.watcha.data.model.local.MovieCategoryEntity
import com.simplepeople.watcha.data.model.local.MovieEntity
import com.simplepeople.watcha.data.model.local.RemoteKeysEntity
import com.simplepeople.watcha.data.remotemediator.MovieRemoteMediator
import com.simplepeople.watcha.data.repository.CacheRepository
import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import com.simplepeople.watcha.data.repository.LocalMovieRepository
import com.simplepeople.watcha.data.repository.MovieCategoryRepository
import com.simplepeople.watcha.data.repository.RemoteKeysRepository
import com.simplepeople.watcha.ui.common.composables.topbar.HomeFilterOptions
import java.io.IOException
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalPagingApi::class)
class FakeMovieRemoteMediator(
    private val apiService: ExternalMovieRepository,
    private val localMovieRepository: LocalMovieRepository,
    private val remoteKeysRepository: RemoteKeysRepository,
    private val movieCategoryRepository: MovieCategoryRepository,
    private val cacheRepository: CacheRepository,
    private val filterOption: HomeFilterOptions,
    private val lastTimeoutCache : Long?,
    private val successfulDatabaseTransaction: Boolean = true
) : RemoteMediator<Int, MovieEntity>() {

    interface FakeMovieRemoteMediatorFactory : MovieRemoteMediator.MovieRemoteMediatorFactory {
        override fun create(filterOption: HomeFilterOptions): MovieRemoteMediator
    }

    override suspend fun initialize(): InitializeAction {
        val currentTime = System.currentTimeMillis()
        val cacheTimeout = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cacheTimeout.add(Calendar.DAY_OF_YEAR, 1)
        cacheTimeout.set(Calendar.HOUR_OF_DAY, 0)
        cacheTimeout.set(Calendar.MINUTE, 0)
        cacheTimeout.set(Calendar.SECOND, 0)
        cacheTimeout.set(Calendar.MILLISECOND, 0)

        return when {
            lastTimeoutCache == null -> {
                cacheRepository.saveMovieCacheExpiration(
                    categoryId = filterOption.categoryId,
                    cacheExpiration = cacheTimeout.timeInMillis
                )
                InitializeAction.LAUNCH_INITIAL_REFRESH
            }

            currentTime > lastTimeoutCache -> {
                if (successfulDatabaseTransaction) {
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
            val locale = Locale.current.language
            val movies = when (filterOption) {
                HomeFilterOptions.NowPlaying -> apiService.getNowPlayingByPage(1, locale)
                HomeFilterOptions.Popular -> apiService.getPopularByPage(1, locale)
                HomeFilterOptions.TopRated -> apiService.getTopRatedByPage(1, locale)
                HomeFilterOptions.Upcoming -> apiService.getUpcomingByPage(1, locale)
            }.toEntity()

            val remoteKeys = movies.map { movieModel ->
                RemoteKeysEntity(
                    movieId = movieModel.movieId,
                    categoryId = filterOption.categoryId,
                    prevKey = null,
                    nextKey = 2
                )
            }

            val movieCategories = movies.mapIndexed { index, movieModel ->
                MovieCategoryEntity(
                    movieId = movieModel.movieId,
                    categoryId = filterOption.categoryId,
                    position = index + state.config.pageSize + 1
                )
            }

            if (successfulDatabaseTransaction) {
                remoteKeysRepository.insertAll(remoteKeys)
                movieCategoryRepository.insertAll(movieCategories)
                localMovieRepository.insertAllMovies(movies)
            }

            MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}