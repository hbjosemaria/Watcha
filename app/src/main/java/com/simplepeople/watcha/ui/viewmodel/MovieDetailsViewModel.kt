package com.simplepeople.watcha.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.simplepeople.watcha.MainActivity
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.core.exampleMovieSet
import com.simplepeople.watcha.domain.repo.ExternalMovieRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO: uiState for success, error and loading screen state

@HiltViewModel(assistedFactory = MovieDetailsViewModel.MovieDetailsViewModelFactory::class)
class MovieDetailsViewModel @AssistedInject constructor(
    private val movieRepository: ExternalMovieRepository,
    @Assisted private val movieId: Int
) : ViewModel() {

    @AssistedFactory
    interface MovieDetailsViewModelFactory {
        fun create(movieId: Int) : MovieDetailsViewModel
    }

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
            movie.value = movieRepository.getMovieById(movieId)
        }
    }
}
