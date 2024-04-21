package com.simplepeople.watcha.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.usecase.MovieListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//TODO: uiState for success, error and loading screen state

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieListUseCase: MovieListUseCase
) : ViewModel() {

    var movieLIst = MutableStateFlow<List<Movie>>(listOf())
        private set

    var currentPage = MutableStateFlow(1)
        private set

    init {
        getFirstPage()
    }

    fun getFirstPage() {
        viewModelScope.launch {
            movieLIst.value = withContext(Dispatchers.IO) {
                movieListUseCase.getFirstPage()
            }
        }
    }

    fun getNextPage() {
        viewModelScope.launch{
            currentPage.value++
            movieLIst.value = movieLIst.value.plus(withContext(Dispatchers.IO) {
                movieListUseCase.getNextPage(currentPage.value)
            })
        }
    }
}