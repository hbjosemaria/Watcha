package com.simplepeople.watcha.data.repository

import com.simplepeople.watcha.data.model.MovieListResponse
import com.simplepeople.watcha.data.model.MovieResponse
import com.simplepeople.watcha.data.services.MovieDao
import com.simplepeople.watcha.data.services.TmdbApiService
import com.simplepeople.watcha.domain.core.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//Interfaces which returns a Long or Int on insert and delete queries usually means if the query has success or not.
//  -1L means fail, while a positive Long/Int means success

//Model interface guide for function implementation
interface ExternalMovieRepository {

    suspend fun getMovies(): MovieListResponse
    suspend fun getMovieById(movieId: Int): MovieResponse
    suspend fun getMoviesByTitle(searchText: String): MovieListResponse
    suspend fun getMoviesByPage(page: Int): MovieListResponse

}

//Model interface function for function implementation
interface LocalMovieRepository {
    fun getFavoriteMovies(): Flow<List<Movie>>
    suspend fun getFavoriteById(movieId: Int): Movie
    suspend fun saveFavoriteMovie(movie: Movie): Long
    suspend fun deleteFavoriteMovie(movie: Movie): Int
    suspend fun checkIfMovieIsFavorite(movieId: Int): Long
}

//Model interface function for function implementation
interface MixedMovieRepository {
    suspend fun getMovieById(movieId: Int): Pair<MovieResponse, Long>
}

//Implementation of repo
class ExternalMovieRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService
) : ExternalMovieRepository {

    override suspend fun getMovies(): MovieListResponse = apiService.getMovies()

    override suspend fun getMovieById(movieId: Int): MovieResponse =
        apiService.getMovieById(movieId)

    override suspend fun getMoviesByTitle(searchText: String): MovieListResponse =
        apiService.getMoviesByTitle(searchText)

    override suspend fun getMoviesByPage(page: Int): MovieListResponse =
        apiService.getMoviesByPage(page)

}

//Implementation of repo
class LocalMovieRepositoryImpl @Inject constructor(
    private val apiService: MovieDao
) : LocalMovieRepository {

    override fun getFavoriteMovies(): Flow<List<Movie>> =
        apiService.getFavoriteMovies()

    override suspend fun getFavoriteById(movieId: Int): Movie =
        apiService.getFavoriteById(movieId)

    override suspend fun saveFavoriteMovie(movie: Movie): Long =
        apiService.saveFavoriteMovie(movie)

    override suspend fun deleteFavoriteMovie(movie: Movie): Int =
        apiService.deleteFavoriteMovie(movie)

    override suspend fun checkIfMovieIsFavorite(movieId: Int): Long =
        apiService.checkIfMovieIsFavorite(movieId)

}

//Implementation of repo
class MixedMovieRepositoryImpl @Inject constructor(
    private val roomService: MovieDao,
    private val apiService: TmdbApiService
) : MixedMovieRepository {
    override suspend fun getMovieById(movieId: Int): Pair<MovieResponse, Long> {
        return Pair(
            apiService.getMovieById(movieId),
            roomService.checkIfMovieIsFavorite(movieId)
        )
    }

}