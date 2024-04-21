package com.simplepeople.watcha.ui.appscreen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.appscreen.common.MovieItem
import com.simplepeople.watcha.ui.appscreen.navigation.AppScreens
import com.simplepeople.watcha.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(FlowPreview::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
//    val movieList: List<Movie> by homeViewModel.movieList.collectAsState()
    val movieList: LazyPagingItems<Movie> = homeViewModel.movieList.collectAsLazyPagingItems()

    val lazyListState = rememberLazyListState()

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            !lazyListState.canScrollForward
        }
            .distinctUntilChanged()
            .debounce(500L)
            .collectLatest { cannotScrollForward ->
//                if (cannotScrollForward) homeViewModel.getNextPage()
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = lazyListState
    ) {
//        items(
//            items = movieList,
//            key = {
//                it.movieId
//            }
//        ) { movie ->
//            MovieItem(
//                movie,
//                navigateToMovieDetails = { navController.navigate(AppScreens.MovieDetailsScreen.buildArgRoute(movie.movieId))}
//            )
//        }
        items(
            count = movieList.itemCount
        ) { index ->
            val movie = movieList[index]!!
            MovieItem(
                movie,
                navigateToMovieDetails = { navController.navigate(AppScreens.MovieDetailsScreen.buildArgRoute(movie.movieId))}
            )
        }
    }
}