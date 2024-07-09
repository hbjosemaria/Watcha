package com.simplepeople.watcha.data.repository

import androidx.paging.PagingSource
import com.simplepeople.watcha.data.model.external.MovieListResponseDto
import com.simplepeople.watcha.data.model.external.MovieResponseDto
import com.simplepeople.watcha.data.model.local.MovieEntity
import com.simplepeople.watcha.data.services.MovieDao
import com.simplepeople.watcha.data.services.MovieFavoriteDao
import com.simplepeople.watcha.data.services.TmdbMovieService
import javax.inject.Inject

//Interfaces which returns a Long or Int on insert and delete queries usually means if the query has success or not.

//Model interface guide for function implementation
interface ExternalMovieRepository {
    suspend fun getMoviesByTitle(
        searchText: String,
        page: Int,
        language: String,
    ): MovieListResponseDto
    suspend fun getMovieById(movieId: Long, language: String): MovieResponseDto
    suspend fun getNowPlayingByPage(page: Int, language: String): MovieListResponseDto
    suspend fun getPopularByPage(page: Int, language: String): MovieListResponseDto
    suspend fun getTopRatedByPage(page: Int, language: String): MovieListResponseDto
    suspend fun getUpcomingByPage(page: Int, language: String): MovieListResponseDto
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
    suspend fun getMovieById(movieId: Long, language: String): Pair<MovieResponseDto, Int>
}

//Implementation of repo
class ExternalMovieRepositoryImpl @Inject constructor(
    private val apiService: TmdbMovieService,
) : ExternalMovieRepository {

    override suspend fun getMovieById(movieId: Long, language: String): MovieResponseDto =
        apiService.getMovieById(movieId, language)

    override suspend fun getMoviesByTitle(searchText: String, page: Int, language: String): MovieListResponseDto =
        apiService.getMoviesByTitle(searchText, page, language)

    override suspend fun getNowPlayingByPage(page: Int, language: String): MovieListResponseDto =
        apiService.getNowPlayingByPage(page, language)

    override suspend fun getPopularByPage(page: Int, language: String): MovieListResponseDto =
        apiService.getPopularByPage(page, language)

    override suspend fun getTopRatedByPage(page: Int, language: String): MovieListResponseDto =
        apiService.getTopRatedByPage(page, language)

    override suspend fun getUpcomingByPage(page: Int, language: String): MovieListResponseDto =
        apiService.getUpcomingByPage(page, language)


}

//Implementation of repo
class LocalMovieRepositoryImpl @Inject constructor(
    private val apiService: MovieDao,
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
    private val apiService: TmdbMovieService,
) : MixedMovieRepository {
    override suspend fun getMovieById(movieId: Long, language: String): Pair<MovieResponseDto, Int> =
        Pair(
            apiService.getMovieById(movieId, language),
            roomService.checkIfMovieIsFavorite(movieId)
        )
}