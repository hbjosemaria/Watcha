package com.simplepeople.watcha.ui.search

import androidx.paging.PagingData
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.core.SearchLogItem
import kotlinx.coroutines.flow.Flow

data class SearchScreenState(
    val searchText: String = "",
    val searching: Boolean = false,
    val scrollToTop: Boolean = false,
    val searchLog: List<SearchLogItem> = listOf(),
    val movieListState: SearchScreenMovieListState = SearchScreenMovieListState.Loading
)

sealed class SearchScreenMovieListState {
    data object Loading : SearchScreenMovieListState()
    data class Success(val movieList: Flow<PagingData<Movie>>) : SearchScreenMovieListState()
    data class Error(val message: Int) : SearchScreenMovieListState()
}