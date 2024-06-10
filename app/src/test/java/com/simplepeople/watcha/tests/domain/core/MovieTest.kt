package com.simplepeople.watcha.tests.domain.core

import com.simplepeople.watcha.data.model.local.MovieEntity
import com.simplepeople.watcha.data.model.local.MovieFavoriteEntity
import com.simplepeople.watcha.domain.core.Genre
import com.simplepeople.watcha.domain.core.Movie
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MovieTest {

    private lateinit var baseMovie : Movie

    @Before
    fun setUp() {
        baseMovie = Movie(
            movieId = 2369,
            title = "My ascension as Android Kotlin + Compose developer expert.",
            overview = "This is the journey where I become a better Android Developer and achieve the maximum expertise.",
            picture = "No picture, thanks.jpg",
            genres = listOf(Genre.HISTORY, Genre.SCIENCE_FICTION, Genre.ACTION),
            releaseDate = "13/06/2024",
            voteAverage = "10",
            isFavorite = false
        )
    }

    @Test
    fun `Mapping Movie to Entity`() {
        val expectedMovieEntity = MovieEntity(
            movieId = baseMovie.movieId,
            title = baseMovie.title,
            overview = baseMovie.overview,
            picture = baseMovie.picture,
            genres = baseMovie.genres,
            releaseDate = baseMovie.releaseDate,
            voteAverage = baseMovie.voteAverage,
            isFavorite = baseMovie.isFavorite
        )
        assertEquals(expectedMovieEntity, baseMovie.toEntity())
    }

    @Test
    fun `Mapping Movie to MovieFavoriteEntity`() {
        val expectedFavoriteEntity = MovieFavoriteEntity(
            movie = MovieEntity(
                movieId = baseMovie.movieId,
                title = baseMovie.title,
                overview = baseMovie.overview,
                picture = baseMovie.picture,
                genres = baseMovie.genres,
                releaseDate = baseMovie.releaseDate,
                voteAverage = baseMovie.voteAverage,
                isFavorite = baseMovie.isFavorite
            )
        )
        assertEquals(expectedFavoriteEntity, baseMovie.toFavoriteEntity())
    }
}