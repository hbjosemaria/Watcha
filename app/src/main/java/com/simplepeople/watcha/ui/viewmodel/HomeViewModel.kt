package com.simplepeople.watcha.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.core.exampleMovieSet
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel : ViewModel() {

    var movieSet = MutableStateFlow<Set<Movie>>(setOf())
        private set

    init {
        getDefaultMovieSet()
    }

    fun getDefaultMovieSet() {
        movieSet.value = exampleMovieSet //TODO: fetch all movies list from API.
    }
}