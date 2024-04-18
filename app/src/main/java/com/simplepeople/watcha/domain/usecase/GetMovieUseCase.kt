package com.simplepeople.watcha.domain.usecase

import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.repo.ExternalMovieRepository
import javax.inject.Inject

class GetMovieUseCase @Inject constructor(
    private val repository: ExternalMovieRepository
) {
    suspend fun getMovieById(movieId: Int): Movie {
        return repository.getMovieById(movieId).toDomain()
    }

    suspend fun getMovieByTitle(searchText: String): Set<Movie> {
        return repository.getMoviesByTitle(searchText).toDomain()
    }
}