package com.simplepeople.watcha.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.core.SearchLogItem
import com.simplepeople.watcha.ui.common.composables.DefaultIconButton
import com.simplepeople.watcha.ui.common.composables.MovieList
import com.simplepeople.watcha.ui.navigation.topbar.SearchTopAppBar
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navigateToMovieDetails: (Long) -> Unit,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    searchViewModel: SearchViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {

    val movieList: LazyPagingItems<Movie> = searchViewModel.movieList.collectAsLazyPagingItems()
    val searchLog: LazyPagingItems<SearchLogItem> =
        searchViewModel.searchLog.collectAsLazyPagingItems()
    val searchScreenUiState by searchViewModel.searchScreenUiState
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(searchScreenUiState.searchText) {
        snapshotFlow {
            searchScreenUiState.searchText
        }
            .distinctUntilChanged()
            .debounce(500L)
            .collectLatest { searchText ->
                if (searchText.isNotBlank()) {
                    searchViewModel.getMoviesByTitle(searchText)
                    if (searchLog.itemSnapshotList.items.find { it.searchedText.lowercase() == searchText.lowercase() } == null) {
                        searchViewModel.addNewSearch(searchText)
                    }
                } else {
                    searchViewModel.cleanMovieSearch()
                }
            }
    }

    LaunchedEffect(searchScreenUiState.scrollToTop) {
        snapshotFlow {
            searchScreenUiState.scrollToTop
        }
            .collectLatest { scrollingToTop ->
                if (scrollingToTop) {
                    lazyGridState.animateScrollToItem(0)
                }
            }

    }

    Scaffold(
        topBar = {
            SearchTopAppBar(
                navigateBack = navigateBack,
                scrollBehavior = scrollBehavior,
                searchText = searchScreenUiState.searchText,
                onValueChange = { newText: String ->
                    searchViewModel.isSearching()
                    searchViewModel.updateSearchText(newText)
                },
                cleanSearch = {
                    searchViewModel.cleanMovieSearch()
                    searchViewModel.cleanSearchText()
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding( innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            //TODO: create a recent search list and display it when user touches the search box
            // Also, if user touches one of the list item, add the text to the search bar and execute the query to fetch into the movie list
            // And if user touches the X in the end of the item, clear that register

            if (searchScreenUiState.searchText.isBlank()) {
                LazyColumn(
//                    contentPadding = WindowInsets.safeContent.asPaddingValues(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (searchLog.itemCount > 0) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                TextButton(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .wrapContentSize(),
                                    onClick = {
                                        searchViewModel.cleanSearchLog()
                                    },
                                ) {
                                    Text(text = stringResource(id = R.string.search_clean_log))
                                    //TODO: get a proper icon for this
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = Icons.Filled.Delete.name
                                    )
                                }
                            }
                        }
                    }

                    items(
                        count = searchLog.itemCount,
                        key = searchLog.itemKey { it.searchedText },
                    ) { index ->
                        val searchLogItem = searchLog[index]!!
                        SearchLogItem(
                            searchText = searchLogItem.searchedText,
                            applySearch = {
                                searchViewModel.isSearching()
                                searchViewModel.updateSearchText(searchLogItem.searchedText)
                            },
                            removeSearch = {
                                searchViewModel.removeSearch(searchLogItem)
                            }
                        )
                    }
                }
            }

            MovieList(
                movieList = movieList,
                navigateToMovieDetails = navigateToMovieDetails,
                paddingValues = PaddingValues(10.dp)
            )
        }
    }
}

@Composable
fun SearchLogItem(
    searchText: String,
    applySearch: () -> Unit,
    removeSearch: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                end = 12.dp,
                start = 12.dp
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(18f)
                .clickable {
                    applySearch()
                },
            text = searchText,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 18.sp
            )
        )
        DefaultIconButton(
            action = removeSearch,
            iconImage = Icons.Default.Clear,
            contentDescription = Icons.Default.Clear.name,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(24.dp)
                .weight(2f)
        )

    }
}