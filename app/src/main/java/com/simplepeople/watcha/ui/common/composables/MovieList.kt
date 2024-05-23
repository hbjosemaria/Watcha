package com.simplepeople.watcha.ui.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.simplepeople.watcha.domain.core.Movie

@Composable
fun HomeMovieList(
    movieList: LazyPagingItems<Movie>,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    navigateToMovieDetails: (Long) -> Unit,
    paddingValues: PaddingValues? = PaddingValues(top = 5.dp)
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = paddingValues!!,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        state = lazyGridState
    ) {

        item(
            span = {
                GridItemSpan(3)
            }
        ) {
            if (movieList.itemCount > 0) {
                val movie = movieList[0]!!
                MovieItem(
                    movie = movie,
                    modifier = Modifier
                        .padding(WindowInsets.safeDrawing.asPaddingValues())
                        .fillMaxWidth()
                        .clickable {
                            navigateToMovieDetails(movie.movieId)
                        }
                )
            }
        }

        items(
            count = if (movieList.itemCount != 0)
                movieList.itemCount - 1
            else
                movieList.itemCount,
            key = movieList.itemKey { movie ->
                movie.movieId
            }
        ) { index ->
            val movie = movieList[index + 1]!!
            MovieItem(
                movie = movie,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigateToMovieDetails(movie.movieId)
                    }
            )
        }
    }

}

@Composable
fun MovieList(
    movieList: LazyPagingItems<Movie>,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    navigateToMovieDetails: (Long) -> Unit,
    paddingValues: PaddingValues? = null
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = paddingValues ?: PaddingValues(top = 5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        state = lazyGridState,
    ) {
        items(
            count = movieList.itemCount,
            key = movieList.itemKey { movie ->
                movie.movieId
            }
        ) { index ->
            val movie = movieList[index]!!
            MovieItem(
                movie = movie,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigateToMovieDetails(movie.movieId)
                    }
            )
        }
    }
}
