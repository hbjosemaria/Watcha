package com.simplepeople.watcha.domain.usecase

import androidx.compose.ui.text.intl.Locale
import com.simplepeople.watcha.data.repository.MixedMovieRepository
import com.simplepeople.watcha.domain.core.Movie
import javax.inject.Inject

class MovieUseCase @Inject constructor(
    private val mixedRepository: MixedMovieRepository,
) {
    suspend fun getMovieById(movieId: Long): Movie {
        val mixedResponse = mixedRepository.getMovieById(movieId, Locale.current.language)
        return mixedResponse.first.toDomain().copy(isFavorite = mixedResponse.second > 0)
    }
}