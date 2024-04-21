package com.simplepeople.watcha.domain.usecase

import com.simplepeople.watcha.data.repository.MixedMovieRepository
import com.simplepeople.watcha.domain.core.Movie
import javax.inject.Inject

class MovieUseCase @Inject constructor(
    private val mixedRepository: MixedMovieRepository
) {

    suspend fun getMovieById(movieId: Int): Movie {
        val mixedResponse = mixedRepository.getMovieById(movieId)
        return mixedResponse.first.toDomain().copy(isFavorite = mixedResponse.second > 0L)
    }
}