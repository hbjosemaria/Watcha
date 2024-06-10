package com.simplepeople.watcha.data.repository

import com.simplepeople.watcha.data.model.local.SearchLogItemEntity
import com.simplepeople.watcha.data.services.SearchLogDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SearchRepository {
    fun getRecentSearch(): Flow<List<SearchLogItemEntity>>
    fun addNewSearch(searchLogItemEntity: SearchLogItemEntity): Long
    fun removeSearch(searchLogItemEntity: SearchLogItemEntity): Int
    fun cleanSearchLog(): Int
}

class SearchRepositoryImpl @Inject constructor(
    private val apiService: SearchLogDao,
) : SearchRepository {
    override fun getRecentSearch(): Flow<List<SearchLogItemEntity>> =
        apiService.getRecentSearch()

    override fun addNewSearch(searchLogItemEntity: SearchLogItemEntity): Long =
        apiService.addNewSearch(searchLogItemEntity)

    override fun removeSearch(searchLogItemEntity: SearchLogItemEntity): Int =
        apiService.removeSearch(searchLogItemEntity)

    override fun cleanSearchLog(): Int =
        apiService.cleanSearchLog()

}