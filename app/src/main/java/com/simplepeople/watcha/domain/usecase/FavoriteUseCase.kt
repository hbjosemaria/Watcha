package com.simplepeople.watcha.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.simplepeople.watcha.data.repository.MovieFavoriteRepository
import com.simplepeople.watcha.domain.core.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(
    private val repository: MovieFavoriteRepository,
) {

    fun getFavorites(): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { repository.getFavorites() }
        ).flow
            .map { pagingData ->
                pagingData.map { movieFavorite ->
                    movieFavorite.movie.toDomain()
                }
            }

    suspend fun saveFavorite(movie: Movie) =
        repository.insertFavorite(movie.toFavoriteDao())

    suspend fun deleteFavorite(movieId: Long) =
        repository.removeFavorite(movieId)
}