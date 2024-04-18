package com.simplepeople.watcha.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.core.exampleMovieSet
import com.simplepeople.watcha.domain.repo.ExternalMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: ExternalMovieRepository
) : ViewModel() {

    var movieSet = MutableStateFlow<Set<Movie>>(setOf())
        private set

    init {
        getDefaultMovieSet()
    }

    fun getDefaultMovieSet() {
        viewModelScope.launch {
            movieSet.value = movieRepository.getMovies()
        }
    }
}