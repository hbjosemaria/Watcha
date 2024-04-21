package com.simplepeople.watcha.ui.appscreen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.appscreen.common.MovieItem
import com.simplepeople.watcha.ui.appscreen.navigation.AppScreens
import com.simplepeople.watcha.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val movieList: LazyPagingItems<Movie> = homeViewModel.movieList.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(
            count = movieList.itemCount
        ) { index ->
            val movie = movieList[index]!!
            MovieItem(
                movie,
                navigateToMovieDetails = {
                    navController.navigate(AppScreens.MovieDetailsScreen.buildArgRoute(movie.movieId))
                }
            )
        }
    }
}