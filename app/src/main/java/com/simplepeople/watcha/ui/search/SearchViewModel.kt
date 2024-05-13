package com.simplepeople.watcha.ui.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.core.SearchLogItem
import com.simplepeople.watcha.domain.usecase.MovieListUseCase
import com.simplepeople.watcha.domain.usecase.SearchLogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieListUseCase: MovieListUseCase,
    private val searchLogUseCase: SearchLogUseCase
): ViewModel()  {

    private val _movieList = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val movieList = _movieList.asStateFlow()
    private val _searchLog = MutableStateFlow<PagingData<SearchLogItem>>(PagingData.empty())
    val searchLog = _searchLog.asStateFlow()
    val searchScreenUiState = mutableStateOf(SearchScreenUiState())

    init {
        loadRecentSearchLog()
    }

    fun updateSearchText(text: String) {
        searchScreenUiState.value = searchScreenUiState.value.copy(
            searchText = text
        )
    }

    fun isSearching() {
        searchScreenUiState.value = searchScreenUiState.value.copy(
            searching = true,
            scrollToTop = false
        )
    }

    private fun loadRecentSearchLog() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchLogUseCase
                    .getRecentSearch()
                    .cachedIn(viewModelScope)
                    .collect {
                        _searchLog.value = it
                    }
            }
        }
    }

    fun cleanSearchLog() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchLogUseCase.cleanSearchLog()
            }
        }
    }

    fun addNewSearch(searchedText : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchLogUseCase.addNewSearch(searchedText)
            }
        }
    }

    fun removeSearch(searchLogItem : SearchLogItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchLogUseCase.removeSearch(searchLogItem)
            }
        }
    }

    fun getMoviesByTitle(text: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                movieListUseCase
                    .getByTitle(text)
                    .cachedIn(viewModelScope)
                    .collect {
                        _movieList.value = it
                        resetAfterSearch()
                    }
            }
        }
    }

    private fun resetAfterSearch() {
        searchScreenUiState.value = searchScreenUiState.value.copy(
            searching = false,
            scrollToTop = true
        )
    }

    fun cleanMovieSearch() {
        _movieList.value = PagingData.empty()
        searchScreenUiState.value = searchScreenUiState.value.copy(
            searching = false
        )
    }

    fun cleanSearchText() {
        searchScreenUiState.value = searchScreenUiState.value.copy(
            searchText = ""
        )
    }

}