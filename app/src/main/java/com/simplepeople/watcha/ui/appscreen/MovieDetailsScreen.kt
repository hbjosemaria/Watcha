package com.simplepeople.watcha.ui.appscreen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.viewmodel.MovieDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable //TODO: clean and refactor MovieDetailsScreen in several composables
fun MovieDetailsScreen(
    movieId: Int,
    navigateBack: () -> Unit
) {

    val movieDetailsViewModel =
        hiltViewModel<MovieDetailsViewModel, MovieDetailsViewModel.MovieDetailsViewModelFactory>(
            creationCallback = { factory -> factory.create(movieId = movieId) }
        )

    val movie by movieDetailsViewModel.movie.collectAsState()

    val scrollState: ScrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent.copy(alpha = 0f)
                ),
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigation_back),
                            tint = Color.Black
                        )
                    }
                }
            )
        },
        bottomBar = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(it.calculateBottomPadding())
        ) {
            Box {
                AsyncImage(
                    model = movie.picture,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1920f / 1280f)
                        .background(Color.White)
                )
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
                    onClick = { movieDetailsViewModel.toggleFavorite() },
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

    }
}