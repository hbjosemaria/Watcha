package com.simplepeople.watcha.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.core.SearchLogItem
import com.simplepeople.watcha.domain.usecase.MovieListUseCase
import com.simplepeople.watcha.domain.usecase.SearchLogUseCase
import com.simplepeople.watcha.ui.common.composables.NavigationBarItemSelection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieListUseCase: MovieListUseCase,
    private val searchLogUseCase: SearchLogUseCase,
) : ViewModel() {

    private val _searchScreenState = MutableStateFlow(SearchScreenState())
    val searchScreenState = _searchScreenState.asStateFlow()

    init {
        loadRecentSearchLog()
    }

    fun isSearching(text: String) {
        _searchScreenState.value = searchScreenState.value.copy(
            searching = true,
            scrollToTop = false,
            searchText = text,
            movieListState = SearchScreenMovieListState.Loading
        )
    }

    private fun loadRecentSearchLog() {
        viewModelScope.launch(
            context = Dispatchers.IO
        ) {
            searchLogUseCase
                .getRecentSearch()
                .collect { searchLog ->
                    _searchScreenState.value = searchScreenState.value.copy(
                        searchLog = searchLog
                    )
                }
        }
    }

    fun cleanSearchLog() {
        viewModelScope.launch(
            context = Dispatchers.IO
        ) {
            searchLogUseCase.cleanSearchLog()
        }
    }

    fun addNewSearch(searchedText: String) {
        viewModelScope.launch(
            context = Dispatchers.IO
        ) {
            searchLogUseCase.addNewSearch(searchedText)
        }
    }

    fun removeSearch(searchLogItem: SearchLogItem) {
        viewModelScope.launch(
            context = Dispatchers.IO
        ) {
            searchLogUseCase.removeSearch(searchLogItem)
        }
    }

    fun cleanSearchText() {
        _searchScreenState.value = searchScreenState.value.copy(
            searchText = ""
        )
    }

    fun getMoviesByTitle(text: String) {
        viewModelScope.launch(
            context = Dispatchers.IO
        ) {
            try {
                _searchScreenState.value = searchScreenState.value.copy(
                    movieListState = SearchScreenMovieListState.Success(
                        movieList = movieListUseCase
                            .getByTitle(text)
                            .cachedIn(viewModelScope)
                    )
                )
            } catch (e: Exception) {
                _searchScreenState.value = searchScreenState.value.copy(
                    movieListState = SearchScreenMovieListState.Error(
                        message = R.string.movie_list_error
                    )
                )
            }

            resetAfterSearch()
        }
    }

    fun cleanMovieSearch() {
        _searchScreenState.value = searchScreenState.value.copy(
            searching = false,
            movieListState = SearchScreenMovieListState.Success(
                movieList = emptyFlow(),
            )
        )
    }

    private fun resetAfterSearch() {
        _searchScreenState.value = searchScreenState.value.copy(
            searching = false,
            scrollToTop = true
        )
    }

    fun updateNavigationItemIndex(index: Int) {
        NavigationBarItemSelection.selectedNavigationItemIndex = index
    }

}