package com.simplepeople.watcha.presentation.appnavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.core.exampleMovieList
import com.simplepeople.watcha.presentation.appscreens.HomeScreen
import com.simplepeople.watcha.presentation.appscreens.MovieDetailsScreen

sealed class AppScreens(val route: String, val screenName: Int) {
    object HomeScreen: AppScreens("home", R.string.home)
    object MovieDetailsScreen: AppScreens("movie_details/{movieId}", R.string.movie_details)
    object FavoritesScreen: AppScreens("favorites", R.string.list_favorites)
    object SearchScreen: AppScreens("search", R.string.search)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.HomeScreen.route) {
        composable(AppScreens.HomeScreen.route){
            HomeScreen(
                navigateToMovieDetails = {navController.navigate(AppScreens.MovieDetailsScreen.route)},
                movieList = exampleMovieList) //TODO: remove this later. It's an example filling list.
        }
        composable(
            AppScreens.MovieDetailsScreen.route,
            arguments = listOf(navArgument("movieId") {type = NavType.IntType})) {
            MovieDetailsScreen(
                movie = exampleMovieList[0] //TODO: remove this later. It's an example.
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