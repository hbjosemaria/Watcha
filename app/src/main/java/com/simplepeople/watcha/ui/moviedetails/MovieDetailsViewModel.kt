package com.simplepeople.watcha.ui.moviedetails

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.usecase.FavoriteUseCase
import com.simplepeople.watcha.domain.usecase.MovieUseCase
import com.simplepeople.watcha.ui.common.utils.SnackbarItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//TODO: uiState for success, error and loading screen state
//TODO: uiState for display Snackbar

@HiltViewModel(assistedFactory = MovieDetailsViewModel.MovieDetailsViewModelFactory::class)
class MovieDetailsViewModel @AssistedInject constructor(
    private val favoriteUseCase: FavoriteUseCase,
    private val movieUseCase: MovieUseCase,
//    private val favoriteEventFlow: SharedFavoriteEventFlow.Instance,
    @Assisted private val movieId: Long
) : ViewModel() {

    @AssistedFactory
    interface MovieDetailsViewModelFactory {
        fun create(movieId: Long): MovieDetailsViewModel
    }

    val movie = mutableStateOf(Movie())
    val movieDetailsUiState = mutableStateOf(MovieDetailsUiState())

    init {
        getMovieDetails()
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                movie.value = movie.value.copy(isFavorite = !movie.value.isFavorite)

                try {
                    if (movie.value.isFavorite) {
                        favoriteUseCase.saveFavorite(movie.value)
                        movieDetailsUiState.value = movieDetailsUiState.value.copy(
                            snackBarItem = SnackbarItem(
                                show = true,
                                isError = false,
                                message = R.string.favorite_add_success
                            ),
                        )
                    } else {
                        favoriteUseCase.deleteFavorite(movie.value.movieId)
                        movieDetailsUiState.value = movieDetailsUiState.value.copy(
                            snackBarItem = SnackbarItem(
                                show = true,
                                isError = false,
                                message = R.string.favorite_remove_success
                            ),
                        )
                    }
                }
                catch (e : IllegalStateException) {
                    movieDetailsUiState.value = movieDetailsUiState.value.copy(
                        snackBarItem = SnackbarItem(
                            show = true,
                            isError = true,
                            message = R.string.favorite_error
                        )
                    )
                }
            }
        }
    }

    fun resetSnackbar() {
        movieDetailsUiState.value = movieDetailsUiState.value.copy(
            snackBarItem = SnackbarItem(
                show = false
            )
        )
    }

    private fun getMovieDetails() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                movie.value = movieUseCase.getMovieById(movieId)
            }
        }
    }
}