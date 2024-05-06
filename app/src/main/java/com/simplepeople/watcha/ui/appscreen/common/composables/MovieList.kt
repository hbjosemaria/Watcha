package com.simplepeople.watcha.ui.appscreen.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.simplepeople.watcha.domain.core.Movie

@Composable
fun MovieList(
    movieList : LazyPagingItems<Movie>,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    navigateToMovieDetails: (Long) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        state = lazyGridState,
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
