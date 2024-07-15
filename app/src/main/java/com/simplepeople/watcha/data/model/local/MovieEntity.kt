package com.simplepeople.watcha.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.simplepeople.watcha.data.service.Room.GenreConverter
import com.simplepeople.watcha.domain.core.Genre
import com.simplepeople.watcha.domain.core.Movie

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey
    val movieId: Long = 1,
    val title: String = "",
    val overview: String = "",
    val picture: String = "",
    @TypeConverters(GenreConverter::class)
    val genres: List<Genre> = listOf(),
    val releaseDate: String = "",
    val voteAverage: String? = "",
    var isFavorite: Boolean = false,
) {
    fun toDomain(): Movie {
        return Movie(
            movieId = this.movieId,
            title = this.title,
            overview = this.overview,
            picture = this.picture,
            genres = this.genres,
            releaseDate = this.releaseDate,
            voteAverage = this.voteAverage,
            isFavorite = this.isFavorite
        )
    }
}
    
    
    
