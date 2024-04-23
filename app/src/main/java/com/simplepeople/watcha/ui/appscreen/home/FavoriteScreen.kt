package com.simplepeople.watcha.ui.appscreen.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.appscreen.common.MovieList
import com.simplepeople.watcha.ui.viewmodel.FavoriteViewModel

@Composable
fun FavoriteScreen(
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    navigateToMovieDetails: (Long) -> Unit
) {

    val movieList: LazyPagingItems<Movie> = favoriteViewModel.movieList.collectAsLazyPagingItems()

    MovieList (
        movieList = movieList,
        navigateToMovieDetails = navigateToMovieDetails
    )

}
