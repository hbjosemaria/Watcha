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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.common.composables.MovieList
import com.simplepeople.watcha.ui.navigation.NavigationBarItemSelection
import com.simplepeople.watcha.ui.navigation.SharedNavigationBar
import com.simplepeople.watcha.ui.navigation.topbar.MainTopAppBar
import com.simplepeople.watcha.ui.navigation.topbar.common.TopBarDynamicParamCalc
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    lazyGridState: LazyGridState = rememberLazyGridState(),
    navigateToMovieDetails: (Long) -> Unit,
    navigateToNavigationBarItem: (String) -> Unit,
    navigateToSearchScreen: () -> Unit
) {

    val movieList: LazyPagingItems<Movie> = favoriteViewModel.movieList.collectAsLazyPagingItems()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val favoriteScreenUiState by favoriteViewModel.favoriteScreenUiState

    val topBarAlpha = remember {
        mutableFloatStateOf(
            TopBarDynamicParamCalc(
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
            .collect {
                topBarAlpha.floatValue = TopBarDynamicParamCalc(
                    minValue = .5f,
                    maxValue = .75f,
                    fraction = scrollBehavior.state.overlappedFraction
                )
            }
    }

    LaunchedEffect(favoriteScreenUiState.scrollToTop) {
        if (favoriteScreenUiState.scrollToTop) {
            lazyGridState.animateScrollToItem(0)
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
            MainTopAppBar(
                navigateToSearchScreen = navigateToSearchScreen,
                scrollBehavior = scrollBehavior,
                topBarAlpha = topBarAlpha.floatValue
            )
        }
    }
}

