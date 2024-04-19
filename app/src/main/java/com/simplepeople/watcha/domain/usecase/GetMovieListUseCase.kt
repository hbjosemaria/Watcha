package com.simplepeople.watcha.domain.usecase

import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.repo.ExternalMovieRepository
import javax.inject.Inject

class GetMovieListUseCase @Inject constructor(
    private val repository: ExternalMovieRepository
) {
    suspend fun getFirstPage(): Set<Movie> {
        return repository.getMovies().toDomain()
    }

    suspend fun getNextPage(page: Int): Set<Movie> {
        return repository.getMoviesByPage(page).toDomain()
    }
}