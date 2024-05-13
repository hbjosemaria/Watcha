package com.simplepeople.watcha.ui.moviedetails

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.navigation.topbar.TransparentTopAppBar

@Composable
fun MovieDetailsScreen(
    movieId: Long,
    navigateBack: () -> Unit,
    movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel<MovieDetailsViewModel, MovieDetailsViewModel.MovieDetailsViewModelFactory>(
        creationCallback = { factory ->
            factory.create(movieId = movieId)
        }
    )
) {

    val movie by movieDetailsViewModel.movie
    val movieDetailsUiState by movieDetailsViewModel.movieDetailsUiState
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(movieDetailsUiState.snackBarItem) {
        if (movieDetailsUiState.snackBarItem.show) {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = context.getString(movieDetailsUiState.snackBarItem.message),
                duration = SnackbarDuration.Short
            )
            movieDetailsViewModel.resetSnackbar()
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { paddingContent ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingContent)
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(movie.picture)
                            .crossfade(true)
                            .memoryCacheKey(movie.picture)
                            .diskCacheKey(movie.picture)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .fallback(R.drawable.movie_placeholder)
                            .placeholder(R.drawable.movie_placeholder)
                            .build(),
                        contentDescription = movie.title,
                        contentScale = ContentScale.FillWidth,
                        placeholder = painterResource(id = R.drawable.movie_placeholder)
                    )
                    //TODO: Improve this UI element
                    Box(
                        modifier = Modifier
                            .padding(0.dp, 52.dp, 30.dp, 0.dp)
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
                            text = movie.voteAverage
                                ?: stringResource(id = R.string.movie_score_empty),
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

            TransparentTopAppBar {
                navigateBack()
            }
        }

    }

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MovieDetailsPreview() {
    MovieDetailsScreen(
        movieId = 647313L,
        navigateBack = {}
    )
}
