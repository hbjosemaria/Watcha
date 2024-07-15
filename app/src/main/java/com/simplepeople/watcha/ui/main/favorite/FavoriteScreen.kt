package com.simplepeople.watcha.ui.main.favorite

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.common.composables.ImageWithMessage
import com.simplepeople.watcha.ui.common.composables.LoadingIndicator
import com.simplepeople.watcha.ui.common.composables.SharedNavigationBar
import com.simplepeople.watcha.ui.common.composables.movielist.MovieList
import com.simplepeople.watcha.ui.common.composables.topbar.MainTopAppBar
import com.simplepeople.watcha.ui.navigation.UserProfileResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    lazyGridState: LazyGridState = rememberLazyGridState(),
    navigateToMovieDetails: (Long) -> Unit,
    navigateToNavigationBarItem: (String) -> Unit,
    userProfileResult: UserProfileResult,
    updateNavigationItemIndex: (Int) -> Unit,
    selectedNavigationItemIndex: Int,
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val favoriteScreenUiState by favoriteViewModel.favoriteScreenState.collectAsState()

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
                selectedNavigationItemIndex = selectedNavigationItemIndex,
                updateNavigationBarSelectedIndex = updateNavigationItemIndex,
                scrollToTopAction = {
                    favoriteViewModel.scrollingToTop(true)
                },
                avatarUrl = if (userProfileResult is UserProfileResult.Success) {
                    userProfileResult.userProfile.getUserImageUrl()
                } else ""
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
                    ImageWithMessage(
                        modifier = Modifier
                            .align(Alignment.Center),
                        image = R.drawable.movie_list_loading_error,
                        message = R.string.movie_list_error
                    )
                }

                is FavoriteScreenMovieListState.Loading -> {
                    LoadingIndicator(
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
                            ImageWithMessage(
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
                scrollBehavior = scrollBehavior
            )
        }
    }
}
