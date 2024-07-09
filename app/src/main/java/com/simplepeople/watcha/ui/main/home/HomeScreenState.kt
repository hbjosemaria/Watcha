package com.simplepeople.watcha.ui.main.home

import androidx.paging.PagingData
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.common.composables.topbar.HomeFilterOptions
import kotlinx.coroutines.flow.Flow

data class HomeScreenState(
    val scrollToTop: Boolean = false,
    val selectedHomeFilterOption: HomeFilterOptions = HomeFilterOptions.NowPlaying,
    val movieListState: HomeScreenMovieListState = HomeScreenMovieListState.Loading,
)

sealed class HomeScreenMovieListState {
    data object Loading : HomeScreenMovieListState()
    data class Success(val movieList: Flow<PagingData<Movie>>) : HomeScreenMovieListState()
    data class Error(val message: Int) : HomeScreenMovieListState()
}