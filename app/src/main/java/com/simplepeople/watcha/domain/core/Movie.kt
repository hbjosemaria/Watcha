package com.simplepeople.watcha.domain.core

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