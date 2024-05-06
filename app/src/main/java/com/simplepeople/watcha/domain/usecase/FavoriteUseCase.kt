package com.simplepeople.watcha.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.simplepeople.watcha.data.repository.LocalMovieRepository
import com.simplepeople.watcha.domain.core.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteUseCase @Inject constructor (
    private val repository: LocalMovieRepository
) {

    fun getFavorites() : Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(
                pageSize = 40,
                maxSize = 200
            ),
            pagingSourceFactory = {repository.getFavoriteMovies()}
        ).flow
            .map {
                it.map {
                    it.toDomain()
                }
            }

    suspend fun saveFavorite(movie: Movie) : Boolean =
        repository.saveFavoriteMovie(movie.toDao()) > 0L

    suspend fun deleteFavorite(movieId: Long) : Boolean =
        repository.deleteFavoriteMovie(movieId) > 0L
}