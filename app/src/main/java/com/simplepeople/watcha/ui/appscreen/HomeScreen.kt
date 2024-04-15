package com.simplepeople.watcha.ui.appscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.appnavigation.AppScreens

@Composable
fun HomeScreen(
    navController: NavController,
    movieList: Set<Movie> //TODO: remove this later. This is a filling example.
) {
    Scaffold (
        bottomBar = {
            BottomAppBar {
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        MovieList(
            paddingValues = paddingValues,
            movieList,
            navController = navController
        )
    }
}

@Composable
fun MovieList(
    paddingValues: PaddingValues,
    movieSet: Set<Movie>,
    navController: NavController) {
    LazyColumn (modifier = Modifier
        .padding(paddingValues.calculateBottomPadding())
    ) {
        items(movieSet.toList()) { movie ->
            MovieAvatar(
                movie,
                navController = navController)
        }
    }
}

@Composable
fun MovieAvatar(
    movie: Movie,
    navController: NavController) {
    Column(modifier = Modifier
        .clickable { navController.navigate(AppScreens.MovieDetailsScreen.route + "/${movie.movieId}") } //TODO: add arg to navigation to get movie details by its ID
    ) {
        Image(painter = painterResource(id = movie.picture), contentDescription = movie.overview)
        Spacer(modifier = Modifier
            .padding(2.dp, 0.dp, 2.dp, 0.dp)
        )
        Text(text = movie.title)
        Text(text = movie.overview)
    }
}

//Here should be located the stateHolders. These have the compose or flow status holder where de data is collected from the ViewModel and then passed to the UI element