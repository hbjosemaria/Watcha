package com.simplepeople.watcha.ui.appscreen.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.core.Movie

@Composable
fun MovieItem(
    movie: Movie,
    navigateToMovieDetails: () -> Unit
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .clickable {
            navigateToMovieDetails()
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)

    ) {
        AsyncImage( //TODO: Fix poster size
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.picture)
                .crossfade(true)
                .build(),
            contentDescription = movie.title,
            contentScale = ContentScale.FillWidth,
            placeholder = painterResource(id = R.drawable.movie_placeholder)
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