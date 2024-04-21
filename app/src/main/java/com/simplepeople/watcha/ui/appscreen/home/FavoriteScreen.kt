package com.simplepeople.watcha.ui.appscreen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.simplepeople.watcha.ui.appscreen.common.MovieItem
import com.simplepeople.watcha.ui.appscreen.navigation.AppScreens
import com.simplepeople.watcha.ui.viewmodel.FavoriteViewModel

@Composable
fun FavoriteScreen(
    navController: NavController,
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {

    val movieList by favoriteViewModel.movieList.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(
            items = movieList.toList(),
            key = {
                it.movieId
            }
        ) { movie ->
            MovieItem(
                movie,
                navigateToMovieDetails = {
                    navController.navigate(
                        AppScreens.MovieDetailsScreen.buildArgRoute(
                            movie.movieId
                        )
                    )
                }
            )
        }
    }


}
