package com.simplepeople.watcha.ui.appscreen.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.simplepeople.watcha.domain.core.Movie

//TODO: scroll back to top automatically when new movie data is fetched
@Composable
fun MovieList(
    movieList : LazyPagingItems<Movie>,
    navigateToMovieDetails: (Long) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 125.dp),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(
            count = movieList.itemCount
        ) { index ->
            val movie = movieList[index]!!
            MovieItem(
                movie,
                navigateToMovieDetails = {
                    navigateToMovieDetails(movie.movieId)
                }
            )
        }
    }
}