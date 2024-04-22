package com.simplepeople.watcha.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.usecase.FavoriteUseCase
import com.simplepeople.watcha.domain.usecase.MovieUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//TODO: uiState for success, error and loading screen state

@HiltViewModel(assistedFactory = MovieDetailsViewModel.MovieDetailsViewModelFactory::class)
class MovieDetailsViewModel @AssistedInject constructor(
    private val favoriteUseCase: FavoriteUseCase,
    private val movieUseCase: MovieUseCase,
    @Assisted private val movieId: Int
) : ViewModel() {

    @AssistedFactory
    interface MovieDetailsViewModelFactory {
        fun create(movieId: Int): MovieDetailsViewModel
    }

    var movie = MutableStateFlow(Movie())
        private set

    init {
        getMovieDetails()
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (!movie.value.isFavorite) {
                    favoriteUseCase.saveFavorite(movie.value)
                } else {
                    favoriteUseCase.deleteFavorite(movie.value)
                }
                movie.value = movie.value.copy(isFavorite = !movie.value.isFavorite)
            }
        }
    }

    private fun getMovieDetails() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val item = movieUseCase.getMovieById(movieId)
                movie.value = item
            }
        }
    }
}