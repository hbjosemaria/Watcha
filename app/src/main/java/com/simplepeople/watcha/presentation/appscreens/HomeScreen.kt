package com.simplepeople.watcha.presentation.appscreens

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
import com.simplepeople.watcha.domain.core.Genre
import com.simplepeople.watcha.domain.core.Movie

@Composable
fun HomeScreen(
    navigateToMovieDetails: (Int) -> Unit,
    movieList: List<Movie> //TODO: remove this later. This is a filling example.
) {
    Scaffold (
        topBar = {/*Without topbar*/},
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
            navigateToMovieDetails = navigateToMovieDetails
        )
    }
}

@Composable
fun MovieList(
    paddingValues: PaddingValues,
    movieList: List<Movie>,
    navigateToMovieDetails: (Int) -> Unit) {
    LazyColumn (modifier = Modifier
        .padding(paddingValues.calculateBottomPadding())
    ) {
        items(movieList) {movie ->
            MovieAvatar(
                movie,
                navigateToMovieDetails = {navigateToMovieDetails})
        }
    }
}

@Composable
fun MovieAvatar(
    movie: Movie,
    navigateToMovieDetails: (Int) -> Unit) {
    Column(modifier = Modifier
        .clickable { navigateToMovieDetails(movie.movieId) } //TODO: add arg to navigation to get movie details by its ID
    ) {
        Image(painter = painterResource(id = movie.picture), contentDescription = movie.overview)
        Spacer(modifier = Modifier
            .padding(2.dp, 0.dp, 2.dp, 0.dp)
        )
        Text(text = movie.title)
        Text(text = movie.overview)
    }
}