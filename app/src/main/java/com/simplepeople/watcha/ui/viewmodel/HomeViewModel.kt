package com.simplepeople.watcha.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    init {
        getDefaultMovieSet()
    }

    fun getDefaultMovieSet() {
        viewModelScope.launch {
            movieSet.value = withContext(Dispatchers.IO) {
                getMovieListUseCase.getMovieListMapped()
            }
        }
    }
}