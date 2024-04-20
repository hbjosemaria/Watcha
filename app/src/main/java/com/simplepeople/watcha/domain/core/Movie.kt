package com.simplepeople.watcha.domain.core

import javax.annotation.concurrent.Immutable

@Immutable //TODO: remove from here isFavorite property and implement that in another class to make this one fully Immutable
data class Movie(
    val movieId: Int = 1,
    val title: String = "",
    val overview: String = "",
    val picture: String = "",
    val genres: List<Genre> = listOf(),
    val releaseDate: String = "",
    val voteAverage: String? = "",
    var isFavorite: Boolean = false
)