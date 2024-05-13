package com.simplepeople.watcha.ui.home

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
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.common.composables.HomeMovieList
import com.simplepeople.watcha.ui.navigation.NavigationBarItemSelection
import com.simplepeople.watcha.ui.navigation.SharedNavigationBar
import com.simplepeople.watcha.ui.navigation.topbar.HomeTopAppBar
import com.simplepeople.watcha.ui.navigation.topbar.common.TopBarDynamicParamCalc
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    lazyGridState: LazyGridState = rememberLazyGridState(),
    navigateToMovieDetails: (Long) -> Unit,
    navigateToNavigationBarItem: (String) -> Unit,
    navigateToSearchScreen: () -> Unit
) {

    //TODO: Create an UIState class for movie and api call status
    val movieList: LazyPagingItems<Movie> = homeViewModel.movieList.collectAsLazyPagingItems()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val homeScreenUiState by homeViewModel.homeScreenUiState

    val movieListPaddingValues = remember {
        mutableStateOf(
            TopBarDynamicParamCalc(
                minValue = 78.dp,
                maxValue = 136.dp,
                fraction = scrollBehavior.state.collapsedFraction
            )
        )
    }

    val topBarAlpha = remember {
        mutableStateOf(
            TopBarDynamicParamCalc(
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
            .collect {
                movieListPaddingValues.value = TopBarDynamicParamCalc(
                    minValue = 78.dp,
                    maxValue = 146.dp,
                    fraction = scrollBehavior.state.collapsedFraction
                )

                topBarAlpha.value = TopBarDynamicParamCalc(
                    minValue = .5f,
                    maxValue = 1f,
                    fraction = scrollBehavior.state.collapsedFraction
                )
            }
    }

    LaunchedEffect(homeScreenUiState.scrollToTop) {
        if (homeScreenUiState.scrollToTop) {
            lazyGridState.animateScrollToItem(0)
            homeViewModel.scrollingToTop(false)
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
            HomeTopAppBar(
                selectedHomeFilterOption = homeScreenUiState.selectedHomeFilterOption,
                navigateToSearchScreen = navigateToSearchScreen,
                filterNowPlaying = {
                    homeViewModel.updateTopBarSelection(HomeFilterOptions.NowPlaying)
                    homeViewModel.loadMovies(HomeFilterOptions.NowPlaying)
                },
                filterPopular = {
                    homeViewModel.updateTopBarSelection(HomeFilterOptions.Popular)
                    homeViewModel.loadMovies(HomeFilterOptions.Popular)
                },
                filterTopRated = {
                    homeViewModel.updateTopBarSelection(HomeFilterOptions.TopRated)
                    homeViewModel.loadMovies(HomeFilterOptions.TopRated)
                },
                filterUpcoming = {
                    homeViewModel.updateTopBarSelection(HomeFilterOptions.Upcoming)
                    homeViewModel.loadMovies(HomeFilterOptions.Upcoming)
                },
                scrollBehavior = scrollBehavior,
                topBarAlpha = topBarAlpha.value
            )
        }
    }
}