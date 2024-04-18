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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.appnavigation.AppScreens
import com.simplepeople.watcha.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {},
        bottomBar = {
            BottomAppBar {
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) {
        MovieList(
            paddingValues = it,
            navController = navController
        )
    }
}

@Composable
fun MovieList(
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {

    val movieSet: Set<Movie> by homeViewModel.movieSet.collectAsState()

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        items(movieSet.toList()) { movie ->
            MovieAvatar(
                movie,
                navigateToMovieDetails = { navController.navigate(AppScreens.MovieDetailsScreen.route + "/${movie.movieId}") })
        }
    }
}

@Composable
fun MovieAvatar(
    movie: Movie,
    navigateToMovieDetails: () -> Unit
) {
    Card(modifier = Modifier
        .clickable { navigateToMovieDetails() }
        .fillMaxWidth()
        .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        AsyncImage(
            model = movie.picture,
            contentDescription = movie.overview,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(
            modifier = Modifier
                .padding(5.dp, 0.dp, 5.dp, 0.dp)
        )
        Column(
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