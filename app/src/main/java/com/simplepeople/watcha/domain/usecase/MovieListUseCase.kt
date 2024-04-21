package com.simplepeople.watcha.domain.usecase

import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import com.simplepeople.watcha.domain.core.Movie
import javax.inject.Inject

class MovieListUseCase @Inject constructor(
    private val repository: ExternalMovieRepository
) {
    suspend fun getFirstPage(): List<Movie> =
        repository.getMovies().toDomain()


    suspend fun getNextPage(page: Int): List<Movie> =
        repository.getMoviesByPage(page).toDomain()


    suspend fun getByTitle(searchText: String): List<Movie> =
        repository.getMoviesByTitle(searchText).toDomain()
}