package com.simplepeople.watcha.ui.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.usecase.FavoriteUseCase
import com.simplepeople.watcha.domain.usecase.MovieUseCase
import com.simplepeople.watcha.ui.common.utils.SnackbarItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = MovieDetailsViewModel.MovieDetailsViewModelFactory::class)
class MovieDetailsViewModel @AssistedInject constructor(
    private val favoriteUseCase: FavoriteUseCase,
    private val movieUseCase: MovieUseCase,
    @Assisted private val movieId: Long
) : ViewModel() {

    @AssistedFactory
    interface MovieDetailsViewModelFactory {
        fun create(movieId: Long): MovieDetailsViewModel
    }

    private val _movieDetailsState = MutableStateFlow(MovieDetailsState())
    val movieDetailsState = _movieDetailsState.asStateFlow()

    init {
        getMovieDetails()
    }

    fun toggleFavorite() {
        viewModelScope.launch(
            context = Dispatchers.IO
        ) {
            when (val state = movieDetailsState.value.movieState) {
                is MovieDetailsMovieState.Error,
                MovieDetailsMovieState.Loading -> {
                    //Do nothing
                }

                is MovieDetailsMovieState.Success -> {

                    val movie = state.movie.copy(
                        isFavorite = !state.movie.isFavorite
                    )

                    _movieDetailsState.value = _movieDetailsState.value.copy(
                        movieState = MovieDetailsMovieState.Success(
                            movie = movie
                        )
                    )

                    try {
                        if (movie.isFavorite) {
                            favoriteUseCase.saveFavorite(movie)
                            _movieDetailsState.value = _movieDetailsState.value.copy(
                                snackBarItem = SnackbarItem(
                                    show = true,
                                    isError = false,
                                    message = R.string.favorite_add_success
                                ),
                            )
                        } else {
                            favoriteUseCase.deleteFavorite(movie.movieId)
                            _movieDetailsState.value = _movieDetailsState.value.copy(
                                snackBarItem = SnackbarItem(
                                    show = true,
                                    isError = false,
                                    message = R.string.favorite_remove_success
                                ),
                            )
                        }
                    } catch (e: IllegalStateException) {
                        _movieDetailsState.value = _movieDetailsState.value.copy(
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
    }

    fun resetSnackbar() {
        _movieDetailsState.value = _movieDetailsState.value.copy(
            snackBarItem = SnackbarItem(
                show = false
            )
        )
    }

    private fun getMovieDetails() {
        viewModelScope.launch(
            context = Dispatchers.IO
        ) {
            try {
                _movieDetailsState.value = _movieDetailsState.value.copy(
                    movieState = MovieDetailsMovieState.Success(
                        movie = movieUseCase.getMovieById(movieId)
                    )
                )
            } catch (e: Exception) {
                _movieDetailsState.value = _movieDetailsState.value.copy(
                    movieState = MovieDetailsMovieState.Error(
                        message = R.string.movie_list_error
                    )
                )
            }
        }
    }
}