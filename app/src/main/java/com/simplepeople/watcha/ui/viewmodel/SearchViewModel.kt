package com.simplepeople.watcha.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.usecase.MovieListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieListUseCase: MovieListUseCase
): ViewModel()  {

    var movieList = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
        private set

    var searchText = MutableStateFlow("")
        private set

    var searching = MutableStateFlow(false)
        private set

    var scrollToTop = MutableStateFlow(false)
        private set

    fun updateSearchText(text: String) {
        searchText.value = text
    }

    fun isSearching() {
        searching.value = true
        scrollToTop.value = false
    }

    fun getMoviesByTitle(text: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                movieListUseCase
                    .getByTitle(text)
                    .cachedIn(viewModelScope)
                    .collect {
                        movieList.value = it
                        searching.value = false
                        scrollToTop.value = true
                    }
            }
        }

    }

    fun cleanMovieSearch() {
        movieList.value = PagingData.empty()
        searching.value = false
    }

}