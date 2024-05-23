package com.simplepeople.watcha.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.simplepeople.watcha.domain.core.SearchLogItem

@Entity(tableName = "search")
data class SearchLogItemModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val searchedText: String = ""
) {
    fun toDomain(): SearchLogItem =
        SearchLogItem(
            id = this.id,
            searchedText = this.searchedText
        )
}