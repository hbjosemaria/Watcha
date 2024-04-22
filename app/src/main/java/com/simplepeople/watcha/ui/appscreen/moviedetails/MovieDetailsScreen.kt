package com.simplepeople.watcha.ui.appscreen.moviedetails

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.viewmodel.MovieDetailsViewModel

@Composable
fun MovieDetailsScreen (
    navigateBack : () -> Unit,
    movieId: Int
) {
    val movieDetailsViewModel =
        hiltViewModel<MovieDetailsViewModel, MovieDetailsViewModel.MovieDetailsViewModelFactory>(
            creationCallback = { factory -> factory.create(movieId = movieId) }
        )

    val movie by movieDetailsViewModel.movie.collectAsState()
    val scrollState: ScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Box (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.picture)
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                contentScale = ContentScale.FillWidth,
                placeholder = painterResource(id = R.drawable.movie_placeholder)
            )
            Box(
                modifier = Modifier
                    .padding(0.dp, 30.dp, 30.dp, 0.dp)
                    .align(Alignment.TopEnd)
            ) {
                Canvas(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Center)
                ) {
                    drawCircle(
                        color = Color.Black,
                        radius = this.size.maxDimension
                    )
                }
                Text(
                    text = movie.voteAverage ?: stringResource(id = R.string.movie_score_empty),
                    style = TextStyle(
                        color = Color.White
                    ),
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
            IconButton(
                onClick = {
                    movieDetailsViewModel.toggleFavorite()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(0.dp, 0.dp, 20.dp, 20.dp)
            ) {
                Icon(
                    imageVector = if (movie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = stringResource(R.string.movie_mark_favorite),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            IconButton(
                onClick = {navigateBack()},
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .size(24.dp)
            ) {
                Icon (
                    Icons.Filled.ArrowBack,
                    Icons.Filled.ArrowBack.name,
                    tint = Color.Black
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
            }
            Row {
                Text(
                    text = movie.genres.map { stringResource(id = it.title) }
                        .joinToString(", "),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            Spacer(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 15.dp)
            )
            Text(
                text = movie.overview
            )
        }
    }
}