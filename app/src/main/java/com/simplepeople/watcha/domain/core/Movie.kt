package com.simplepeople.watcha.domain.core

import com.simplepeople.watcha.data.model.local.MovieEntity
import com.simplepeople.watcha.data.model.local.MovieFavoriteEntity
import javax.annotation.concurrent.Immutable

@Immutable
data class Movie(
    val movieId: Long = 1,
    val title: String = "",
    val overview: String = "",
    val picture: String = "",
    val genres: List<Genre> = listOf(),
    val releaseDate: String = "",
    val voteAverage: String? = "",
    var isFavorite: Boolean = false,
) {
    fun toEntity(): MovieEntity {
        return MovieEntity(
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

    fun toFavoriteEntity(): MovieFavoriteEntity {
        return MovieFavoriteEntity(
            movie = this.toEntity()
        )
    }
}