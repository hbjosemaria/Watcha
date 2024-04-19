package com.simplepeople.watcha.domain.repo

import com.simplepeople.watcha.data.local.RoomMovieServiceImpl
import com.simplepeople.watcha.data.model.MovieListResponse
import com.simplepeople.watcha.data.model.MovieResponse
import com.simplepeople.watcha.data.tmdb.TmdbApiServiceImpl
import com.simplepeople.watcha.domain.core.Movie

//Model interface guide for function implementation
interface ExternalMovieRepository {

    suspend fun getMovies(): MovieListResponse
    suspend fun getMovieById(movieId: Int): MovieResponse
    suspend fun getMoviesByTitle(searchText: String): MovieListResponse
    suspend fun getMoviesByPage(page: Int): MovieListResponse

}

//Model interface function for function implementation
interface LocalMovieRepository {
    suspend fun getFavoriteMovies(): Set<Movie>
}

//Implementation of repo
class ExternalMovieRepositoryImpl(private val apiService: TmdbApiServiceImpl) :
    ExternalMovieRepository {

    override suspend fun getMovies(): MovieListResponse =
        apiService.getMovies()

    override suspend fun getMovieById(movieId: Int): MovieResponse =
        apiService.getMovieById(movieId)

    override suspend fun getMoviesByTitle(searchText: String): MovieListResponse =
        apiService.getMoviesByTitle(searchText)

    override suspend fun getMoviesByPage(page: Int): MovieListResponse =
        apiService.getMoviesByPage(page)

}

//Implementation of repo
class LocalMovieRepositoryImpl(private val apiService: RoomMovieServiceImpl) :
    LocalMovieRepository {
    override suspend fun getFavoriteMovies(): Set<Movie> = apiService.getFavoriteMovies()
}