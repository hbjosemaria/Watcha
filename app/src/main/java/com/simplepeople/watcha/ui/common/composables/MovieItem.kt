package com.simplepeople.watcha.ui.common.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.common.utils.shimmerEffect
import kotlinx.coroutines.Dispatchers

@Composable
fun MovieItem(
    movie: Movie,
    modifier: Modifier,
    textSize: TextUnit = 12.sp,
) {
    Column(
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .dispatcher(Dispatchers.IO)
                .data(movie.picture)
                .placeholder(R.drawable.movie_placeholder)
                .fallback(R.drawable.movie_placeholder)
                .error(R.drawable.movie_error_placeholder)
                .allowConversionToBitmap(true)
                .crossfade(true)
                .memoryCacheKey(movie.picture)
                .diskCacheKey(movie.picture)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = movie.title,
            contentScale = ContentScale.Fit,
            placeholder = painterResource(id = R.drawable.movie_placeholder)
        )
        Text(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth(),
            text = movie.title,
            textAlign = TextAlign.Left,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = textSize
        )
    }
}

@Composable
fun MovieItemPlaceholder(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .shimmerEffect()
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
        )
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
                .height(16.dp)
                .shimmerEffect()
        )
    }
}
