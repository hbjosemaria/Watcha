package com.simplepeople.watcha.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.simplepeople.watcha.ui.auth.auth.AuthScreen
import com.simplepeople.watcha.ui.auth.signin.SignInScreen
import com.simplepeople.watcha.ui.auth.signup.SignUpScreen
import com.simplepeople.watcha.ui.common.composables.NavigationBarIndex
import com.simplepeople.watcha.ui.main.favorite.FavoriteScreen
import com.simplepeople.watcha.ui.main.home.HomeScreen
import com.simplepeople.watcha.ui.main.moviedetails.MovieDetailsScreen
import com.simplepeople.watcha.ui.main.search.SearchScreen
import com.simplepeople.watcha.ui.settings.SettingsScreen
import com.simplepeople.watcha.ui.settings.language.SettingsLanguageScreen

@Composable
fun AppNavigation(
    isUserSignedIn: Boolean = false
) {

    val navController = rememberNavController()
    navController.addOnDestinationChangedListener { _, destination, _ ->
        if (destination.route == MainAppScreens.HomeScreen.route) {
            NavigationBarIndex.setSelectedIndex(0)
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (!isUserSignedIn) AuthAppScreens.SignInScreen.route else MainAppScreens.MainScreen.route
    ) {
        composable(
            route = AuthAppScreens.SignInScreen.route
        ) {
            SignInScreen(
                navigateToHome = {
                    navController.navigate(MainAppScreens.MainScreen.route) {
                        popUpTo(AuthAppScreens.SignInScreen.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                navigateToSignUp = {
                    navController.navigate(AuthAppScreens.SignUpScreen.route)
                },
                navigateToAuth = {
                    navController.navigate(AuthAppScreens.AuthScreen.route)
                }
            )
        }

        composable(
            route = AuthAppScreens.SignUpScreen.route
        ) {
            SignUpScreen(
                navigateToAuth = { navController.navigate(AuthAppScreens.AuthScreen.route)},
                navigateBack = {navController.popBackStack()}
            )
        }

        composable(
            route = AuthAppScreens.AuthScreen.route
        ) {
            AuthScreen(
                navigateToHome = {navController.navigate(MainAppScreens.MainScreen.route)},
                navigateBack = {navController.navigateUp()}
            )
        }

        mainNestedGraph(
            navController = navController
        )
    }
}

private fun NavGraphBuilder.mainNestedGraph(
    navController: NavHostController
) {
    navigation(
        route = MainAppScreens.MainScreen.route,
        startDestination = MainAppScreens.HomeScreen.route
    ) {
        composable(
            route = MainAppScreens.HomeScreen.route,
            enterTransition = {
                EnterTransition.None
            }
        ) { backStackEntry ->
            val isRefreshing = backStackEntry.savedStateHandle
                .get<Boolean>(NavigationVariableNames.REFRESH_MOVIE_LIST.variableName) ?: false
            HomeScreen(
                navigateToMovieDetails = { movieId: Long ->
                    navController.navigate(
                        MainAppScreens.MovieDetailsScreen.buildArgRoute(movieId)
                    )
                },
                navigateToNavigationBarItem = { route ->
                    navigateToBottomNavigationItem(
                        navController = navController,
                        route = route
                    )
                },
                navigateToSettings = {
                    navController.navigate(MainAppScreens.SettingsScreen.route)
                },
                isRefreshing = isRefreshing
            )
        }
        composable(
            route = MainAppScreens.FavoriteScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            FavoriteScreen(
                navigateToMovieDetails = { movieId: Long ->
                    navController.navigate(
                        MainAppScreens.MovieDetailsScreen.buildArgRoute(movieId)
                    )
                },
                navigateToNavigationBarItem = { route ->
                    navigateToBottomNavigationItem(
                        navController = navController,
                        route = route
                    )
                },
                navigateToSettings = {
                    navController.navigate(MainAppScreens.SettingsScreen.route)
                }
            )
        }
        composable(
            route = MainAppScreens.MovieDetailsScreen.buildRoute(),
            arguments = listOf(navArgument(NavigationVariableNames.MOVIE_ID.variableName) {
                type = NavType.LongType
            })
        ) {
            val movieId =
                it.arguments?.getLong(NavigationVariableNames.MOVIE_ID.variableName) ?: 1
            MovieDetailsScreen(
                movieId = movieId,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = MainAppScreens.SearchScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            SearchScreen(
                navigateToMovieDetails = { movieId: Long ->
                    navController.navigate(
                        MainAppScreens.MovieDetailsScreen.buildArgRoute(movieId)
                    )
                },
                navigateToNavigationBarItem = { route ->
                    navigateToBottomNavigationItem(
                        navController = navController,
                        route = route
                    )
                }
            )
        }

        settingsNestedGraph(
            navController = navController
        )
    }
}

private fun NavGraphBuilder.settingsNestedGraph(
    navController: NavHostController
) {
    navigation(
        route = MainAppScreens.SettingsScreen.route,
        startDestination = SettingsAppScreens.SettingsScreen.route
    ) {
        composable(
            route = SettingsAppScreens.SettingsScreen.route,
        ) { backStackEntry ->
            val isRefreshing = backStackEntry.savedStateHandle
                .get<Boolean>(NavigationVariableNames.REFRESH_MOVIE_LIST.variableName) ?: false

            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set(NavigationVariableNames.REFRESH_MOVIE_LIST.variableName, isRefreshing)

            SettingsScreen(
                settingsViewModel = backStackEntry.sharedViewModel(navController = navController),
                navigateBack = {
                    navController.popBackStack()
                },
                navigateToLanguageSettings = {
                    navController.navigate(SettingsAppScreens.SettingsLanguageScreen.route)
                },
                navigateToLogIn = {
                    navController.navigate(AuthAppScreens.SignInScreen.route) {
                        popUpTo(MainAppScreens.MainScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = SettingsAppScreens.SettingsLanguageScreen.route
        ) { backStackEntry ->
            SettingsLanguageScreen(
                settingsViewModel = backStackEntry.sharedViewModel(navController = navController),
                navigateBack = {
                    navController.popBackStack()
                },
                refreshMovieList = { isRefreshing: Boolean ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(NavigationVariableNames.REFRESH_MOVIE_LIST.variableName, isRefreshing)
                }
            )
        }
    }
}

private fun navigateToBottomNavigationItem(
    navController: NavHostController,
    route: String,
) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
): T {
    val route = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(route)
    }
    return hiltViewModel(parentEntry)
}

enum class NavigationVariableNames(val variableName: String) {
    REFRESH_MOVIE_LIST("refresh_movie_list"),
    MOVIE_ID("movieId")
}






