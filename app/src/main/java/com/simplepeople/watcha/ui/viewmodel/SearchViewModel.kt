@file:OptIn(ExperimentalFoundationApi::class)

package com.simplepeople.watcha.ui.viewmodel

import androidx.compose.foundation.ExperimentalFoundationApi
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

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getMovieListUseCase: GetMovieListUseCase
): ViewModel()  {

    var movieSet = MutableStateFlow<Set<Movie>>(setOf())
        private set

    var textFieldText = MutableStateFlow("")
        private set

    fun onTextFieldChange(text: String) {
        textFieldText.value = text
    }

    fun getMoviesByTitle(searchText: String) {
        viewModelScope.launch {
            movieSet.value = withContext(Dispatchers.IO) {
                getMovieListUseCase.getByTitle(searchText)
            }
        }
    }

    fun cleanMovieSearch() {
        movieSet.value = movieSet.value.drop(movieSet.value.size).toSet()
    }

}