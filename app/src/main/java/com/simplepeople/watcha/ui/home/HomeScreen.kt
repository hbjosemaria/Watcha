package com.simplepeople.watcha.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.common.composables.HomeMovieList
import com.simplepeople.watcha.ui.common.composables.LoadingMovieDataImageDisplay
import com.simplepeople.watcha.ui.common.composables.LoadingMovieDataLoadingDisplay
import com.simplepeople.watcha.ui.common.composables.MovieListPlaceholder
import com.simplepeople.watcha.ui.common.composables.NavigationBarItemSelection
import com.simplepeople.watcha.ui.common.composables.SharedNavigationBar
import com.simplepeople.watcha.ui.common.composables.topbar.HomeFilterOptions
import com.simplepeople.watcha.ui.common.composables.topbar.HomeTopAppBar
import com.simplepeople.watcha.ui.common.composables.topbar.common.topBarDynamicParamCalc
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    lazyGridState: LazyGridState = rememberLazyGridState(),
    navigateToMovieDetails: (Long) -> Unit,
    navigateToNavigationBarItem: (String) -> Unit,
    navigateToSettings: () -> Unit,
    isRefreshing: Boolean
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val homeScreenUiState by homeViewModel.homeScreenState.collectAsState()
    val userHasConnection by homeViewModel.connectivityState.collectAsState()
    var refreshMovieList by remember{ mutableStateOf(isRefreshing) }

    val movieListPaddingValues = remember {
        mutableStateOf(
            topBarDynamicParamCalc(
                minValue = 78.dp,
                maxValue = 136.dp,
                fraction = scrollBehavior.state.collapsedFraction
            )
        )
    }

    val topBarAlpha = remember {
        mutableFloatStateOf(
            topBarDynamicParamCalc(
                minValue = .5f,
                maxValue = 1f,
                fraction = scrollBehavior.state.collapsedFraction
            )
        )
    }

    LaunchedEffect(scrollBehavior.state.collapsedFraction) {
        snapshotFlow {
            scrollBehavior.state.collapsedFraction
        }
            .distinctUntilChanged()
            .collectLatest {
                movieListPaddingValues.value = topBarDynamicParamCalc(
                    minValue = 78.dp,
                    maxValue = 136.dp,
                    fraction = scrollBehavior.state.collapsedFraction
                )

                topBarAlpha.floatValue = topBarDynamicParamCalc(
                    minValue = .5f,
                    maxValue = 1f,
                    fraction = scrollBehavior.state.collapsedFraction
                )
            }
    }

    LaunchedEffect(homeScreenUiState.scrollToTop) {
        if (homeScreenUiState.scrollToTop) {
            lazyGridState.scrollToItem(0)
            homeViewModel.scrollingToTop(false)
        }
    }

    LaunchedEffect(refreshMovieList) {
        if (refreshMovieList) {
            homeViewModel.reloadMovies()
            refreshMovieList = false
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        bottomBar = {
            SharedNavigationBar(
                navigateToNavigationBarItem = navigateToNavigationBarItem,
                selectedNavigationItemIndex = NavigationBarItemSelection.selectedNavigationItemIndex,
                updateNavigationBarSelectedIndex = { index ->
                    homeViewModel.updateNavigationItemIndex(index)
                },
                scrollToTopAction = {
                    homeViewModel.scrollingToTop(true)
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
            //TODO: add retry button to each error display
            when (val state = homeScreenUiState.movieListState) {
                is HomeScreenMovieListState.Loading -> {
                    MovieListPlaceholder(
                        isHomeScreen = true,
                        paddingValues = PaddingValues(
                            top = movieListPaddingValues.value,
                            start = 10.dp,
                            end = 10.dp,
                            bottom = 10.dp
                        )
                    )
                }

                is HomeScreenMovieListState.Error -> {
                    LoadingMovieDataImageDisplay(
                        modifier = Modifier
                            .align(Alignment.Center),
                        image = R.drawable.movie_list_loading_error,
                        message = state.message
                    )
                }

                is HomeScreenMovieListState.Success -> {
                    val movieList = state.movieList.collectAsLazyPagingItems()

                    when {
                        movieList.itemSnapshotList.isEmpty() && (
                                movieList.loadState.refresh == LoadState.Loading || movieList.loadState.source.append == LoadState.Loading) -> {
                            LoadingMovieDataLoadingDisplay(
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                        }

                        movieList.loadState.refresh is LoadState.Error ||
                                (movieList.loadState.source.append == LoadState.NotLoading(true) && movieList.itemSnapshotList.isEmpty()) -> {
                            LoadingMovieDataImageDisplay(
                                modifier = Modifier
                                    .align(Alignment.Center),
                                image = R.drawable.movie_list_loading_error,
                                message = R.string.movie_list_connection_lost
                            )

                            if (userHasConnection) {
                                TextButton(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(
                                            bottom = 30.dp
                                        ),
                                    onClick = {
                                        homeViewModel.reloadMovies()
                                    },
                                    shape = ButtonDefaults.filledTonalShape
                                ) {
                                    Text(
                                        stringResource(id = R.string.retry)
                                    )
                                }
                            }
                        }

                        else -> {
                            Column() {
                                HomeMovieList(
                                    movieList = movieList,
                                    navigateToMovieDetails = navigateToMovieDetails,
                                    lazyGridState = lazyGridState,
                                    paddingValues = PaddingValues(
                                        top = movieListPaddingValues.value,
                                        start = 10.dp,
                                        end = 10.dp,
                                        bottom = 10.dp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            HomeTopAppBar(
                selectedHomeFilterOption = homeScreenUiState.selectedHomeFilterOption,
                filterNowPlaying = {
                    homeViewModel.updateTopBarSelection(HomeFilterOptions.NowPlaying)
                },
                filterPopular = {
                    homeViewModel.updateTopBarSelection(HomeFilterOptions.Popular)
                },
                filterTopRated = {
                    homeViewModel.updateTopBarSelection(HomeFilterOptions.TopRated)
                },
                filterUpcoming = {
                    homeViewModel.updateTopBarSelection(HomeFilterOptions.Upcoming)
                },
                loadMovies = {
                    homeViewModel.loadMovies()
                },
                scrollToTop = {
                    homeViewModel.scrollingToTop(true)
                },
                scrollBehavior = scrollBehavior,
                topBarAlpha = topBarAlpha.floatValue,
                navigateToSettings = navigateToSettings
            )
        }
    }
}