package com.simplepeople.watcha.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.simplepeople.watcha.data.repository.LocalMovieRepository
import com.simplepeople.watcha.domain.core.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteUseCase @Inject constructor (
    private val repository: LocalMovieRepository
) {

    fun getFavorites() : Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 30
            ),
            pagingSourceFactory = {repository.getFavoriteMovies()}
        ).flow

    suspend fun saveFavorite(movie: Movie) : Boolean =
        repository.saveFavoriteMovie(movie) > 0L

    suspend fun deleteFavorite(movie: Movie) : Boolean =
        repository.deleteFavoriteMovie(movie) > 0L
}