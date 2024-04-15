package com.simplepeople.watcha.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.core.exampleMovieSet
import com.simplepeople.watcha.ui.stateholder.MovieDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MovieDetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailsUiState())
    val uiState : StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    /*TODO: fetch movie details
    init {
        getMovieDetails(movieId)
    }*/

    fun toggleFavorite(movie: Movie) {
        _uiState.value = MovieDetailsUiState(isFavourite = !_uiState.value.isFavourite)
        if (_uiState.value.isFavourite) {
            //TODO: save movie into local database list of favorites
        }
        else {
            //TODO: remove movie from local database list of favorites
        }
    }

    fun getMovieDetails(movieId: Int): Movie {
        return exampleMovieSet.find { it.movieId == movieId } ?: exampleMovieSet.first() //TODO: call to repo for fetching movie details and then remove this example
    }

}