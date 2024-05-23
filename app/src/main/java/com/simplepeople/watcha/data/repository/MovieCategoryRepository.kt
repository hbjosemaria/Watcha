package com.simplepeople.watcha.data.repository

import com.simplepeople.watcha.data.model.local.MovieCategory
import com.simplepeople.watcha.data.services.MovieCategoryDao
import javax.inject.Inject

interface MovieCategoryRepository {
    suspend fun insertAll(keys: List<MovieCategory>)
    suspend fun clearAll()
}

class MovieCategoryRepositoryImpl @Inject constructor(
    private val apiService: MovieCategoryDao
) : MovieCategoryRepository {
    override suspend fun insertAll(keys: List<MovieCategory>) =
        apiService.insertAll(keys)

    override suspend fun clearAll() =
        apiService.clearAll()
}