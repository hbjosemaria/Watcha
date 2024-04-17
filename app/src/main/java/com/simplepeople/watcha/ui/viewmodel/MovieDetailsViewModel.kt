package com.simplepeople.watcha.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.simplepeople.watcha.MainActivity
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.core.exampleMovieSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//TODO: uiState for success, error and loading screen state

class MovieDetailsViewModel(movieId: Int) : ViewModel() {

    /*Comment: this is one of the options suggested to use when working with Flow
    private val _movie = MutableStateFlow<Movie>(Movie())
    val movie = _movie.asStateFlow()*/

    //Comment: this is a better way, IMO, to work with Flow using Kotling custom setters
    var movie = MutableStateFlow<Movie>(Movie())
        private set

    init {
        getMovieDetails(movieId)
    }

    fun toggleFavorite() {
        movie.value = movie.value.copy(isFavorite = !movie.value.isFavorite)
        if (movie.value.isFavorite) {
            //TODO: save movie into local database list of favorites
        }
        else {
            //TODO: remove movie from local database list of favorites
        }
    }

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            movie.value = exampleMovieSet.find { it.movieId == movieId } ?: exampleMovieSet.first() //TODO: call to repo for fetching movie details and then remove this example
        }
    }
}

//Comment: a ViewModelFactory implementation was needed in order to initialize movie data in the MovieDetailsViewModel
class MovieDetailsViewModelFactory(private val movieId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieDetailsViewModel(movieId) as T
        }
        throw IllegalArgumentException("Not valid ViewModel class")
    }
}