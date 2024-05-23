package com.simplepeople.watcha.ui.favorite

import androidx.paging.PagingData
import com.simplepeople.watcha.domain.core.Movie
import kotlinx.coroutines.flow.Flow

data class FavoriteScreenUiState(
    val scrollToTop: Boolean = false,
    val movieListState: FavoriteScreenMovieListState = FavoriteScreenMovieListState.Loading
)

sealed class FavoriteScreenMovieListState {
    data object Loading : FavoriteScreenMovieListState()
    data class Success(val movieList: Flow<PagingData<Movie>>) : FavoriteScreenMovieListState()
    data class Error(val message: Int) : FavoriteScreenMovieListState()
}