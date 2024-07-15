package com.simplepeople.watcha.ui.main.search

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.common.composables.DefaultIconButton
import com.simplepeople.watcha.ui.common.composables.ImageWithMessage
import com.simplepeople.watcha.ui.common.composables.LoadingIndicator
import com.simplepeople.watcha.ui.common.composables.SharedNavigationBar
import com.simplepeople.watcha.ui.common.composables.movielist.MovieList
import com.simplepeople.watcha.ui.common.composables.topbar.SearchTopAppBar
import com.simplepeople.watcha.ui.navigation.UserProfileResult
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navigateToNavigationBarItem: (String) -> Unit,
    navigateToMovieDetails: (Long) -> Unit,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    searchViewModel: SearchViewModel = hiltViewModel(),
    userProfileResult: UserProfileResult,
    updateNavigationItemIndex: (Int) -> Unit,
    selectedNavigationItemIndex: Int,
) {

    val searchScreenUiState by searchViewModel.searchScreenState.collectAsState()
    val searchLog = searchScreenUiState.searchLog
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
                    if (searchLog.find { it.searchedText.lowercase() == searchText.lowercase() } == null) {
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

    DisposableEffect(true) {
        onDispose {
            searchViewModel.cleanMovieSearch()
            searchViewModel.cleanSearchText()
        }
    }

    Scaffold(
        topBar = {
            SearchTopAppBar(
                scrollBehavior = scrollBehavior,
                searchText = searchScreenUiState.searchText,
                onValueChange = { newText: String ->
                    searchViewModel.isSearching(newText)
                },
                cleanSearch = {
                    searchViewModel.cleanMovieSearch()
                    searchViewModel.cleanSearchText()
                }
            )
        },
        bottomBar = {
            SharedNavigationBar(
                navigateToNavigationBarItem = navigateToNavigationBarItem,
                selectedNavigationItemIndex = selectedNavigationItemIndex,
                updateNavigationBarSelectedIndex = updateNavigationItemIndex,
                avatarUrl = if (userProfileResult is UserProfileResult.Success) {
                    userProfileResult.userProfile.getUserImageUrl()
                } else ""
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            if (searchScreenUiState.searchText.isBlank()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (searchLog.isNotEmpty()) {
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
                                }
                            }
                        }
                    }

                    items(
                        items = searchLog,
                        key = { searchLogItem ->
                            searchLogItem.id
                        }
                    ) { searchLogItem ->
                        SearchLogItem(
                            searchText = searchLogItem.searchedText,
                            applySearch = {
                                searchViewModel.isSearching(searchLogItem.searchedText)
                            },
                            removeSearch = {
                                searchViewModel.removeSearch(searchLogItem)
                            }
                        )
                    }
                }
            }

            when (val state = searchScreenUiState.movieListState) {

                is SearchScreenMovieListState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        LoadingIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }

                is SearchScreenMovieListState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        ImageWithMessage(
                            modifier = Modifier
                                .align(Alignment.Center),
                            image = R.drawable.favorite_empty,
                            message = state.message
                        )
                    }
                }

                is SearchScreenMovieListState.Success -> {
                    val movieList = state.movieList.collectAsLazyPagingItems()
                    when {
                        movieList.itemCount > 0 -> {
                            MovieList(
                                movieList = movieList,
                                navigateToMovieDetails = navigateToMovieDetails,
                                paddingValues = PaddingValues(10.dp)
                            )
                        }

                        movieList.itemCount == 0 &&
                                movieList.loadState.refresh is LoadState.Error -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                ImageWithMessage(
                                    modifier = Modifier
                                        .align(Alignment.Center),
                                    image = R.drawable.search_no_result,
                                    message = R.string.search_no_result
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchLogItem(
    searchText: String,
    applySearch: () -> Unit,
    removeSearch: () -> Unit,
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