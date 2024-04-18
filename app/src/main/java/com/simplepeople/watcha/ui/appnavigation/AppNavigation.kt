package com.simplepeople.watcha.ui.appnavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.appscreen.HomeScreen
import com.simplepeople.watcha.ui.appscreen.MovieDetailsScreen

sealed class AppScreens(val route: String, val screenName: Int) {
    object HomeScreen : AppScreens("home", R.string.home)
    object MovieDetailsScreen : AppScreens("movie_details", R.string.movie_details)
    object FavoritesScreen : AppScreens("favorites", R.string.list_favorites)
    object SearchScreen : AppScreens("search", R.string.search)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.HomeScreen.route) {
        composable(AppScreens.HomeScreen.route) {
            HomeScreen(
                navController = navController
            )
        }
        composable(
            AppScreens.MovieDetailsScreen.route + "/{movieId}",
            arguments = listOf(navArgument("movieId") {
                type = NavType.IntType
                nullable = false
            })
        ) {
            val movieId = it.arguments?.getInt("movieId") ?: 1
            MovieDetailsScreen(
                movieId = movieId,
                navigateBack = { navController.popBackStack() }
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