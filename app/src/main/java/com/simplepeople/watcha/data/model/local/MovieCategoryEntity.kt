package com.simplepeople.watcha.data.model.local

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "movie_category",
    primaryKeys = ["movieId", "categoryId"],
    indices = [Index(value = ["categoryId"])]
)
data class MovieCategoryEntity(
    val movieId: Long,
    val categoryId: Int,
    val position: Int,
)