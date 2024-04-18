package com.simplepeople.watcha.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.usecase.GetMovieUseCase
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
    //private val favoriteUseCase: FavoriteUseCase, //TODO: Favorite Use Case and CRUD on Room
    private val getMovieUseCase: GetMovieUseCase,
    @Assisted private val movieId: Int
) : ViewModel() {

    @AssistedFactory
    interface MovieDetailsViewModelFactory {
        fun create(movieId: Int): MovieDetailsViewModel
    }

    var movie = MutableStateFlow(Movie())
        private set

    init {
        getMovieDetails(movieId)
    }

    fun toggleFavorite() {
        movie.value = movie.value.copy(isFavorite = !movie.value.isFavorite)
        if (movie.value.isFavorite) {
            //TODO: save movie into local database list of favorites
        } else {
            //TODO: remove movie from local database list of favorites
        }
    }

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            movie.value = withContext(Dispatchers.IO) {
                getMovieUseCase.getMovieById(movieId)
            }
        }
    }
}
