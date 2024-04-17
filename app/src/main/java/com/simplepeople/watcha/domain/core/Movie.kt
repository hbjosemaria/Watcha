package com.simplepeople.watcha.domain.core

import com.simplepeople.watcha.R

data class Movie(
    val movieId: Int,
    val title: String,
    val overview: String,
    val picture: Int,
    val company: String,
    val genres: List<Genre>,
    val releaseDate: String,
    val voteAverage: String? = null,
    var isFavorite: Boolean = false
)

val exampleMovieSet = setOf(
    Movie(1, "Filling film 1", "This is an exciting filling film", R.drawable.watcha_logo, "My own company", listOf(Genre.ACTION, Genre.ADVENTURE, Genre.COMEDY), "2024-04-15", "5.35"),
    Movie(2, "Filling film 2", "This is the first sequel from the exciting filling film", R.drawable.watcha_logo, "My own company", listOf(Genre.ACTION, Genre.ADVENTURE, Genre.COMEDY), "2024-04-15", "7.15"),
    Movie(3, "Filling film 3", "This is conclusion from the exciting filling film", R.drawable.watcha_logo, "My own company", listOf(Genre.ACTION, Genre.ADVENTURE, Genre.COMEDY), "2024-04-15", "3.89"),
    Movie(4, "Filling film 4", "This is conclusion from the exciting filling film", R.drawable.watcha_logo, "My own company", listOf(Genre.ACTION, Genre.ADVENTURE, Genre.COMEDY), "2024-04-15", "3.89"),
    Movie(5, "Filling film 5", "This is conclusion from the exciting filling film", R.drawable.watcha_logo, "My own company", listOf(Genre.ACTION, Genre.ADVENTURE, Genre.COMEDY), "2024-04-15", "3.89"),
    Movie(6, "Filling film 6", "This is conclusion from the exciting filling film", R.drawable.watcha_logo, "My own company", listOf(Genre.ACTION, Genre.ADVENTURE, Genre.COMEDY), "2024-04-15", "3.89"),
    Movie(7, "Filling film 7", "This is conclusion from the exciting filling film", R.drawable.watcha_logo, "My own company", listOf(Genre.ACTION, Genre.ADVENTURE, Genre.COMEDY), "2024-04-15", "3.89"),
    Movie(8, "Filling film 8", "This is conclusion from the exciting filling film", R.drawable.watcha_logo, "My own company", listOf(Genre.ACTION, Genre.ADVENTURE, Genre.COMEDY), "2024-04-15", "3.89"),
    Movie(9, "Filling film 9", "This is conclusion from the exciting filling film", R.drawable.watcha_logo, "My own company", listOf(Genre.ACTION, Genre.ADVENTURE, Genre.COMEDY), "2024-04-15", "3.89"),
    Movie(10, "Filling film 10", "This is conclusion from the exciting filling film", R.drawable.watcha_logo, "My own company", listOf(Genre.ACTION, Genre.ADVENTURE, Genre.COMEDY), "2024-04-15", "3.89")
)