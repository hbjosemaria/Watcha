package com.simplepeople.watcha.data.repository

import androidx.paging.PagingSource
import com.simplepeople.watcha.data.model.local.MovieFavorite
import com.simplepeople.watcha.data.services.MovieFavoriteDao
import javax.inject.Inject


interface MovieFavoriteRepository {
    fun getFavorites(): PagingSource<Int, MovieFavorite>
    suspend fun insertFavorite(favorite: MovieFavorite)
    suspend fun removeFavorite(movieId: Long)
    suspend fun checkIfMovieIsFavorite(movieId: Long): Int
}

class MovieFavoriteRepositoryImpl @Inject constructor(
    private val apiService: MovieFavoriteDao
) : MovieFavoriteRepository {
    override fun getFavorites(): PagingSource<Int, MovieFavorite> =
        apiService.getFavorites()

    override suspend fun insertFavorite(favorite: MovieFavorite) =
        apiService.insertFavorite(favorite = favorite)

    override suspend fun removeFavorite(movieId: Long) =
        apiService.removeFavorite(movieId = movieId)

    override suspend fun checkIfMovieIsFavorite(movieId: Long): Int =
        apiService.checkIfMovieIsFavorite(movieId)

}