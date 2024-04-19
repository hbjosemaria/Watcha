package com.simplepeople.watcha.ui.appscreen

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.simplepeople.watcha.R

sealed class AppScreens(
    val route: String,
    val name: Int
) {
    object HomeScreen : AppScreens("home", R.string.home)
    object MovieDetailsScreen : AppScreens("movie_details", R.string.movie_details)
    object FavoritesScreen : AppScreens("favorites", R.string.list_favorites)
    object SearchScreen : AppScreens("search", R.string.search)
}

data class BottomNavigationItem(
    val navigationRoute: String,
    val itemSelectedIcon: ImageVector,
    val itemNotSelectedIcon: ImageVector,
    val itemLabel: Int
)

class BottomNavigationItemProvider {
    companion object {
        val bottomNavigationItemList: List<BottomNavigationItem> = listOf(
            BottomNavigationItem(
                AppScreens.HomeScreen.route,
                Icons.Filled.Home,
                Icons.Outlined.Home,
                AppScreens.HomeScreen.name
            ),
            BottomNavigationItem(
                AppScreens.SearchScreen.route,
                Icons.Filled.Search,
                Icons.Outlined.Search,
                AppScreens.SearchScreen.name
            ),
            BottomNavigationItem(
                AppScreens.FavoritesScreen.route,
                Icons.Filled.Favorite,
                Icons.Outlined.FavoriteBorder,
                AppScreens.FavoritesScreen.name
            )
        )
    }
}

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    var showBottomBar by rememberSaveable {
        mutableStateOf(true)
    }

    //Set a listener for navigation changes in order to check if the Scaffold BottomBar should be shown or not
    navController.addOnDestinationChangedListener { _, destination, _ ->
        showBottomBar = !destination.route!!.startsWith(AppScreens.MovieDetailsScreen.route)
    }

    Scaffold(
        topBar = {},
        bottomBar = {
            if (showBottomBar) {
                BottomAppBar {
                    NavigationBar {
                        BottomNavigationItemProvider.bottomNavigationItemList.forEachIndexed { index, item ->
                            val itemLabel = stringResource(item.itemLabel)
                            NavigationBarItem(
                                selected = selectedItemIndex == index,
                                onClick = {
                                    selectedItemIndex = index
                                    navController.navigate(item.navigationRoute) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    if (selectedItemIndex == index) {
                                        Icon(
                                            imageVector = item.itemSelectedIcon,
                                            contentDescription = itemLabel
                                        )
                                    } else {
                                        Icon(
                                            imageVector = item.itemNotSelectedIcon,
                                            contentDescription = itemLabel
                                        )
                                    }
                                },
                                label = {
                                    Text(itemLabel)
                                })
                        }
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
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
                HomeScreen(
                    navController = navController
                )
            }
            composable(
                AppScreens.MovieDetailsScreen.route + "/{movieId}",
                arguments = listOf(navArgument("movieId") {
                    type = NavType.IntType
                })
            ) {
                val movieId = it.arguments?.getInt("movieId") ?: 1
                MovieDetailsScreen(
                    movieId = movieId,
                    navigateBack = { navController.popBackStack() }
                )
            }
            composable(AppScreens.FavoritesScreen.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }) {
                FavoritesScreen()
            }
            composable(AppScreens.SearchScreen.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }) {
                SearchScreen()
            }
        }
    }
}