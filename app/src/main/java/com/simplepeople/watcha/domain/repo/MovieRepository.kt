package com.simplepeople.watcha.domain.repo

import com.simplepeople.watcha.domain.core.Movie

interface ExternalMovieRepository {

    suspend fun getMovies() : Set<Movie>
    suspend fun getMovieById(movieId: Int) : Set<Movie>
    suspend fun getMoviesByTitle(searchText: String) : Set<Movie>

}

interface LocalMovieRepository {
    suspend fun getFavoriteMovies() : Set<Movie>
}