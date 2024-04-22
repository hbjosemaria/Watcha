package com.simplepeople.watcha.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.simplepeople.watcha.ui.appscreen.home.FavoriteScreen
import com.simplepeople.watcha.ui.appscreen.home.HomeScreen
import com.simplepeople.watcha.ui.appscreen.moviedetails.MovieDetailsScreen
import com.simplepeople.watcha.ui.appscreen.search.SearchScreen
import com.simplepeople.watcha.ui.viewmodel.AppNavigationViewModel

@Composable
fun Content(
    innerPadding: PaddingValues,
    navController: NavHostController,
    appNavigationViewModel : AppNavigationViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.HomeScreen.route,
        modifier = Modifier
            .padding(innerPadding)
    ) {
        composable(
            AppScreens.HomeScreen.route,
            enterTransition = {
                EnterTransition.None
            }
        ) {
            appNavigationViewModel.navigatingToMainScreen()
            HomeScreen(
                navigateToMovieDetails = {movieId : Int ->
                    navController.navigate(
                        AppScreens.MovieDetailsScreen.buildArgRoute(movieId)
                    )
                }
            )
        }
        composable(
            AppScreens.MovieDetailsScreen.buildRoute(),
            arguments = listOf(navArgument("movieId") {
                type = NavType.IntType
            })
        ) {
            appNavigationViewModel.navigatingToMovieDetails()
            val movieId = it.arguments?.getInt("movieId") ?: 1
            MovieDetailsScreen(
                movieId = movieId,
                navigateBack = {navController.popBackStack()}
            )
        }
        composable(
            AppScreens.FavoriteScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            appNavigationViewModel.navigatingToMainScreen()
            FavoriteScreen(
                navigateToMovieDetails = {movieId : Int ->
                    navController.navigate(
                        AppScreens.MovieDetailsScreen.buildArgRoute(movieId)
                    )
                }
            )
        }
        composable(
            AppScreens.SearchScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            appNavigationViewModel.navigatingToSearch()
            SearchScreen(
                navigateToMovieDetails = {movieId : Int ->
                    navController.navigate(
                        AppScreens.MovieDetailsScreen.buildArgRoute(movieId)
                    )
                }
            )
        }
    }
}