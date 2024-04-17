package com.simplepeople.watcha.ui.appscreen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.viewmodel.MovieDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    movieId: Int,
    movieDetailsViewModel: MovieDetailsViewModel = viewModel(),
    navigateBack: () -> Unit
) {
    val movie by remember {mutableStateOf(movieDetailsViewModel.getMovieDetails(movieId))}
    
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),

        topBar = { TopAppBar(
            title = { Text(movie.title) },
            navigationIcon = { IconButton(onClick = {navigateBack()}) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.navigation_back))
            }}
        )}
    ) {
        Surface (
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Box {
                     Image(
                        painter = painterResource(id = R.drawable.watcha_logo),
                        contentDescription = movie.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                     ) //TODO: Load image from website with Glide? Website base URL to each image is this: https://image.tmdb.org/t/p/w1280/
                    Text(//TODO: make it a circle with the rating
                        text = movie.voteAverage ?: stringResource(id = R.string.movie_score_empty),
                        style = TextStyle(
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(0.dp, 20.dp, 20.dp, 0.dp)
                            .background(Color.Gray)
                    )
                    IconButton(
                        onClick = { movieDetailsViewModel.toggleFavorite(movie) },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(0.dp, 0.dp, 20.dp, 20.dp)
                    ) {
                        Icon(
                            imageVector = if (!movie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = stringResource(R.string.movie_mark_favorite),
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp)
                ) {
                    Text(
                        text = movie.title,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(5.dp, 0.dp, 0.dp, 5.dp)
                    )
                    Row {
                        Text(
                            text = movie.releaseDate,
                            style = TextStyle(
                                fontSize = 14.sp
                            ),
                            modifier = Modifier
                                .padding(0.dp, 0.dp, 10.dp, 0.dp)
                        )
                        Text(
                            text = movie.company,
                            style = TextStyle(
                                fontSize = 14.sp
                            ),
                        )
                    }
                    Row {
                        Text(
                            text = movie.genres.map{ stringResource(id = it.title) }.joinToString(", "),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 15.dp))
                    Text(
                        text = movie.overview
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun MovieDetailsScreenPreview() {
    MovieDetailsScreen(1) {
        
    }
}