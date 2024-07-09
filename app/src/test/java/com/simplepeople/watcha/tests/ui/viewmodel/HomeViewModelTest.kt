package com.simplepeople.watcha.tests.ui.viewmodel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.usecase.CacheUseCase
import com.simplepeople.watcha.domain.usecase.MovieListUseCase
import com.simplepeople.watcha.tests.data.FakeData
import com.simplepeople.watcha.ui.common.composables.NavigationBarIndex
import com.simplepeople.watcha.ui.common.composables.topbar.HomeFilterOptions
import com.simplepeople.watcha.ui.common.utils.ConnectivityState
import com.simplepeople.watcha.ui.main.home.HomeScreenMovieListState
import com.simplepeople.watcha.ui.main.home.HomeScreenState
import com.simplepeople.watcha.ui.main.home.HomeViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

    @MockK
    private lateinit var movieListUseCase : MovieListUseCase

    @MockK
    private lateinit var cacheUseCase : CacheUseCase

    @MockK
    private lateinit var connectivityState: ConnectivityState

    private lateinit var homeViewModel : HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)

        coEvery { movieListUseCase.getMovies(HomeFilterOptions.NowPlaying) } returns flowOf(PagingData.from((FakeData.fakeMovieData)))

        val connectivityStateResult : StateFlow<Boolean> = MutableStateFlow(true)
        coEvery { connectivityState.connectivityStateFlow } returns connectivityStateResult

        coJustRun {cacheUseCase.forceCacheExpiration()}

        homeViewModel = HomeViewModel(
            movieListUseCase = movieListUseCase,
            cacheUseCase = cacheUseCase,
            _connectivityState = connectivityState
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadMovies() = runTest {
        homeViewModel.homeScreenState.test {
            val state = awaitItem()
            when (val movieListState = state.movieListState) {
                is HomeScreenMovieListState.Error,
                HomeScreenMovieListState.Loading -> {}
                is HomeScreenMovieListState.Success -> {
                    assertThat(movieListState).isInstanceOf(HomeScreenMovieListState.Success::class.java)
                    val movieList = movieListState.movieList.first()
                    assertThat(movieList).isNotNull()
                }
            }
        }
    }

    @Test
    fun getHomeScreenState() = runTest {
        homeViewModel.homeScreenState.test {
            val state = awaitItem()
            assertThat(state).isInstanceOf(HomeScreenState::class.java)
            assertThat(state).isNotNull()
        }
    }

    @Test
    fun getConnectivityState() = runTest {
        homeViewModel.connectivityState.test {
            val state = awaitItem()
            assertThat(state).isTrue()
        }
    }

    @Test
    fun updateTopBarSelection() = runTest {
        val newSelection = HomeFilterOptions.Popular
        homeViewModel.homeScreenState.test {
            val previousSelection = awaitItem().selectedHomeFilterOption
            homeViewModel.updateTopBarSelection(newSelection)
            val currentSelection = awaitItem().selectedHomeFilterOption
            assertThat(previousSelection).isNotSameInstanceAs(currentSelection)
            assertThat(currentSelection).isSameInstanceAs(newSelection)
        }
    }

    @Test
    fun scrollingToTop() = runTest {
        val scrollToTop = true
        homeViewModel.homeScreenState.test {
            val previousValue = awaitItem().scrollToTop
            homeViewModel.scrollingToTop(scrollToTop)
            val currentValue = awaitItem().scrollToTop
            assertThat(previousValue).isNotEqualTo(currentValue)
            assertThat(currentValue).isTrue()
        }
    }

    @Test
    fun updateNavigationItemIndex() {
        val newNavigationIndex = 1
        val previousNavigationIndex = NavigationBarIndex.selectedIndex
        homeViewModel.updateNavigationItemIndex(newNavigationIndex)
        val currentNavigationIndex = NavigationBarIndex.selectedIndex
        assertThat(previousNavigationIndex).isNotEqualTo(currentNavigationIndex)
    }

    @Test
    fun reloadMovies() = runTest {
        homeViewModel.homeScreenState.test {
            val previousState = awaitItem().movieListState
            assertThat(previousState).isInstanceOf(HomeScreenMovieListState.Success::class.java)

            when (previousState) {
                is HomeScreenMovieListState.Error,
                HomeScreenMovieListState.Loading -> {}
                is HomeScreenMovieListState.Success -> {
                    val previousValue = previousState.movieList.first()
                    assertThat(previousValue).isNotEqualTo(PagingData.empty<Movie>())
                }
            }

            homeViewModel.reloadMovies()

            val intermediateState = awaitItem().movieListState
            assertThat(intermediateState).isInstanceOf(HomeScreenMovieListState.Success::class.java)

            when (intermediateState) {
                is HomeScreenMovieListState.Error,
                HomeScreenMovieListState.Loading -> {}
                is HomeScreenMovieListState.Success -> {
                    try {
                        intermediateState.movieList.first()
                    } catch (e : NoSuchElementException) {
                        assertThat(e).isInstanceOf(NoSuchElementException::class.java)
                    }
                }
            }

            val currentState = awaitItem()
            when (val state = currentState.movieListState) {
                is HomeScreenMovieListState.Error,
                HomeScreenMovieListState.Loading -> {}
                is HomeScreenMovieListState.Success -> {
                    val currentValue = state.movieList.first()
                    assertThat(currentValue).isNotEqualTo(PagingData.empty<Movie>())
                }
            }
        }
    }

    @Test
    fun cleanMovieList() = runTest {
        homeViewModel.homeScreenState.test {
            val previousState = awaitItem()
            when (val state = previousState.movieListState) {
                is HomeScreenMovieListState.Error,
                HomeScreenMovieListState.Loading -> {}
                is HomeScreenMovieListState.Success -> {
                    val previousValue = state.movieList.first()
                    assertThat(previousValue).isNotEqualTo(PagingData.empty<Movie>())
                }
            }

            homeViewModel.cleanMovieList()

            val currentState = awaitItem()
            when (val state = currentState.movieListState) {
                is HomeScreenMovieListState.Error,
                HomeScreenMovieListState.Loading -> {}
                is HomeScreenMovieListState.Success -> {
                    try {
                        state.movieList.first()
                    } catch (e : NoSuchElementException) {
                        assertThat(e).isInstanceOf(NoSuchElementException::class.java)
                    }
                }
            }
        }
    }
}