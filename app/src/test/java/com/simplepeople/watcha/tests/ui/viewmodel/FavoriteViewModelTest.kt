package com.simplepeople.watcha.tests.ui.viewmodel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.usecase.FavoriteUseCase
import com.simplepeople.watcha.tests.data.FakeMovieFavoriteRepository
import com.simplepeople.watcha.ui.main.favorite.FavoriteScreenMovieListState
import com.simplepeople.watcha.ui.main.favorite.FavoriteScreenState
import com.simplepeople.watcha.ui.main.favorite.FavoriteViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkObject
import junit.framework.TestCase.fail
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class FavoriteViewModelTest {

    private lateinit var favoriteUseCase: FavoriteUseCase
    private lateinit var favoriteViewModel: FavoriteViewModel

    @Before
    fun setUp() {
        favoriteUseCase = FavoriteUseCase(FakeMovieFavoriteRepository())
        favoriteViewModel = FavoriteViewModel(favoriteUseCase)
    }

    @After
    fun tearDown() {
    }
    
    @Test
    fun `Load favorite movies in ViewModel instantiation - Happy path`() = runTest {
        favoriteViewModel.favoriteScreenState.test {
            val favoriteScreenState = awaitItem()
            assertThat(favoriteScreenState).isNotInstanceOf(FavoriteScreenMovieListState.Loading::class.java)
            when (val movieListState = favoriteScreenState.movieListState) {
                is FavoriteScreenMovieListState.Error,
                FavoriteScreenMovieListState.Loading -> {
                    fail("Not expected behavior")
                }
                is FavoriteScreenMovieListState.Success -> {
                    assertThat(movieListState.movieList).isNotEqualTo(emptyFlow<PagingData<Movie>>())
                    assertThat(favoriteScreenState).isNotEqualTo(FavoriteScreenState())
                }
            }
        }
    }

    @Test
    fun getFavoriteScreenState() = runTest {
        favoriteViewModel.favoriteScreenState.test {
            val favoriteScreenState = awaitItem()
            assertThat(favoriteScreenState).isInstanceOf(FavoriteScreenState::class.java)
            assertThat(favoriteScreenState).isNotEqualTo(FavoriteScreenState())
            assertThat(favoriteScreenState.movieListState).isNotInstanceOf(
                FavoriteScreenMovieListState.Error::class.java
            )
        }
    }

    @Test
    fun scrollingToTop() = runTest {
        val scrollToTop = true
        favoriteViewModel.favoriteScreenState.test {
            val previousValue = awaitItem().scrollToTop
            favoriteViewModel.scrollingToTop(scrollToTop)
            val currentValue = awaitItem().scrollToTop
            assertThat(previousValue).isNotEqualTo(currentValue)
            assertThat(currentValue).isTrue()
        }
    }
}

class FavoriteViewModelTestErrorPath {

    @MockK
    private lateinit var favoriteUseCase: FavoriteUseCase

    private lateinit var favoriteViewModel: FavoriteViewModel


    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { favoriteUseCase.getFavorites() } throws Exception()

        favoriteViewModel = FavoriteViewModel(favoriteUseCase)
    }

    @After
    fun tearDown() {
        unmockkObject(favoriteUseCase)
    }

    @Test
    fun `Load favorite movies in ViewModel instantiation - Error path`() = runTest {
        favoriteViewModel.favoriteScreenState.test {
            val favoriteScreenState = awaitItem()
            assertThat(favoriteScreenState.movieListState).isInstanceOf(FavoriteScreenMovieListState.Error::class.java)
        }
    }
}