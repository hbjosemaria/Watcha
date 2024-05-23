package com.simplepeople.watcha.data.model.local

import androidx.room.Entity

@Entity(
    tableName = "movie_category",
    primaryKeys = ["movieId", "categoryId"]
)
data class MovieCategory(
    val movieId: Long,
    val categoryId: Int,
    val position: Int
)