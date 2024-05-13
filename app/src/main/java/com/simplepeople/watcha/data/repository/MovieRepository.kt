package com.simplepeople.watcha.data.repository

import androidx.paging.PagingSource
import com.simplepeople.watcha.data.model.MovieListResponseModel
import com.simplepeople.watcha.data.model.MovieResponseModel
import com.simplepeople.watcha.data.services.MovieDao
import com.simplepeople.watcha.data.services.TmdbApiService
import javax.inject.Inject

//Interfaces which returns a Long or Int on insert and delete queries usually means if the query has success or not.
//  -1L means fail, while a positive Long/Int means success

//Model interface guide for function implementation
interface ExternalMovieRepository {

    suspend fun getMovieById(movieId: Long): MovieResponseModel
    suspend fun getMoviesByTitle(searchText: String, page: Int): MovieListResponseModel
    suspend fun getNowPlayingByPage(page: Int): MovieListResponseModel
    suspend fun getPopularByPage(page: Int): MovieListResponseModel
    suspend fun getTopRatedByPage(page: Int): MovieListResponseModel
    suspend fun getUpcomingByPage(page: Int): MovieListResponseModel

}

//Model interface function for function implementation
interface LocalMovieRepository {

    //Added Paging loading with Room for favorite movies. PagingSource is responsible for fetching new data when needed
    fun getFavoriteMovies(): PagingSource<Int, com.simplepeople.watcha.data.model.MovieModel>
    suspend fun getFavoriteById(movieId: Long): com.simplepeople.watcha.data.model.MovieModel
    suspend fun saveFavoriteMovie(movie: com.simplepeople.watcha.data.model.MovieModel): Long
    suspend fun deleteFavoriteMovie(movieId: Long): Int
    suspend fun checkIfMovieIsFavorite(movieId: Long): Int
}

//Model interface function for function implementation
interface MixedMovieRepository {
    suspend fun getMovieById(movieId: Long): Pair<MovieResponseModel, Int>
}

//Implementation of repo
class ExternalMovieRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService
) : ExternalMovieRepository {

    override suspend fun getMovieById(movieId: Long): MovieResponseModel =
        apiService.getMovieById(movieId)

    override suspend fun getMoviesByTitle(searchText: String, page: Int): MovieListResponseModel =
        apiService.getMoviesByTitle(searchText, page)

    override suspend fun getNowPlayingByPage(page: Int): MovieListResponseModel =
        apiService.getNowPlayingByPage(page)

    override suspend fun getPopularByPage(page: Int): MovieListResponseModel =
        apiService.getPopularByPage(page)

    override suspend fun getTopRatedByPage(page: Int): MovieListResponseModel =
        apiService.getTopRatedByPage(page)

    override suspend fun getUpcomingByPage(page: Int): MovieListResponseModel =
        apiService.getUpcomingByPage(page)


}

//Implementation of repo
class LocalMovieRepositoryImpl @Inject constructor(
    private val apiService: MovieDao
) : LocalMovieRepository {

    override fun getFavoriteMovies(): PagingSource<Int, com.simplepeople.watcha.data.model.MovieModel> =
        apiService.getFavoriteMovies()

    override suspend fun getFavoriteById(movieId: Long): com.simplepeople.watcha.data.model.MovieModel =
        apiService.getFavoriteById(movieId)

    override suspend fun saveFavoriteMovie(movie: com.simplepeople.watcha.data.model.MovieModel): Long =
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
    override suspend fun getMovieById(movieId: Long): Pair<MovieResponseModel, Int> {
        return Pair(
            apiService.getMovieById(movieId),
            roomService.checkIfMovieIsFavorite(movieId)
        )
    }

}