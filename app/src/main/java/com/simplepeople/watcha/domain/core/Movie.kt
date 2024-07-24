package com.simplepeople.watcha.domain.core

import com.simplepeople.watcha.R
import com.simplepeople.watcha.data.model.local.MovieEntity
import com.simplepeople.watcha.data.model.local.MovieFavoriteEntity
import javax.annotation.concurrent.Immutable

@Immutable
data class Movie(
    val movieId: Long = 1,
    val title: String = "",
    val overview: String = "",
    val tagline: String = "",
    val picture: String = "",
    val backdrop: String = "",
    val genres: List<Genre> = listOf(),
    val runTime: Int = 0,
    val releaseDate: String = "",
    val voteAverage: Int? = null,
    val webUrl: String = "",
    var isFavorite: Boolean = false,
    val images: List<String> = emptyList(),
    val trailer: String? = null,
    val credits: List<CastMember> = emptyList(),
    val review: Review? = Review(),
    val recommendations: List<MovieItem> = emptyList(),
    val similar: List<MovieItem> = emptyList()
) {

    data class Review(
        val author: String = "",
        val avatarPath: String? = null,
        val rating: Int? = null,
        val content: String = "",
        val updatedAt: String = ""
    )

    data class MovieItem(
        val id: Long = 0,
        val title: String = "",
        val posterPath: String? = null,
    )

    data class CastMember(
        val name: String = "",
        val profilePath: String? = null,
        val character: String = "",
        val order: Int = 0
    )

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

enum class Genre(val id: Int, val title: Int) {
    ACTION(28, R.string.genre_action),
    ADVENTURE(12, R.string.genre_adventure),
    ANIMATION(16, R.string.genre_animation),
    COMEDY(35, R.string.genre_comedy),
    CRIME(80, R.string.genre_crime),
    DOCUMENTARY(99, R.string.genre_documentary),
    DRAMA(18, R.string.genre_drama),
    FAMILY(10751, R.string.genre_family),
    FANTASY(14, R.string.genre_fantasy),
    HISTORY(36, R.string.genre_history),
    HORROR(27, R.string.genre_horror),
    MUSIC(10402, R.string.genre_music),
    MYSTERY(9648, R.string.genre_mystery),
    ROMANCE(10749, R.string.genre_romance),
    SCIENCE_FICTION(878, R.string.genre_science_fiction),
    TV_MOVIE(10770, R.string.genre_tv_movie),
    THRILLER(53, R.string.genre_thriller),
    WAR(10752, R.string.genre_war),
    WESTERN(37, R.string.genre_western),
    NOT_SPECIFIED(-1, R.string.genre_not_specified)
}