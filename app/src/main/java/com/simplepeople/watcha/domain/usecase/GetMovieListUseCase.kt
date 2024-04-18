package com.simplepeople.watcha.domain.usecase

import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.repo.ExternalMovieRepository
import javax.inject.Inject

class GetMovieListUseCase @Inject constructor(
    private val repository: ExternalMovieRepository
) {
    suspend fun getMovieListMapped(): Set<Movie> {
        return repository.getMovies().toDomain()
    }
}