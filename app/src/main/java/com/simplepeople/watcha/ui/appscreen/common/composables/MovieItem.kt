package com.simplepeople.watcha.ui.appscreen.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.CachePolicy
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
                .memoryCacheKey(movie.picture)
                .diskCacheKey(movie.picture)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .fallback(R.drawable.movie_placeholder)
                .placeholder(R.drawable.movie_placeholder)
                .build(),
            contentDescription = movie.title,
            contentScale = ContentScale.Fit,
            placeholder = painterResource(id = R.drawable.movie_placeholder)
        )
//        Spacer(
//                modifier = Modifier
//                    .padding(6.dp)
//                )
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//        ) {
//            Text(
//                text = movie.title,
//                overflow = TextOverflow.Ellipsis,
//                textAlign = TextAlign.Justify,
//                maxLines = 1,
//                style = TextStyle(
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            )
//            Spacer(
//                modifier = Modifier
//                    .padding(4.dp)
//            )
//            Text(
//                text = movie.overview,
//                overflow = TextOverflow.Ellipsis,
//                textAlign = TextAlign.Justify,
//                maxLines = 2
//            )
//        }
    }
}