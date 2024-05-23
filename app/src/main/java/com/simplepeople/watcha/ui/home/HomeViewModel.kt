package com.simplepeople.watcha.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.simplepeople.watcha.domain.usecase.MovieListUseCase
import com.simplepeople.watcha.ui.common.composables.NavigationBarItemSelection
import com.simplepeople.watcha.ui.common.composables.topbar.HomeFilterOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieListUseCase: MovieListUseCase
) : ViewModel() {

    private val _homeScreenUiState = MutableStateFlow(HomeScreenUiState())
    val homeScreenUiState = _homeScreenUiState.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch(
            context = Dispatchers.IO
        ) {
            try {
                _homeScreenUiState.value = homeScreenUiState.value.copy(
                    movieListState = HomeScreenMovieListState.Success(
                        movieList = movieListUseCase
                            .getMovies(homeScreenUiState.value.selectedHomeFilterOption)
                            .distinctUntilChanged()
                            .cachedIn(viewModelScope)
                    )
                )
            } catch (e: Exception) {
                _homeScreenUiState.value = homeScreenUiState.value.copy(
                    movieListState = HomeScreenMovieListState.Error(
                        message = com.simplepeople.watcha.R.string.movie_list_error
                    )
                )

            }
        }
    }

    fun updateTopBarSelection(filterOption: HomeFilterOptions) {
        _homeScreenUiState.value = homeScreenUiState.value.copy(
            selectedHomeFilterOption = filterOption,
            movieListState = HomeScreenMovieListState.Loading
        )
    }

    fun scrollingToTop(scrollToTopAction: Boolean) {
        _homeScreenUiState.value = homeScreenUiState.value.copy(
            scrollToTop = scrollToTopAction
        )
    }

    fun updateNavigationItemIndex(index: Int) {
        NavigationBarItemSelection.selectedNavigationItemIndex = index
    }

}