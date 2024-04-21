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
import com.simplepeople.watcha.ui.viewmodel.FavoriteViewModel

@Composable
fun FavoriteScreen(
    navController: NavController,
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {

    val movieList: LazyPagingItems<Movie> = favoriteViewModel.movieList.collectAsLazyPagingItems()

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
