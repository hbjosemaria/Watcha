package com.simplepeople.watcha.domain.repo

import com.simplepeople.watcha.data.local.RoomMovieServiceImpl
import com.simplepeople.watcha.data.tmdb.TmdbApiServiceImpl
import com.simplepeople.watcha.domain.core.Movie

//Model interface guide
interface ExternalMovieRepository {

    suspend fun getMovies() : Set<Movie>
    suspend fun getMovieById(movieId: Int) : Movie
    suspend fun getMoviesByTitle(searchText: String) : Set<Movie>

}

//Implementation
class ExternalMovieRepositoryImpl (private val apiService: TmdbApiServiceImpl) : ExternalMovieRepository {

    override suspend fun getMovies(): Set<Movie> = apiService.getMovies()
    override suspend fun getMovieById(movieId: Int): Movie = apiService.getMovieById(movieId)
    override suspend fun getMoviesByTitle(searchText: String): Set<Movie> = apiService.getMoviesByTitle(searchText)

}

//Model interface guide
interface LocalMovieRepository {
    suspend fun getFavoriteMovies() : Set<Movie>
}

//Implementation
class LocalMovieRepositoryImpl (private val apiService: RoomMovieServiceImpl) : LocalMovieRepository {
    override suspend fun getFavoriteMovies(): Set<Movie> = apiService.getFavoriteMovies()
}