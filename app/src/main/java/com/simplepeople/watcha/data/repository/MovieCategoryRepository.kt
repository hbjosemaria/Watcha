package com.simplepeople.watcha.data.repository

import com.simplepeople.watcha.data.model.local.MovieCategoryEntity
import com.simplepeople.watcha.data.services.MovieCategoryDao
import javax.inject.Inject

interface MovieCategoryRepository {
    suspend fun insertAll(keys: List<MovieCategoryEntity>)
    suspend fun clearAll()
}

class MovieCategoryRepositoryImpl @Inject constructor(
    private val apiService: MovieCategoryDao
) : MovieCategoryRepository {
    override suspend fun insertAll(keys: List<MovieCategoryEntity>) =
        apiService.insertAll(keys)

    override suspend fun clearAll() =
        apiService.clearAll()
}