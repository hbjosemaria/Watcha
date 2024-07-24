package com.simplepeople.watcha.tests.ui.viewmodel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ibm.icu.impl.Assert.fail
import com.simplepeople.watcha.data.model.local.SearchLogItemEntity
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.core.SearchLogItem
import com.simplepeople.watcha.domain.usecase.MovieListUseCase
import com.simplepeople.watcha.domain.usecase.SearchLogUseCase
import com.simplepeople.watcha.tests.data.FakeData
import com.simplepeople.watcha.tests.data.FakeSearchRepositoryImpl
import com.simplepeople.watcha.ui.main.search.SearchScreenMovieListState
import com.simplepeople.watcha.ui.main.search.SearchViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchViewModelTest {

    @MockK(relaxed = true)
    private lateinit var movieListUseCase : MovieListUseCase
    @MockK(relaxed = true)
    private lateinit var movieListUseCaseErrorPath : MovieListUseCase
    private lateinit var searchLogUseCase : SearchLogUseCase
    private val searchText = "Movie 1"
    private val backupSearchData = FakeData.fakeSearchData.toMutableList()

    private lateinit var searchViewModel : SearchViewModel
    private lateinit var searchViewModelErrorPath : SearchViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        searchLogUseCase = SearchLogUseCase(
            repository = FakeSearchRepositoryImpl()
        )

        every {movieListUseCase.getByTitle(searchText)} returns flowOf(
            PagingData.from(
                FakeData.fakeMovieData.filter {
                    it.title.contains(searchText)
                }
            )
        )

        searchViewModel = SearchViewModel(
            movieListUseCase = movieListUseCase,
            searchLogUseCase = searchLogUseCase
        )

        every {movieListUseCaseErrorPath.getByTitle(any())} throws Exception()

        searchViewModelErrorPath = SearchViewModel(
            movieListUseCase = movieListUseCaseErrorPath,
            searchLogUseCase = searchLogUseCase
        )
    }

    @After
    fun tearDown() {
        if(FakeData.fakeSearchData.isEmpty()) FakeData.fakeSearchData.addAll(backupSearchData)
    }

    @Test
    fun `Search movies - Happy path`() = runTest {
        searchViewModel.searchScreenState.test {
            val previousState = awaitItem()
            assertThat(previousState.movieListState).isInstanceOf(SearchScreenMovieListState.Loading::class.java)
            searchViewModel.getMoviesByTitle(searchText)
            val currentState = awaitItem()
            when (val movieListState = currentState.movieListState) {
                is SearchScreenMovieListState.Error,
                SearchScreenMovieListState.Loading -> {
                    fail("Not expected behavior")
                }
                is SearchScreenMovieListState.Success -> {
                    assertThat(movieListState.movieList).isNotEqualTo(emptyFlow<PagingData<Movie>>())
                }
            }
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Search movies - Error path`() = runTest {
        searchViewModelErrorPath.searchScreenState.test {
            awaitItem()
            searchViewModelErrorPath.getMoviesByTitle(searchText)
            val currentState = awaitItem()
            assertThat(currentState.movieListState).isInstanceOf(SearchScreenMovieListState.Error::class.java)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun getSearchScreenState() = runTest {
        searchViewModel.searchScreenState.test {
            val state = awaitItem()
            assertThat(state.movieListState).isInstanceOf(SearchScreenMovieListState.Loading::class.java)
            assertThat(FakeData.fakeSearchData.map { it.toDomain() }).containsAtLeastElementsIn(state.searchLog)
        }
    }

    @Test
    fun isSearching() = runTest {
        searchViewModel.searchScreenState.test {
            val previousState = awaitItem()
            assertThat(previousState.searching).isFalse()
            assertThat(previousState.movieListState).isInstanceOf(SearchScreenMovieListState.Loading::class.java)
            searchViewModel.isSearching(searchText)
            val currentState = awaitItem()
            assertThat(currentState.searching).isTrue()
            assertThat(currentState.searchText).isEqualTo(searchText)
            assertThat(currentState.scrollToTop).isFalse()
        }
    }

    @Test
    fun cleanSearchLog() = runTest {
        searchViewModel.searchScreenState.test {
            val state = awaitItem()
            val previousValue = state.searchLog
            assertThat(previousValue).isNotEmpty()
            searchViewModel.cleanSearchLog()
            val currentValue = FakeData.fakeSearchData
            assertThat(previousValue).isNotEqualTo(currentValue)
            assertThat(currentValue).isEmpty()
        }
    }

    @Test
    fun addNewSearch() = runTest {
        val newSearch = "Interstellar"
        val expectedItem = SearchLogItemEntity(
            searchedText = newSearch
        )
        searchViewModel.searchScreenState.test {
            val state = awaitItem()
            val previousValue = state.searchLog
            assertThat(previousValue).isNotEmpty()
            val job = launch {
                searchViewModel.addNewSearch(newSearch)
            }
            job.join()
            assertThat(FakeData.fakeSearchData).contains(expectedItem)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun removeSearch() = runTest {
        val removeTargetSearch = SearchLogItem(
            searchedText = "Rocky"
        )
        searchViewModel.searchScreenState.test {
            val state = awaitItem()
            val searchLog = state.searchLog
            assertThat(searchLog).isNotEmpty()
            assertThat(searchLog).contains(removeTargetSearch)
            val job = launch {
                searchViewModel.removeSearch(removeTargetSearch)
            }
            job.join()
            advanceUntilIdle()
            val targetExists = FakeData.fakeSearchData.find { it.searchedText == "Rocky" }
            assertThat(targetExists).isNull()
        }
    }

    @Test
    fun cleanSearchText() = runTest {
        searchViewModel.searchScreenState.test {
            val previousState = awaitItem()
            val previousValue = previousState.searchText
            assertThat(previousValue).isEmpty()
            searchViewModel.isSearching("This is a test movie")
            val intermediateState = awaitItem()
            val intermediateValue = intermediateState.searchText
            assertThat(intermediateValue).isNotEmpty()
            searchViewModel.cleanSearchText()
            val currentState = awaitItem()
            val currentValue = currentState.searchText
            assertThat(currentValue).isEmpty()
        }
    }

    @Test
    fun cleanMovieSearch() = runTest {
        searchViewModel.searchScreenState.test {
            val previousState = awaitItem()
            assertThat(previousState.movieListState).isInstanceOf(SearchScreenMovieListState.Loading::class.java)
            searchViewModel.cleanMovieSearch()
            val currentState = awaitItem()
            when (val movieListState = currentState.movieListState) {
                is SearchScreenMovieListState.Error,
                SearchScreenMovieListState.Loading -> {
                    fail("Not expected behavior")
                }
                is SearchScreenMovieListState.Success -> {
                    assertThat(movieListState.movieList).isEqualTo(emptyFlow<PagingData<Movie>>())
                }
            }
        }
    }

    @Test
    fun loadRecentSearchLog() = runTest {
        searchViewModel.searchScreenState.test {
            val state = awaitItem()
            assertThat(state.searchLog).isNotEmpty()
        }
    }
}