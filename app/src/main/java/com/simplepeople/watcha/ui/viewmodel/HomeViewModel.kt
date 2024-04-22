package com.simplepeople.watcha.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.usecase.MovieListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

//TODO: uiState for success, error and loading screen state

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieListUseCase: MovieListUseCase
) : ViewModel() {

    var movieList : Flow<PagingData<Movie>> = MutableStateFlow(PagingData.empty())
        private set

    init {
        loadMovies()
    }

    private fun loadMovies() {
        movieList = movieListUseCase
            .getMovies()
            .cachedIn(viewModelScope)
    }

}