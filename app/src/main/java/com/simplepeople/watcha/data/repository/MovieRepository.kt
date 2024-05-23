package com.simplepeople.watcha.data.repository

import androidx.paging.PagingSource
import com.simplepeople.watcha.data.model.external.MovieListResponseModel
import com.simplepeople.watcha.data.model.external.MovieResponseModel
import com.simplepeople.watcha.data.model.local.MovieModel
import com.simplepeople.watcha.data.services.MovieDao
import com.simplepeople.watcha.data.services.MovieFavoriteDao
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
    fun getAllMovies(): PagingSource<Int, MovieModel>
    fun getByCategory(category: Int): PagingSource<Int, MovieModel>
    suspend fun getMovieById(movieId: Long): MovieModel
    suspend fun addMovie(movie: MovieModel): Long
    suspend fun deleteMovie(movieId: Long): Int
    suspend fun insertAllMovies(movieList: List<MovieModel>)
    suspend fun clearCachedMovies(): Int
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

    override fun getAllMovies(): PagingSource<Int, MovieModel> =
        apiService.getAllMovies()

    override fun getByCategory(category: Int): PagingSource<Int, MovieModel> =
        apiService.getByCategory(category)

    override suspend fun getMovieById(movieId: Long): MovieModel =
        apiService.getMovieById(movieId)

    override suspend fun addMovie(movie: MovieModel): Long =
        apiService.addMovie(movie)

    override suspend fun deleteMovie(movieId: Long): Int =
        apiService.deleteMovie(movieId)

    override suspend fun insertAllMovies(movieList: List<MovieModel>) =
        apiService.insertAllMovies(movieList)

    override suspend fun clearCachedMovies(): Int =
        apiService.clearCachedMovies()
}

//Implementation of repo
class MixedMovieRepositoryImpl @Inject constructor(
    private val roomService: MovieFavoriteDao,
    private val apiService: TmdbApiService
) : MixedMovieRepository {
    override suspend fun getMovieById(movieId: Long): Pair<MovieResponseModel, Int> {
        return Pair(
            apiService.getMovieById(movieId),
            roomService.checkIfMovieIsFavorite(movieId)
        )
    }
}