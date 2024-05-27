package com.simplepeople.watcha.data.repository

import androidx.paging.PagingSource
import com.simplepeople.watcha.data.model.external.MovieListResponseDto
import com.simplepeople.watcha.data.model.external.MovieResponseDto
import com.simplepeople.watcha.data.model.local.MovieEntity
import com.simplepeople.watcha.data.services.MovieDao
import com.simplepeople.watcha.data.services.MovieFavoriteDao
import com.simplepeople.watcha.data.services.TmdbApiService
import javax.inject.Inject

//Interfaces which returns a Long or Int on insert and delete queries usually means if the query has success or not.

//Model interface guide for function implementation
interface ExternalMovieRepository {
    suspend fun getMovieById(movieId: Long): MovieResponseDto
    suspend fun getMoviesByTitle(searchText: String, page: Int): MovieListResponseDto
    suspend fun getNowPlayingByPage(page: Int): MovieListResponseDto
    suspend fun getPopularByPage(page: Int): MovieListResponseDto
    suspend fun getTopRatedByPage(page: Int): MovieListResponseDto
    suspend fun getUpcomingByPage(page: Int): MovieListResponseDto
}

//Model interface function for function implementation
interface LocalMovieRepository {
    //Added Paging loading with Room for favorite movies. PagingSource is responsible for fetching new data when needed
    fun getAllMovies(): PagingSource<Int, MovieEntity>
    fun getByCategory(category: Int): PagingSource<Int, MovieEntity>
    suspend fun getMovieById(movieId: Long): MovieEntity
    suspend fun addMovie(movie: MovieEntity): Long
    suspend fun deleteMovie(movieId: Long): Int
    suspend fun insertAllMovies(movieList: List<MovieEntity>)
    suspend fun clearCachedMovies(): Int
}

//Model interface function for function implementation
interface MixedMovieRepository {
    suspend fun getMovieById(movieId: Long): Pair<MovieResponseDto, Int>
}

//Implementation of repo
class ExternalMovieRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService
) : ExternalMovieRepository {

    override suspend fun getMovieById(movieId: Long): MovieResponseDto =
        apiService.getMovieById(movieId)

    override suspend fun getMoviesByTitle(searchText: String, page: Int): MovieListResponseDto =
        apiService.getMoviesByTitle(searchText, page)

    override suspend fun getNowPlayingByPage(page: Int): MovieListResponseDto =
        apiService.getNowPlayingByPage(page)

    override suspend fun getPopularByPage(page: Int): MovieListResponseDto =
        apiService.getPopularByPage(page)

    override suspend fun getTopRatedByPage(page: Int): MovieListResponseDto =
        apiService.getTopRatedByPage(page)

    override suspend fun getUpcomingByPage(page: Int): MovieListResponseDto =
        apiService.getUpcomingByPage(page)


}

//Implementation of repo
class LocalMovieRepositoryImpl @Inject constructor(
    private val apiService: MovieDao
) : LocalMovieRepository {

    override fun getAllMovies(): PagingSource<Int, MovieEntity> =
        apiService.getAllMovies()

    override fun getByCategory(category: Int): PagingSource<Int, MovieEntity> =
        apiService.getByCategory(category)

    override suspend fun getMovieById(movieId: Long): MovieEntity =
        apiService.getMovieById(movieId)

    override suspend fun addMovie(movie: MovieEntity): Long =
        apiService.addMovie(movie)

    override suspend fun deleteMovie(movieId: Long): Int =
        apiService.deleteMovie(movieId)

    override suspend fun insertAllMovies(movieList: List<MovieEntity>) =
        apiService.insertAllMovies(movieList)

    override suspend fun clearCachedMovies(): Int =
        apiService.clearCachedMovies()
}

//Implementation of repo
class MixedMovieRepositoryImpl @Inject constructor(
    private val roomService: MovieFavoriteDao,
    private val apiService: TmdbApiService
) : MixedMovieRepository {
    override suspend fun getMovieById(movieId: Long): Pair<MovieResponseDto, Int> {
        return Pair(
            apiService.getMovieById(movieId),
            roomService.checkIfMovieIsFavorite(movieId)
        )
    }
}