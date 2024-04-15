package com.simplepeople.watcha.ui.appscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.stateholder.MovieDetailsUiState
import com.simplepeople.watcha.ui.viewmodel.MovieDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    movie: Movie,
    movieDetailsViewModel: MovieDetailsViewModel = viewModel(),
    navigateBack: () -> Unit
) {
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
        Column(
            modifier = Modifier
                .padding(it.calculateBottomPadding())
        ) {
            Box {
                Text(
                    text = movie.voteAverage ?: stringResource(id = R.string.movie_score_empty)
                )
                //Image(painter = , contentDescription = ) TODO: Load image from website with Glide? Website base URL to each image is this: https://image.tmdb.org/t/p/w1280/
                IconButton(onClick = { movieDetailsViewModel.toggleFavorite(movie) }) {
                    Icon(
                        imageVector = if (!movie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(R.string.movie_mark_favorite))
                }
            }
            Text(
                text = movie.title
            )
            Row {
                Text(
                    text = movie.releaseDate
                )
                Text(
                    text = movie.company
                )
            }
            Row {
                Text(movie.genres.joinToString(
                    separator = ", "
                    ) { genre ->
                        genre.name
                    }
                )
            }
            Text(
                text = movie.overview
            )
        }

    }
}