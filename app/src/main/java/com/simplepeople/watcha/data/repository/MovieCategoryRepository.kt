package com.simplepeople.watcha.data.repository

import com.simplepeople.watcha.data.model.local.MovieCategoryEntity
import com.simplepeople.watcha.data.services.MovieCategoryDao
import javax.inject.Inject

interface MovieCategoryRepository {
    suspend fun insertAll(categories: List<MovieCategoryEntity>)
    suspend fun clearAll()
}

class MovieCategoryRepositoryImpl @Inject constructor(
    private val apiService: MovieCategoryDao
) : MovieCategoryRepository {
    override suspend fun insertAll(categories: List<MovieCategoryEntity>) =
        apiService.insertAll(categories)

    override suspend fun clearAll() =
        apiService.clearAll()
}