package com.simplepeople.watcha.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.core.SearchLogItem
import com.simplepeople.watcha.domain.usecase.MovieListUseCase
import com.simplepeople.watcha.domain.usecase.SearchLogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieListUseCase: MovieListUseCase,
    private val searchLogUseCase: SearchLogUseCase
) : ViewModel() {

    private val _searchScreenUiState = MutableStateFlow(SearchScreenUiState())
    val searchScreenUiState = _searchScreenUiState.asStateFlow()

    init {
        loadRecentSearchLog()
    }

    fun isSearching(text: String) {
        _searchScreenUiState.value = searchScreenUiState.value.copy(
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
            _searchScreenUiState.value = searchScreenUiState.value.copy(
                searchLog = searchLogUseCase
                    .getRecentSearch()
                    .catch { exception ->
                        _searchScreenUiState.value = _searchScreenUiState.value.copy(
                            searchLog = MutableStateFlow(PagingData.empty())
                        )
                    }
                    .cachedIn(viewModelScope)
            )
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
        _searchScreenUiState.value = searchScreenUiState.value.copy(
            searchText = ""
        )
    }

    fun getMoviesByTitle(text: String) {
        viewModelScope.launch(
            context = Dispatchers.IO
        ) {
            _searchScreenUiState.value = searchScreenUiState.value.copy(
                movieListState = SearchScreenMovieListState.Success(
                    movieList = movieListUseCase
                        .getByTitle(text)
                        .catch { exception ->
                            _searchScreenUiState.value = searchScreenUiState.value.copy(
                                movieListState = SearchScreenMovieListState.Error(
                                    message = R.string.movie_list_error
                                )
                            )
                        }
                        .cachedIn(viewModelScope)
                )
            )

            resetAfterSearch()
        }
    }

    fun cleanMovieSearch() {
        _searchScreenUiState.value = searchScreenUiState.value.copy(
            searching = false,
            movieListState = SearchScreenMovieListState.Success(
                movieList = emptyFlow(),
            )
        )
    }

    private fun resetAfterSearch() {
        _searchScreenUiState.value = searchScreenUiState.value.copy(
            searching = false,
            scrollToTop = true
        )
    }

}