package com.simplepeople.watcha.ui.appnavigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.core.exampleMovieSet
import com.simplepeople.watcha.ui.appscreen.HomeScreen
import com.simplepeople.watcha.ui.appscreen.MovieDetailsScreen
import com.simplepeople.watcha.ui.viewmodel.MovieDetailsViewModel

sealed class AppScreens(val route: String, val screenName: Int) {
    object HomeScreen: AppScreens("home", R.string.home)
    object MovieDetailsScreen: AppScreens("movie_details", R.string.movie_details)
    object FavoritesScreen: AppScreens("favorites", R.string.list_favorites)
    object SearchScreen: AppScreens("search", R.string.search)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.HomeScreen.route) {
        composable(AppScreens.HomeScreen.route){
            HomeScreen(
                navController = navController,
                movieList = exampleMovieSet) //TODO: remove this later. It's an example filling list.
        }
        composable(
            AppScreens.MovieDetailsScreen.route + "/{movieId}",
            arguments = listOf(navArgument("movieId") {type = NavType.IntType})) {
            val movieId = it.arguments?.getInt("movieId")
            val movie = exampleMovieSet.find {movie -> movie.movieId == (movieId ?: 1)}
            MovieDetailsScreen(
                movie = movie!!, //TODO: remove this later. It's an example.
                navigateBack = {navController.popBackStack()}
            )
        }
        composable(AppScreens.FavoritesScreen.route) {
            //TODO: composable function for My Favorites Screen
        }
        composable(AppScreens.SearchScreen.route) {
            //TODO: composable function for Search Screen
        }
    }
}