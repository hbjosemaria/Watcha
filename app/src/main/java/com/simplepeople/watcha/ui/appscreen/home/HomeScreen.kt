package com.simplepeople.watcha.ui.appscreen.home

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.appscreen.common.composables.MovieList
import com.simplepeople.watcha.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen (
    homeViewModel: HomeViewModel = hiltViewModel(),
    lazyGridState: LazyGridState = rememberLazyGridState(),
    navigateToMovieDetails: (Long) -> Unit
) {
    //TODO: Create an UIState class for movie and api call status
    val movieList: LazyPagingItems<Movie> = homeViewModel.movieList.collectAsLazyPagingItems()
    val scrollToTop by homeViewModel.scrollToTop

    LaunchedEffect (scrollToTop) {
        if (scrollToTop) {
            lazyGridState.animateScrollToItem(0)
            homeViewModel.resetScrollToTop()
        }
    }

    MovieList (
        movieList = movieList,
        navigateToMovieDetails = navigateToMovieDetails,
        lazyGridState = lazyGridState
    )
}