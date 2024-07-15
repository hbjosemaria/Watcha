package com.simplepeople.watcha.data.worker

import android.content.Context
import androidx.compose.ui.text.intl.Locale
import androidx.hilt.work.HiltWorker
import androidx.room.withTransaction
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.simplepeople.watcha.data.model.local.MovieCategoryEntity
import com.simplepeople.watcha.data.model.local.MovieEntity
import com.simplepeople.watcha.data.model.local.RemoteKeysEntity
import com.simplepeople.watcha.data.pagingsource.TMDB_API_MAX_PAGES
import com.simplepeople.watcha.data.repository.CacheRepository
import com.simplepeople.watcha.data.repository.LocalMovieRepository
import com.simplepeople.watcha.data.repository.MovieCategoryRepository
import com.simplepeople.watcha.data.repository.RemoteKeysRepository
import com.simplepeople.watcha.data.service.Room.WatchaDatabase
import com.simplepeople.watcha.data.service.TmdbMovieService
import com.simplepeople.watcha.ui.common.composables.topbar.HomeFilterOptions
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.TimeZone

@HiltWorker
class HomeMovieCachingWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val apiService: TmdbMovieService,
    private val database: WatchaDatabase,
    private val localMovieRepository: LocalMovieRepository,
    private val remoteKeysRepository: RemoteKeysRepository,
    private val movieCategoryRepository: MovieCategoryRepository,
    private val cacheRepository: CacheRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val pages = 1..11
        try {
            val nowPlayingMovieList = mutableListOf<MovieEntity>()
            val popularMovieList = mutableListOf<MovieEntity>()
            val topRatedMovieList = mutableListOf<MovieEntity>()
            val upcomingMovieList = mutableListOf<MovieEntity>()
            val remoteKeys = mutableListOf<RemoteKeysEntity>()
            val movieCategories = mutableListOf<MovieCategoryEntity>()

            pages.flatMap { page ->
                listOf(
                    async {
                        val nowPlayingList =
                            apiService.getNowPlayingByPage(page, Locale.current.language).toEntity()
                        nowPlayingMovieList.addAll(nowPlayingList)
                        remoteKeys.addAll(nowPlayingList.map { movieEntity ->
                            RemoteKeysEntity(
                                movieId = movieEntity.movieId,
                                categoryId = HomeFilterOptions.NowPlaying.categoryId,
                                prevKey = if (page == 1) null else page - 1,
                                nextKey = if (page == TMDB_API_MAX_PAGES) null else page + 1
                            )
                        })
                        movieCategories.addAll(nowPlayingList.mapIndexed { index, movieEntity ->
                            MovieCategoryEntity(
                                movieId = movieEntity.movieId,
                                categoryId = HomeFilterOptions.NowPlaying.categoryId,
                                position = index + ((page - 1) * 20) + 1
                            )
                        })
                    },

                    async {
                        val popularList =
                            apiService.getPopularByPage(page, Locale.current.language).toEntity()
                        popularMovieList.addAll(popularList)
                        remoteKeys.addAll(popularList.map { movieEntity ->
                            RemoteKeysEntity(
                                movieId = movieEntity.movieId,
                                categoryId = HomeFilterOptions.Popular.categoryId,
                                prevKey = if (page == 1) null else page - 1,
                                nextKey = if (page == TMDB_API_MAX_PAGES) null else page + 1
                            )
                        })
                        movieCategories.addAll(popularList.mapIndexed { index, movieEntity ->
                            MovieCategoryEntity(
                                movieId = movieEntity.movieId,
                                categoryId = HomeFilterOptions.Popular.categoryId,
                                position = index + ((page - 1) * 20) + 1
                            )
                        })
                    },

                    async {
                        val topRatedList =
                            apiService.getTopRatedByPage(page, Locale.current.language).toEntity()
                        topRatedMovieList.addAll(topRatedList)
                        remoteKeys.addAll(topRatedList.map { movieEntity ->
                            RemoteKeysEntity(
                                movieId = movieEntity.movieId,
                                categoryId = HomeFilterOptions.TopRated.categoryId,
                                prevKey = if (page == 1) null else page - 1,
                                nextKey = if (page == TMDB_API_MAX_PAGES) null else page + 1
                            )
                        })
                        movieCategories.addAll(topRatedList.mapIndexed { index, movieEntity ->
                            MovieCategoryEntity(
                                movieId = movieEntity.movieId,
                                categoryId = HomeFilterOptions.TopRated.categoryId,
                                position = index + ((page - 1) * 20) + 1
                            )
                        })
                    },

                    async {
                        val upcomingList =
                            apiService.getUpcomingByPage(page, Locale.current.language).toEntity()
                        upcomingMovieList.addAll(upcomingList)
                        remoteKeys.addAll(upcomingList.map { movieEntity ->
                            RemoteKeysEntity(
                                movieId = movieEntity.movieId,
                                categoryId = HomeFilterOptions.Upcoming.categoryId,
                                prevKey = if (page == 1) null else page - 1,
                                nextKey = if (page == TMDB_API_MAX_PAGES) null else page + 1
                            )
                        })
                        movieCategories.addAll(upcomingList.mapIndexed { index, movieEntity ->
                            MovieCategoryEntity(
                                movieId = movieEntity.movieId,
                                categoryId = HomeFilterOptions.Upcoming.categoryId,
                                position = index + ((page - 1) * 20) + 1
                            )
                        })
                    }
                )
            }.awaitAll()

            val movies = nowPlayingMovieList.asSequence()
                .plus(popularMovieList)
                .plus(topRatedMovieList)
                .plus(upcomingMovieList)
                .toSet()
                .toList()

            val newCacheExpiration = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            newCacheExpiration.add(Calendar.DAY_OF_YEAR, 1)
            newCacheExpiration.set(Calendar.HOUR_OF_DAY, 0)
            newCacheExpiration.set(Calendar.MINUTE, 0)
            newCacheExpiration.set(Calendar.SECOND, 0)
            newCacheExpiration.set(Calendar.MILLISECOND, 0)

            database.withTransaction {
                remoteKeysRepository.insertAll(remoteKeys)
                movieCategoryRepository.insertAll(movieCategories)
                localMovieRepository.insertAllMovies(movies)
                cacheRepository.clearMovieCacheExpiration()
                cacheRepository.saveMovieCacheExpiration(
                    HomeFilterOptions.NowPlaying.categoryId,
                    newCacheExpiration.timeInMillis
                )
                cacheRepository.saveMovieCacheExpiration(
                    HomeFilterOptions.Popular.categoryId,
                    newCacheExpiration.timeInMillis
                )
                cacheRepository.saveMovieCacheExpiration(
                    HomeFilterOptions.TopRated.categoryId,
                    newCacheExpiration.timeInMillis
                )
                cacheRepository.saveMovieCacheExpiration(
                    HomeFilterOptions.Upcoming.categoryId,
                    newCacheExpiration.timeInMillis
                )
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}