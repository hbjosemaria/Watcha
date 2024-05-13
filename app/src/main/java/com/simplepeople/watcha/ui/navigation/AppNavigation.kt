package com.simplepeople.watcha.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.favorite.FavoriteScreen
import com.simplepeople.watcha.ui.home.HomeScreen
import com.simplepeople.watcha.ui.moviedetails.MovieDetailsScreen
import com.simplepeople.watcha.ui.search.SearchScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.HomeScreen.route
    ) {
        composable(
            AppScreens.HomeScreen.route,
            enterTransition = {
                EnterTransition.None
            }
        ) {
            HomeScreen(
                navigateToMovieDetails = { movieId: Long ->
                    navController.navigate(
                        AppScreens.MovieDetailsScreen.buildArgRoute(movieId)
                    )
                },
                navigateToNavigationBarItem = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToSearchScreen = {
                    navController.navigate(AppScreens.SearchScreen.route)
                }
            )
        }
        composable(
            AppScreens.MovieDetailsScreen.buildRoute(),
            arguments = listOf(navArgument("movieId") {
                type = NavType.LongType
            })
        ) {
            val movieId = it.arguments?.getLong("movieId") ?: 1
            MovieDetailsScreen(
                movieId = movieId,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            AppScreens.FavoriteScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            FavoriteScreen(
                navigateToMovieDetails = { movieId: Long ->
                    navController.navigate(
                        AppScreens.MovieDetailsScreen.buildArgRoute(movieId)
                    )
                },
                navigateToNavigationBarItem = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToSearchScreen = {
                    navController.navigate(AppScreens.SearchScreen.route)
                }
            )
        }
        composable(
            AppScreens.SearchScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            SearchScreen(navigateBack = {
                navController.popBackStack()
            },
                navigateToMovieDetails = { movieId: Long ->
                    navController.navigate(
                        AppScreens.MovieDetailsScreen.buildArgRoute(movieId)
                    )
                }
            )
        }
    }
}

sealed class AppScreens(
    val route: String,
    val name: Int
) {
    data object HomeScreen : AppScreens("home", R.string.home)
    data object MovieDetailsScreen : AppScreens("movie_details", R.string.movie_details) {
        fun buildArgRoute(value: Long): String {
            return "$route/${value}"
        }

        fun buildRoute(): String {
            return "$route/{movieId}"
        }
    }

    data object FavoriteScreen : AppScreens("favorites", R.string.list_favorites)
    data object SearchScreen : AppScreens("search", R.string.search)
}





