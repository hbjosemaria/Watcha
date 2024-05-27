package com.simplepeople.watcha.data.model.local

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "remote_keys",
    primaryKeys = ["movieId", "categoryId"],
    indices = [Index(value = ["categoryId"])]
)
data class RemoteKeysEntity(
    val movieId: Long,
    val categoryId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)