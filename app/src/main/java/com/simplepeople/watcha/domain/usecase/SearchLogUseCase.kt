package com.simplepeople.watcha.domain.usecase

import com.simplepeople.watcha.data.model.local.SearchLogItemEntity
import com.simplepeople.watcha.data.repository.SearchRepository
import com.simplepeople.watcha.domain.core.SearchLogItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchLogUseCase @Inject constructor(
    private val repository: SearchRepository,
) {
    fun getRecentSearch(): Flow<List<SearchLogItem>> =
        repository.getRecentSearch().map {
            it.map {
                it.toDomain()
            }
        }

    fun addNewSearch(searchedText: String) {
        repository.addNewSearch(SearchLogItemEntity(searchedText = searchedText))
    }

    fun removeSearch(searchLogItem: SearchLogItem) {
        repository.removeSearch(searchLogItem.toDao())
    }

    fun cleanSearchLog() {
        repository.cleanSearchLog()
    }
}