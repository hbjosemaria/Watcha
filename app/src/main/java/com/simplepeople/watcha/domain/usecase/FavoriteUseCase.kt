package com.simplepeople.watcha.domain.usecase

import com.simplepeople.watcha.data.repository.LocalMovieRepository
import com.simplepeople.watcha.domain.core.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteUseCase @Inject constructor (
    private val localRepository: LocalMovieRepository
) {
    suspend fun getFavorites() : Flow<List<Movie>> =
        localRepository.getFavoriteMovies()

    suspend fun saveFavorite(movie: Movie) : Boolean =
        localRepository.saveFavoriteMovie(movie) > 0L

    suspend fun deleteFavorite(movie: Movie) : Boolean =
        localRepository.deleteFavoriteMovie(movie) > 0L
}