package com.simplepeople.watcha.data.repository

import androidx.paging.PagingSource
import com.simplepeople.watcha.data.model.MovieDAO
import com.simplepeople.watcha.data.model.MovieListResponse
import com.simplepeople.watcha.data.model.MovieResponse
import com.simplepeople.watcha.data.services.MovieDao
import com.simplepeople.watcha.data.services.TmdbApiService
import javax.inject.Inject

//Interfaces which returns a Long or Int on insert and delete queries usually means if the query has success or not.
//  -1L means fail, while a positive Long/Int means success

//Model interface guide for function implementation
interface ExternalMovieRepository {

    suspend fun getMovieById(movieId: Long): MovieResponse
    suspend fun getMoviesByTitle(searchText: String, page: Int): MovieListResponse
    suspend fun getNowPlayingByPage(page: Int): MovieListResponse
    suspend fun getPopularByPage(page: Int): MovieListResponse
    suspend fun getTopRatedByPage(page: Int): MovieListResponse
    suspend fun getUpcomingByPage(page: Int): MovieListResponse

}

//Model interface function for function implementation
interface LocalMovieRepository {

    //Added Paging loading with Room for favorite movies. PagingSource is responsible for fetching new data when needed
    fun getFavoriteMovies(): PagingSource<Int, MovieDAO>
    suspend fun getFavoriteById(movieId: Long): MovieDAO
    suspend fun saveFavoriteMovie(movie: MovieDAO): Long
    suspend fun deleteFavoriteMovie(movieId: Long): Int
    suspend fun checkIfMovieIsFavorite(movieId: Long): Int
}

//Model interface function for function implementation
interface MixedMovieRepository {
    suspend fun getMovieById(movieId: Long): Pair<MovieResponse, Int>
}

//Implementation of repo
class ExternalMovieRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService
) : ExternalMovieRepository {

    override suspend fun getMovieById(movieId: Long): MovieResponse =
        apiService.getMovieById(movieId)

    override suspend fun getMoviesByTitle(searchText: String, page: Int): MovieListResponse =
        apiService.getMoviesByTitle(searchText, page)

    override suspend fun getNowPlayingByPage(page: Int): MovieListResponse =
        apiService.getNowPlayingByPage(page)

    override suspend fun getPopularByPage(page: Int): MovieListResponse =
        apiService.getPopularByPage(page)

    override suspend fun getTopRatedByPage(page: Int): MovieListResponse =
        apiService.getTopRatedByPage(page)

    override suspend fun getUpcomingByPage(page: Int): MovieListResponse =
        apiService.getUpcomingByPage(page)


}

//Implementation of repo
class LocalMovieRepositoryImpl @Inject constructor(
    private val apiService: MovieDao
) : LocalMovieRepository {

    override fun getFavoriteMovies(): PagingSource<Int, MovieDAO> =
        apiService.getFavoriteMovies()

    override suspend fun getFavoriteById(movieId: Long): MovieDAO =
        apiService.getFavoriteById(movieId)

    override suspend fun saveFavoriteMovie(movie: MovieDAO): Long =
        apiService.saveFavoriteMovie(movie)

    override suspend fun deleteFavoriteMovie(movieId: Long): Int =
        apiService.deleteFavoriteMovie(movieId)

    override suspend fun checkIfMovieIsFavorite(movieId: Long): Int =
        apiService.checkIfMovieIsFavorite(movieId)

}

//Implementation of repo
class MixedMovieRepositoryImpl @Inject constructor(
    private val roomService: MovieDao,
    private val apiService: TmdbApiService
) : MixedMovieRepository {
    override suspend fun getMovieById(movieId: Long): Pair<MovieResponse, Int> {
        return Pair(
            apiService.getMovieById(movieId),
            roomService.checkIfMovieIsFavorite(movieId)
        )
    }

}