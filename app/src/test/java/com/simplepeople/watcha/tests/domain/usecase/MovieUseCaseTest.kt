package com.simplepeople.watcha.tests.domain.usecase

import androidx.compose.ui.text.intl.Locale
import com.google.common.truth.Truth.assertThat
import com.simplepeople.watcha.data.repository.MixedMovieRepository
import com.simplepeople.watcha.tests.data.FakeMixedMovieRepositoryImpl
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class MovieUseCaseTest {

    private lateinit var fakeMixedMovieRepository : MixedMovieRepository

    @Before
    fun setUp() {
        fakeMixedMovieRepository = FakeMixedMovieRepositoryImpl()
    }

    @Test
    fun `Fetch movie data`() = runTest {
        val movieId = 6L
        val locale = Locale.current.language
        val (movieDto, isFavorite) = fakeMixedMovieRepository.getMovieById(movieId, locale)
        val movie = movieDto.toDomain().copy(
            isFavorite = isFavorite > 0
        )
        assertThat(movie).isNotNull()
    }
}