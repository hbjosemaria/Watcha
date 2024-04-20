package com.simplepeople.watcha.ui.appscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.appscreen.common.MovieAvatar
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
    val movieSet: Set<Movie> by homeViewModel.movieSet.collectAsState()

    val lazyListState = rememberLazyListState()

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            !lazyListState.canScrollForward
        }
            .distinctUntilChanged()
            .debounce(500L)
            .collectLatest { cannotScrollForward ->
                if (cannotScrollForward) homeViewModel.getNextPage()
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = lazyListState
    ) {
        items(movieSet.toList()) { movie ->
            MovieAvatar(
                movie,
                navigateToMovieDetails = { navController.navigate(AppScreens.MovieDetailsScreen.route + "/${movie.movieId}") })
        }
    }

}