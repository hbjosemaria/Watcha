package com.simplepeople.watcha.ui.appscreen.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.appscreen.common.MovieList
import com.simplepeople.watcha.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(FlowPreview::class)
@Composable
fun SearchScreen(
    navigateToMovieDetails: (Int) -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel()
) {

    val movieList : LazyPagingItems<Movie> = searchViewModel.movieList.collectAsLazyPagingItems()
    var searchText by searchViewModel.searchText

    LaunchedEffect(searchText) {
        snapshotFlow {
            searchText
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextField(
            value = searchText,
            onValueChange = {newText ->
                searchViewModel.updateSearchText(newText)
            },
            placeholder = { Text(stringResource(R.string.search_placeholder)) },
            maxLines = 1,
            label = { Text(stringResource(R.string.search_label)) },
            modifier = Modifier
                .fillMaxWidth()
        )
        //TODO: scroll back to top automatically when new movie data is fetched
        MovieList (
            movieList = movieList,
            navigateToMovieDetails = navigateToMovieDetails
        )
    }
}