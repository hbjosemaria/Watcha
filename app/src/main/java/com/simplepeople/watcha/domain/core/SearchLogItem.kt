package com.simplepeople.watcha.domain.core

import com.simplepeople.watcha.data.model.local.SearchLogItemEntity

data class SearchLogItem(
    val id: Int = 0,
    val searchedText: String = "",
) {
    fun toDao(): SearchLogItemEntity =
        SearchLogItemEntity(
            id = this.id,
            searchedText = this.searchedText
        )
}