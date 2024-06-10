package com.simplepeople.watcha.ui.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.common.composables.LoadingMovieDataImageDisplay
import com.simplepeople.watcha.ui.common.composables.LoadingMovieDataLoadingDisplay
import com.simplepeople.watcha.ui.common.composables.MovieList
import com.simplepeople.watcha.ui.common.composables.NavigationBarItemSelection
import com.simplepeople.watcha.ui.common.composables.SharedNavigationBar
import com.simplepeople.watcha.ui.common.composables.topbar.MainTopAppBar
import com.simplepeople.watcha.ui.common.composables.topbar.common.topBarDynamicParamCalc
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    lazyGridState: LazyGridState = rememberLazyGridState(),
    navigateToMovieDetails: (Long) -> Unit,
    navigateToNavigationBarItem: (String) -> Unit,
    navigateToSettings: () -> Unit,
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val favoriteScreenUiState by favoriteViewModel.favoriteScreenState.collectAsState()

    val topBarAlpha = remember {
        mutableFloatStateOf(
            topBarDynamicParamCalc(
                minValue = .5f,
                maxValue = .75f,
                fraction = scrollBehavior.state.overlappedFraction
            )
        )
    }

    LaunchedEffect(scrollBehavior.state.overlappedFraction) {
        snapshotFlow {
            scrollBehavior.state.overlappedFraction
        }
            .distinctUntilChanged()
            .collectLatest {
                topBarAlpha.floatValue = topBarDynamicParamCalc(
                    minValue = .5f,
                    maxValue = .75f,
                    fraction = scrollBehavior.state.overlappedFraction
                )
            }
    }

    LaunchedEffect(favoriteScreenUiState.scrollToTop) {
        if (favoriteScreenUiState.scrollToTop) {
            lazyGridState.scrollToItem(0)
            favoriteViewModel.scrollingToTop(false)
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        bottomBar = {
            SharedNavigationBar(
                navigateToNavigationBarItem = navigateToNavigationBarItem,
                selectedNavigationItemIndex = NavigationBarItemSelection.selectedNavigationItemIndex,
                updateNavigationBarSelectedIndex = { index ->
                    favoriteViewModel.updateNavigationItemIndex(index)
                },
                scrollToTopAction = {
                    favoriteViewModel.scrollingToTop(true)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
        ) {

            when (val state = favoriteScreenUiState.movieListState) {
                is FavoriteScreenMovieListState.Error -> {
                    LoadingMovieDataImageDisplay(
                        modifier = Modifier
                            .align(Alignment.Center),
                        image = R.drawable.movie_list_loading_error,
                        message = R.string.movie_list_error
                    )
                }

                is FavoriteScreenMovieListState.Loading -> {
                    LoadingMovieDataLoadingDisplay(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }

                is FavoriteScreenMovieListState.Success -> {
                    val movieList = state.movieList.collectAsLazyPagingItems()
                    when {
                        movieList.itemCount > 0 -> {
                            MovieList(
                                movieList = movieList,
                                navigateToMovieDetails = navigateToMovieDetails,
                                lazyGridState = lazyGridState,
                                paddingValues = PaddingValues(
                                    top = 98.dp,
                                    start = 10.dp,
                                    end = 10.dp,
                                    bottom = 10.dp
                                )
                            )
                        }

                        movieList.itemCount == 0 &&
                                movieList.loadState.source.append == LoadState.NotLoading(
                            endOfPaginationReached = true
                        ) -> {
                            LoadingMovieDataImageDisplay(
                                modifier = Modifier
                                    .align(Alignment.Center),
                                image = R.drawable.favorite_empty,
                                message = R.string.favorite_list_empty
                            )
                        }
                    }
                }
            }

            MainTopAppBar(
                scrollBehavior = scrollBehavior,
                topBarAlpha = topBarAlpha.floatValue,
                navigateToSettings = navigateToSettings
            )
        }
    }
}
