package com.simplepeople.watcha.ui.appscreen.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.appscreen.common.composables.MovieList
import com.simplepeople.watcha.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(FlowPreview::class)
@Composable
fun SearchScreen(
    navigateToMovieDetails: (Long) -> Unit,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    searchViewModel: SearchViewModel = hiltViewModel()
) {

    val movieList : LazyPagingItems<Movie> = searchViewModel.movieList.collectAsLazyPagingItems()
    val searchScreenUiState by searchViewModel.searchScreenUiState

    LaunchedEffect(searchScreenUiState.searchText) {
        snapshotFlow {
            searchScreenUiState.searchText
        }
            .distinctUntilChanged()
            .debounce(500L)
            .collectLatest { searchText ->
                if (searchText.isNotBlank()) {
                    searchViewModel.getMoviesByTitle(searchText)

                } else {
                    searchViewModel.cleanMovieSearch()
                }
            }
    }

    LaunchedEffect (searchScreenUiState.scrollToTop) {
        snapshotFlow {
            searchScreenUiState.scrollToTop
        }
            .collectLatest {scrollingToTop ->
                if (scrollingToTop) {
                    lazyGridState.animateScrollToItem(0)
                }
            }

    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextField(
            value = searchScreenUiState.searchText,
            onValueChange = {newText ->
                searchViewModel.isSearching()
                searchViewModel.updateSearchText(newText)
            },
            placeholder = { Text(stringResource(R.string.search_placeholder)) },
            maxLines = 1,
            label = { Text(stringResource(R.string.search_label)) },
            modifier = Modifier
                .fillMaxWidth(),
            leadingIcon = {
                Icon (
                    imageVector = Icons.Default.Search,
                    contentDescription = Icons.Default.Search.name
                )
            },
            trailingIcon = {
                if (searchScreenUiState.searching) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
        )
        MovieList (
            movieList = movieList,
            navigateToMovieDetails = navigateToMovieDetails
        )
    }
}