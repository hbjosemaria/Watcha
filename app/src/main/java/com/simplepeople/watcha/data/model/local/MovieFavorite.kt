package com.simplepeople.watcha.data.model.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_favorite")
data class MovieFavorite(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @Embedded
    val movie: MovieModel
)