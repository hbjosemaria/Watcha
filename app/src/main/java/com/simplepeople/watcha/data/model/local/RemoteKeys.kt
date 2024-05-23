package com.simplepeople.watcha.data.model.local

import androidx.room.Entity

@Entity(
    tableName = "remote_keys",
    primaryKeys = ["movieId", "categoryId"]
)
data class RemoteKeys(
    val movieId: Long,
    val categoryId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)