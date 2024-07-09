package com.simplepeople.watcha.tests.ui.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ibm.icu.impl.Assert.fail
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.usecase.FavoriteUseCase
import com.simplepeople.watcha.domain.usecase.MovieUseCase
import com.simplepeople.watcha.tests.data.FakeData
import com.simplepeople.watcha.tests.data.FakeMixedMovieRepositoryImpl
import com.simplepeople.watcha.tests.data.FakeMovieFavoriteRepository
import com.simplepeople.watcha.ui.common.utils.SnackbarItem
import com.simplepeople.watcha.ui.main.moviedetails.MovieDetailsMovieState
import com.simplepeople.watcha.ui.main.moviedetails.MovieDetailsViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class MovieDetailsViewModelTest {

    @MockK(relaxed = true)
    private lateinit var movieUseCaseMocked : MovieUseCase

    @MockK(relaxed = true)
    private lateinit var favoriteUseCaseMocked: FavoriteUseCase
    private lateinit var viewModelWithFavoriteUseCaseMocked : MovieDetailsViewModel
    private lateinit var viewModelWithMovieUseCaseMocked : MovieDetailsViewModel

    private lateinit var favoriteUseCase : FavoriteUseCase
    private lateinit var movieUseCase : MovieUseCase
    private val movieId = 8L

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        favoriteUseCase = FavoriteUseCase(FakeMovieFavoriteRepository())
        movieUseCase = MovieUseCase(FakeMixedMovieRepositoryImpl())

        movieDetailsViewModel = MovieDetailsViewModel(
            favoriteUseCase = favoriteUseCase,
            movieUseCase = movieUseCase,
            movieId = movieId
        )

        coEvery { movieUseCaseMocked.getMovieById(movieId) } throws Exception()
        coEvery { favoriteUseCaseMocked.saveFavorite(any())} throws IllegalStateException()
        coEvery { favoriteUseCaseMocked.deleteFavorite(any())} throws IllegalStateException()

        viewModelWithMovieUseCaseMocked = MovieDetailsViewModel(
            favoriteUseCase = favoriteUseCase,
            movieUseCase = movieUseCaseMocked,
            movieId = movieId
        )

        viewModelWithFavoriteUseCaseMocked = MovieDetailsViewModel(
            favoriteUseCase = favoriteUseCaseMocked,
            movieUseCase = movieUseCase,
            movieId = movieId
        )

    }

    @After
    fun tearDown() {
    }

    @Test
    fun `Load movie details - Happy Path`() = runTest {
        val expectedMovie = FakeData.fakeMovieData.find {
            it.movieId == movieId
        }
        movieDetailsViewModel.movieDetailsState.test {
            when (val movieState = awaitItem().movieState) {
                is MovieDetailsMovieState.Error,
                MovieDetailsMovieState.Loading -> {
                    fail("Not expected behavior")
                }
                is MovieDetailsMovieState.Success -> {
                    val movie = movieState.movie
                    assertThat(movie).isNotNull()
                    assertThat(movie.movieId).isEqualTo(expectedMovie!!.movieId)
                }
            }
        }
    }

    @Test
    fun `Load movie details - Error Path`() = runTest {
        viewModelWithMovieUseCaseMocked.movieDetailsState.test {
            val state = awaitItem().movieState
            assertThat(state).isInstanceOf(MovieDetailsMovieState.Error::class.java)
        }
    }

    @Test
    fun getMovieDetailsState() = runTest {
        val defaultSnackbarItem = SnackbarItem()
        movieDetailsViewModel.movieDetailsState.test {
            val state = awaitItem()
            assertThat(state.snackBarItem).isEqualTo(defaultSnackbarItem)
            val movieState = state.movieState
            assertThat(movieState).isInstanceOf(MovieDetailsMovieState.Success::class.java)
        }
    }

    @Test
    fun `Toggle favorite - Happy path`() = runTest {
        val expectedSnackbarItem = SnackbarItem(
            show = true,
            isError =  false,
            message = R.string.favorite_add_success
        )
        movieDetailsViewModel.movieDetailsState.test {
            val previousState = awaitItem()
            val previousValue = previousState.snackBarItem
            movieDetailsViewModel.toggleFavorite()
            val currentState = awaitItem()
            val currentValue = currentState.snackBarItem
            assertThat(previousValue).isNotEqualTo(currentValue)
            assertThat(currentValue).isEqualTo(expectedSnackbarItem)
        }
    }

    @Test
    fun `Toggle favorite - Error path`() = runTest {
        viewModelWithFavoriteUseCaseMocked.movieDetailsState.test {
            val previousState = awaitItem()
            viewModelWithFavoriteUseCaseMocked.toggleFavorite()
            val currentState = awaitItem()
            assertThat(currentState.movieState).isInstanceOf(MovieDetailsMovieState.Success::class.java)
            assertThat(currentState.snackBarItem.isError).isTrue()
            assertThat(previousState).isNotEqualTo(currentState)
        }
    }

    @Test
    fun resetSnackbar() = runTest {
        val expectedSnackbarItem = SnackbarItem (
            show = false
        )
        movieDetailsViewModel.movieDetailsState.test {
            val previousValue = awaitItem().snackBarItem
            movieDetailsViewModel.toggleFavorite()
            val intermediateValue = awaitItem().snackBarItem
            movieDetailsViewModel.resetSnackbar()
            val currentValue = awaitItem().snackBarItem
            assertThat(previousValue).isNotEqualTo(intermediateValue)
            assertThat(intermediateValue.show).isTrue()
            assertThat(intermediateValue).isNotEqualTo(currentValue)
            assertThat(currentValue.show).isFalse()
            assertThat(currentValue.show).isEqualTo(expectedSnackbarItem.show)
        }
    }
}
