package com.simplepeople.watcha.data.repository

import androidx.paging.PagingSource
import com.simplepeople.watcha.data.model.local.SearchLogItemModel
import com.simplepeople.watcha.data.services.SearchLogDao
import javax.inject.Inject

interface SearchRepository {
    fun getRecentSearch(): PagingSource<Int, SearchLogItemModel>
    fun addNewSearch(searchLogItemModel: SearchLogItemModel): Long
    fun removeSearch(searchLogItemModel: SearchLogItemModel): Int
    fun cleanSearchLog(): Int
}

class SearchRepositoryImpl @Inject constructor(
    private val apiService: SearchLogDao
) : SearchRepository {
    override fun getRecentSearch(): PagingSource<Int, SearchLogItemModel> =
        apiService.getRecentSearch()

    override fun addNewSearch(searchLogItemModel: SearchLogItemModel): Long =
        apiService.addNewSearch(searchLogItemModel)

    override fun removeSearch(searchLogItemModel: SearchLogItemModel): Int =
        apiService.removeSearch(searchLogItemModel)

    override fun cleanSearchLog(): Int =
        apiService.cleanSearchLog()

}