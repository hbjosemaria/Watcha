package com.simplepeople.watcha.ui.main.moviedetails

import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.common.utils.SnackbarItem

data class MovieDetailsState(
    val snackBarItem: SnackbarItem = SnackbarItem(),
    val movieState: MovieDetailsMovieState = MovieDetailsMovieState.Loading,
)

sealed class MovieDetailsMovieState {
    data object Loading : MovieDetailsMovieState()
    data class Success(val movie: Movie) : MovieDetailsMovieState()
    data class Error(val message: Int) : MovieDetailsMovieState()
}