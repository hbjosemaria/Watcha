package com.simplepeople.watcha.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.appscreen.home.FavoriteScreen
import com.simplepeople.watcha.ui.appscreen.home.HomeScreen
import com.simplepeople.watcha.ui.appscreen.moviedetails.MovieDetailsScreen
import com.simplepeople.watcha.ui.appscreen.search.SearchScreen
import com.simplepeople.watcha.ui.viewmodel.AppNavigationViewModel

@Stable
@Composable
fun Content(
    innerPadding: PaddingValues,
    navController: NavHostController,
    appBarOption: AppBarOption,
    appNavigationViewModel : AppNavigationViewModel = hiltViewModel()
) {

    NavHost(
        navController = navController,
        startDestination = AppScreens.HomeScreen.route,
        modifier = if (appBarOption != AppBarOption.MOVIE_DETAILS) {
            Modifier
                .padding(innerPadding)
        } else {
            Modifier
        }
    ) {
        composable(
            AppScreens.HomeScreen.route,
            enterTransition = {
                EnterTransition.None
            }
        ) {
            appNavigationViewModel.navigatingToHomeScreen()
            HomeScreen { movieId: Long ->
                navController.navigate(
                    AppScreens.MovieDetailsScreen.buildArgRoute(movieId)
                )
            }
        }
        composable(
            AppScreens.MovieDetailsScreen.buildRoute(),
            arguments = listOf(navArgument("movieId") {
                type = NavType.LongType
            })
        ) {
            appNavigationViewModel.navigatingToMovieDetails()
            val movieId = it.arguments?.getLong("movieId") ?: 1
            MovieDetailsScreen(
                movieId = movieId
            )
        }
        composable(
            AppScreens.FavoriteScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            appNavigationViewModel.navigatingToFavoritesScreen()
            FavoriteScreen { movieId: Long ->
                navController.navigate(
                    AppScreens.MovieDetailsScreen.buildArgRoute(movieId)
                )
            }
        }
        composable(
            AppScreens.SearchScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            appNavigationViewModel.navigatingToSearch()
            SearchScreen(
                navigateToMovieDetails = {movieId : Long ->
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
        fun buildArgRoute(value: Long) : String {
            return "$route/${value}"
        }
        fun buildRoute() : String {
            return "$route/{movieId}"
        }
    }
    data object FavoriteScreen : AppScreens("favorites", R.string.list_favorites)
    data object SearchScreen : AppScreens("search", R.string.search)
}