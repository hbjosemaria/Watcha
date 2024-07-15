package com.simplepeople.watcha.ui.main.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.usecase.FavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteUseCase: FavoriteUseCase,
) : ViewModel() {

    private val _favoriteScreenState = MutableStateFlow(FavoriteScreenState())
    val favoriteScreenState = _favoriteScreenState.asStateFlow()

    init {
        getFavorites()
    }

    private fun getFavorites() {
        viewModelScope.launch(
            context = Dispatchers.IO
        ) {
            try {
                _favoriteScreenState.value = favoriteScreenState.value.copy(
                    movieListState = FavoriteScreenMovieListState.Success(
                        movieList = favoriteUseCase
                            .getFavorites()
                            .cachedIn(viewModelScope)
                    )
                )
            } catch (e: Exception) {
                _favoriteScreenState.value = favoriteScreenState.value.copy(
                    movieListState = FavoriteScreenMovieListState.Error(R.string.movie_list_error)
                )
            }

        }
    }

    fun scrollingToTop(scrollToTopAction: Boolean) {
        _favoriteScreenState.value = favoriteScreenState.value.copy(
            scrollToTop = scrollToTopAction
        )
    }

}