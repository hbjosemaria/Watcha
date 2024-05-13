package com.simplepeople.watcha.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.simplepeople.watcha.data.model.SearchLogItemModel
import com.simplepeople.watcha.data.repository.SearchRepository
import com.simplepeople.watcha.domain.core.SearchLogItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchLogUseCase @Inject constructor (
    private val repository: SearchRepository
) {
    fun getRecentSearch() : Flow<PagingData<SearchLogItem>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 200
            ),
            pagingSourceFactory = {repository.getRecentSearch()}
        ).flow
            .map {
                it.map {
                    it.toDomain()
                }
            }

    fun addNewSearch(searchedText : String) {
        repository.addNewSearch(SearchLogItemModel(searchedText = searchedText))
    }

    fun removeSearch(searchLogItem : SearchLogItem) {
        repository.removeSearch(searchLogItem.toDao())
    }

    fun cleanSearchLog() {
        repository.cleanSearchLog()
    }
}