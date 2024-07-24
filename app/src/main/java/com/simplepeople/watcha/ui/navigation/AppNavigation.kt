package com.simplepeople.watcha.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.simplepeople.watcha.ui.main.favorite.FavoriteScreen
import com.simplepeople.watcha.ui.main.home.HomeScreen
import com.simplepeople.watcha.ui.main.moviedetails.MovieDetailsScreen
import com.simplepeople.watcha.ui.main.moviedetails.YoutubeScreen
import com.simplepeople.watcha.ui.main.search.SearchScreen
import com.simplepeople.watcha.ui.settings.SettingsScreen
import com.simplepeople.watcha.ui.settings.language.SettingsLanguageScreen
import com.simplepeople.watcha.ui.userprofile.ProfileScreen

@Composable
fun AppNavigation(
    isUserSignedIn: Boolean = false,
    appNavigationViewModel: AppNavigationViewModel = hiltViewModel(),
) {

    val navController = rememberNavController()
    navController.addOnDestinationChangedListener { _, destination, _ ->
        if (destination.route == MainAppScreens.HomeScreen.route) {
            appNavigationViewModel.updateNavigationBarIndex(0)
        }
    }

    val appNavigationState by appNavigationViewModel.appNavigationState.collectAsState()

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
                navigateToAuth = { navController.navigate(AuthAppScreens.AuthScreen.route) },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = AuthAppScreens.AuthScreen.route
        ) {
            AuthScreen(
                navigateToHome = { navController.navigate(MainAppScreens.MainScreen.route) },
                navigateBack = { navController.popBackStack(AuthAppScreens.SignInScreen.route, false) }
            )
        }

        navigation(
            route = MainAppScreens.MainScreen.route,
            startDestination = MainAppScreens.HomeScreen.route
        ) {
            composable(
                route = MainAppScreens.HomeScreen.route
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
                    isRefreshing = isRefreshing,
                    selectedNavigationItemIndex = appNavigationState.selectedNavigationItemIndex,
                    userProfileResult = appNavigationState.userProfileResult,
                    updateNavigationItemIndex = { newValue ->
                        appNavigationViewModel.updateNavigationBarIndex(newValue)
                    },
                    loadUserProfile = {
                        appNavigationViewModel.loadUserProfile()
                    }
                )
            }
            composable(
                route = MainAppScreens.FavoriteScreen.route
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
                    selectedNavigationItemIndex = appNavigationState.selectedNavigationItemIndex,
                    userProfileResult = appNavigationState.userProfileResult,
                    updateNavigationItemIndex = { newValue ->
                        appNavigationViewModel.updateNavigationBarIndex(newValue)
                    }
                )
            }
            composable(
                route = MainAppScreens.YoutubeScreen.buildRoute(),
                arguments = listOf(navArgument(NavigationVariableNames.VIDEO_KEY.variableName) {
                    type = NavType.StringType
                })
            ) {
                val videoKey =
                    it.arguments?.getString(NavigationVariableNames.VIDEO_KEY.variableName) ?: ""
                YoutubeScreen(
                    videoKey = videoKey,
                    navigateBack = {
                        navController.navigateUp()
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
                    },
                    navigateToYoutubeScreen = { videoKey : String ->
                        navController.navigate(
                            MainAppScreens.YoutubeScreen.buildArgRoute(videoKey)
                        )
                    }
                )
            }
            composable(
                route = MainAppScreens.SearchScreen.route
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
                    },
                    selectedNavigationItemIndex = appNavigationState.selectedNavigationItemIndex,
                    userProfileResult = appNavigationState.userProfileResult,
                    updateNavigationItemIndex = { newValue ->
                        appNavigationViewModel.updateNavigationBarIndex(newValue)
                    }
                )
            }
            composable(
                route = MainAppScreens.ProfileScreen.route
            ) {
                ProfileScreen(
                    navigateToSettings = {
                        navController.navigate(MainAppScreens.SettingsScreen.route)
                    },
                    navigateToSignIn = {
                        navController.navigate(AuthAppScreens.SignInScreen.route) {
                            popUpTo(MainAppScreens.MainScreen.route) {
                                inclusive = true
                            }
                        }
                    },
                    navigateToNavigationBarItem = { route ->
                        navigateToBottomNavigationItem(
                            navController = navController,
                            route = route
                        )
                    },
                    selectedNavigationItemIndex = appNavigationState.selectedNavigationItemIndex,
                    userProfileResult = appNavigationState.userProfileResult,
                    updateNavigationItemIndex = { newValue ->
                        appNavigationViewModel.updateNavigationBarIndex(newValue)
                    },
                    updateAvatar = {
                        appNavigationViewModel.updateAvatar()
                    }
                )
            }

            settingsNestedGraph(
                navController = navController
            )
        }
    }
}

private fun NavGraphBuilder.settingsNestedGraph(
    navController: NavHostController,
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
    MOVIE_ID("movieId"),
    VIDEO_KEY("videoKey")
}




