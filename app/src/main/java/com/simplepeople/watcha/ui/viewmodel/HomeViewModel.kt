package com.simplepeople.watcha.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.usecase.GetMovieListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//TODO: uiState for success, error and loading screen state

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMovieListUseCase: GetMovieListUseCase
) : ViewModel() {

    var movieSet = MutableStateFlow<Set<Movie>>(setOf())
        private set

    var currentPage = MutableStateFlow(1)
        private set

    init {
        getFirstPage()
    }

    fun getFirstPage() {
        viewModelScope.launch {
            movieSet.value = withContext(Dispatchers.IO) {
                getMovieListUseCase.getFirstPage()
            }
        }
    }

    fun getNextPage() {
        viewModelScope.launch{
            currentPage.value++
            movieSet.value = movieSet.value.plus(withContext(Dispatchers.IO) {
                Log.i("Loading next page: ", currentPage.value.toString())
                getMovieListUseCase.getNextPage(currentPage.value)
            })
        }
    }
}