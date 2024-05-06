package com.simplepeople.watcha.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.usecase.MovieListUseCase
import com.simplepeople.watcha.ui.stateholder.SearchScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieListUseCase: MovieListUseCase
): ViewModel()  {

    private val _movieList = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val movieList = _movieList.asStateFlow()
    val searchScreenUiState = mutableStateOf(SearchScreenUiState())

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

}