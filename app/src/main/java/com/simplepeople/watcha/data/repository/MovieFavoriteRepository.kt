package com.simplepeople.watcha.data.repository

import androidx.paging.PagingSource
import com.simplepeople.watcha.data.model.local.MovieFavoriteEntity
import com.simplepeople.watcha.data.services.MovieFavoriteDao
import javax.inject.Inject


interface MovieFavoriteRepository {
    fun getFavorites(): PagingSource<Int, MovieFavoriteEntity>
    suspend fun insertFavorite(favorite: MovieFavoriteEntity)
    suspend fun removeFavorite(movieId: Long)
    suspend fun checkIfMovieIsFavorite(movieId: Long): Int
}

class MovieFavoriteRepositoryImpl @Inject constructor(
    private val apiService: MovieFavoriteDao
) : MovieFavoriteRepository {
    override fun getFavorites(): PagingSource<Int, MovieFavoriteEntity> =
        apiService.getFavorites()

    override suspend fun insertFavorite(favorite: MovieFavoriteEntity) =
        apiService.insertFavorite(favorite = favorite)

    override suspend fun removeFavorite(movieId: Long) =
        apiService.removeFavorite(movieId = movieId)

    override suspend fun checkIfMovieIsFavorite(movieId: Long): Int =
        apiService.checkIfMovieIsFavorite(movieId)

}