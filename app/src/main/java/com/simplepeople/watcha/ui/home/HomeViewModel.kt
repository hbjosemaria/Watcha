package com.simplepeople.watcha.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.simplepeople.watcha.domain.usecase.CacheUseCase
import com.simplepeople.watcha.domain.usecase.MovieListUseCase
import com.simplepeople.watcha.ui.common.composables.NavigationBarItemSelection
import com.simplepeople.watcha.ui.common.composables.topbar.HomeFilterOptions
import com.simplepeople.watcha.ui.common.utils.ConnectivityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieListUseCase: MovieListUseCase,
    private val cacheUseCase: CacheUseCase,
    private val _connectivityState: ConnectivityState
) : ViewModel() {

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()
    val connectivityState = _connectivityState.connectivityStateFlow

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch(
            context = Dispatchers.IO
        ) {
            try {
                _homeScreenState.value = homeScreenState.value.copy(
                    movieListState = HomeScreenMovieListState.Success(
                        movieList = movieListUseCase
                            .getMovies(homeScreenState.value.selectedHomeFilterOption)
                            .distinctUntilChanged()
                            .cachedIn(viewModelScope)
                    )
                )
            } catch (e: Exception) {
                _homeScreenState.value = homeScreenState.value.copy(
                    movieListState = HomeScreenMovieListState.Error(
                        message = com.simplepeople.watcha.R.string.movie_list_error
                    )
                )

            }
        }
    }

    fun updateTopBarSelection(filterOption: HomeFilterOptions) {
        _homeScreenState.value = homeScreenState.value.copy(
            selectedHomeFilterOption = filterOption,
            movieListState = HomeScreenMovieListState.Loading
        )
    }

    fun scrollingToTop(scrollToTopAction: Boolean) {
        _homeScreenState.value = homeScreenState.value.copy(
            scrollToTop = scrollToTopAction
        )
    }

    fun updateNavigationItemIndex(index: Int) {
        NavigationBarItemSelection.selectedNavigationItemIndex = index
    }

    fun reloadMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            cacheUseCase.forceCacheExpiration()
            cleanMovieList()
            loadMovies()
        }
    }

    fun cleanMovieList() {
        _homeScreenState.value = _homeScreenState.value.copy(
            movieListState = HomeScreenMovieListState.Success(
                movieList = emptyFlow()
            )
        )
    }

}