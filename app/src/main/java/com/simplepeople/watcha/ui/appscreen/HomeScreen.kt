package com.simplepeople.watcha.ui.appscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.core.exampleMovieSet
import com.simplepeople.watcha.ui.appnavigation.AppScreens

@Composable
fun HomeScreen(
    navController: NavController,
    movieList: Set<Movie> //TODO: remove this later. This is a filling example.
) {
    Scaffold (
        topBar = {},
        bottomBar = {
            BottomAppBar {
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) { MovieList(
            paddingValues = it,
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
        .padding(paddingValues)
        .fillMaxSize()
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
    Card(modifier = Modifier
        .clickable { navController.navigate(AppScreens.MovieDetailsScreen.route + "/${movie.movieId}") }
        .fillMaxWidth()
        .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp) //TODO: add arg to navigation to get movie details by its ID
    ) {
        Image(
            painter = painterResource(id = movie.picture),
            contentDescription = movie.overview,
            modifier = Modifier
                .fillMaxWidth())
        Spacer(modifier = Modifier
            .padding(5.dp, 0.dp, 5.dp, 0.dp)
        )
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            Text(
                text = movie.title,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(text = movie.overview)
        }

    }
}

@Preview
@Composable
private fun MovieAvatarPreview() {
    MovieAvatar(
        exampleMovieSet.first(),
        rememberNavController()
    )

}