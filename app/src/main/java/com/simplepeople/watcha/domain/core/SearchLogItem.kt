package com.simplepeople.watcha.domain.core

import com.simplepeople.watcha.data.model.local.SearchLogItemModel

data class SearchLogItem(
    val id: Int = 0,
    val searchedText: String = ""
) {
    fun toDao(): SearchLogItemModel =
        SearchLogItemModel(
            id = this.id,
            searchedText = this.searchedText
        )
}