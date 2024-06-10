package com.simplepeople.watcha.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.usecase.FavoriteUseCase
import com.simplepeople.watcha.ui.common.composables.NavigationBarItemSelection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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
            _favoriteScreenState.value = favoriteScreenState.value.copy(
                movieListState = FavoriteScreenMovieListState.Success(
                    movieList = favoriteUseCase
                        .getFavorites()
                        .catch {
                            _favoriteScreenState.value = favoriteScreenState.value.copy(
                                movieListState = FavoriteScreenMovieListState.Error(R.string.movie_list_error)
                            )
                        }
                        .cachedIn(viewModelScope)
                )
            )
        }
    }

    fun scrollingToTop(scrollToTopAction: Boolean) {
        _favoriteScreenState.value = favoriteScreenState.value.copy(
            scrollToTop = scrollToTopAction
        )
    }

    fun updateNavigationItemIndex(index: Int) {
        NavigationBarItemSelection.selectedNavigationItemIndex = index
    }

}