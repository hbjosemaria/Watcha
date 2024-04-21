package com.simplepeople.watcha.domain.core

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.simplepeople.watcha.data.services.GenreConverter
import javax.annotation.concurrent.Immutable

@Immutable
@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey
    val movieId: Int = 1,
    val title: String = "",
    val overview: String = "",
    val picture: String = "",
    @TypeConverters(GenreConverter::class)
    val genres: List<Genre> = listOf(),
    val releaseDate: String = "",
    val voteAverage: String? = "",
    var isFavorite: Boolean = false
)