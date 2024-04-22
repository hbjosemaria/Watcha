package com.simplepeople.watcha.ui.appscreen.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            navigateToMovieDetails()
        }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.picture)
                .crossfade(true)
                .build(),
            contentDescription = movie.title,
            contentScale = ContentScale.Fit,
            placeholder = painterResource(id = R.drawable.movie_placeholder)
        )
        Spacer(
            modifier = Modifier
                .padding(6.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = movie.title,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Justify,
                maxLines = 1,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(
                modifier = Modifier
                    .padding(4.dp)
            )
            Text(
                text = movie.overview,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Justify,
                maxLines = 2
            )
        }
    }
}