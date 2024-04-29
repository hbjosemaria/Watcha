package com.simplepeople.watcha.ui.appscreen.home

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.appscreen.common.composables.MovieList
import com.simplepeople.watcha.ui.viewmodel.FavoriteViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FavoriteScreen(
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    lazyGridState: LazyGridState = rememberLazyGridState(),
    navigateToMovieDetails: (Long) -> Unit
) {

    val movieList: LazyPagingItems<Movie> = favoriteViewModel.movieList.collectAsLazyPagingItems()
    val scrollToTop by favoriteViewModel.scrollToTop

    LaunchedEffect (scrollToTop) {
        snapshotFlow {
            scrollToTop
        }
            .collectLatest {scrollingToTop ->
                if (scrollingToTop) {
                    lazyGridState.animateScrollToItem(0)
                    favoriteViewModel.resetScrollToTop()
                }
            }

    }

    MovieList (
        movieList = movieList,
        navigateToMovieDetails = navigateToMovieDetails,
        lazyGridState = lazyGridState
    )

}
