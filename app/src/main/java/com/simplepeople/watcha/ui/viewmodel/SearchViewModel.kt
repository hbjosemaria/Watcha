@file:OptIn(ExperimentalFoundationApi::class)

package com.simplepeople.watcha.ui.viewmodel

import androidx.compose.foundation.ExperimentalFoundationApi
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

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieListUseCase: MovieListUseCase
): ViewModel()  {

    var movieList = MutableStateFlow<List<Movie>>(listOf())
        private set

    var textFieldText = MutableStateFlow("")
        private set

    fun onTextFieldChange(text: String) {
        textFieldText.value = text
    }

    fun getMoviesByTitle(searchText: String) {
        viewModelScope.launch {
            movieList.value = withContext(Dispatchers.IO) {
                movieListUseCase.getByTitle(searchText)
            }
        }
    }

    fun cleanMovieSearch() {
        movieList.value = movieList.value.drop(movieList.value.size)
    }

}