package com.simplepeople.watcha.ui.appscreen.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.appscreen.home.FavoriteScreen
import com.simplepeople.watcha.ui.appscreen.home.HomeScreen
import com.simplepeople.watcha.ui.appscreen.moviedetails.MovieDetailsScreen
import com.simplepeople.watcha.ui.appscreen.navigation.BottomNavigationItemProvider.Companion.bottomNavigationItemList
import com.simplepeople.watcha.ui.appscreen.search.SearchScreen
import com.simplepeople.watcha.ui.viewmodel.AppNavigationViewModel
import kotlinx.coroutines.FlowPreview

sealed class AppScreens(
    val route: String,
    val name: Int
) {
    data object HomeScreen : AppScreens("home", R.string.home)
    data object MovieDetailsScreen : AppScreens("movie_details", R.string.movie_details) {
        fun buildArgRoute(value: Int) : String {
            return "$route/${value.toString()}"
        }
        fun buildRoute() : String {
            return "$route/{movieId}"
        }
    }
    data object FavoriteScreen : AppScreens("favorites", R.string.list_favorites)
    data object SearchScreen : AppScreens("search", R.string.search)
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
                AppScreens.FavoriteScreen.route,
                Icons.Filled.Favorite,
                Icons.Outlined.FavoriteBorder,
                AppScreens.FavoriteScreen.name
            )
        )
    }
}

@FlowPreview
@ExperimentalFoundationApi
@Composable
fun AppNavigation(
    appNavigationViewModel: AppNavigationViewModel = hiltViewModel()
) {

    val navController = rememberNavController()

    val showBottomBar by appNavigationViewModel.showBottomBar.collectAsState()

    //Set a listener for navigation changes in order to check if the Scaffold BottomBar should be shown or not
    navController.addOnDestinationChangedListener { _, destination, _ ->

        //TODO: refactor this call to make it cleaner and scalability proof
        //If navigation destiny is not any of the first level layer, then hide Search icon and BottomBar
        if (destination.route == AppScreens.SearchScreen.route ||
            destination.route!!.startsWith(AppScreens.MovieDetailsScreen.route)
        ) {
            appNavigationViewModel.toggleShoWSearchIcon(false)
            appNavigationViewModel.toggleShowBottomBar(false)
            appNavigationViewModel.toggleShowBackIcon(true)
        } else {
            appNavigationViewModel.toggleShoWSearchIcon(true)
            appNavigationViewModel.toggleShowBottomBar(true)
            appNavigationViewModel.toggleShowBackIcon(false)
        }

        if (destination.route == AppScreens.HomeScreen.route) {
            appNavigationViewModel.updateBottomBarSelectedIndex(
                bottomNavigationItemList.indexOfFirst { it.navigationRoute == AppScreens.HomeScreen.route }
            )
        }
    }

    Scaffold(
        topBar = {
            SharedTopBar(navController)
        },
        bottomBar = {
            if (showBottomBar) {
                SharedBottomBar(navController)
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
                AppScreens.MovieDetailsScreen.buildRoute(),
                arguments = listOf(navArgument("movieId") {
                    type = NavType.IntType
                })
            ) {
                val movieId = it.arguments?.getInt("movieId") ?: 1
                MovieDetailsScreen(
                    movieId = movieId
                )
            }
            composable(
                AppScreens.FavoriteScreen.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }) {
                FavoriteScreen(navController)
            }
            composable(
                AppScreens.SearchScreen.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }) {
                SearchScreen(navController)
            }
        }
    }
}

//TODO: make it semi-visible and add an event listener when scrolling to hide it when scrolling forward(up)
// Also, fix the AppBar padding to fix it over the Scaffold content

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedTopBar(
    navController: NavController,
    appNavigationViewModel: AppNavigationViewModel = hiltViewModel()
) {

    val showTopBar by appNavigationViewModel.showTopBar.collectAsState()
    val showSearchIcon by appNavigationViewModel.showSearchIcon.collectAsState()
    val showBackIcon by appNavigationViewModel.showBackIcon.collectAsState()

    if (showTopBar) {
        TopAppBar(
            title = {},
            navigationIcon = {
                if (showBackIcon) {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            Icons.Filled.ArrowBack.name
                        )
                    }
                }
            },
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            actions = {
                if (showSearchIcon) {
                    IconButton(
                        onClick = {
                            navController.navigate(AppScreens.SearchScreen.route) {
                                appNavigationViewModel.toggleShoWSearchIcon(false)
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = Icons.Default.Search.name
                        )
                    }
                }
            }
        )
    }

}


@Composable
fun SharedBottomBar(
    navController: NavController,
    appNavigationViewModel: AppNavigationViewModel = hiltViewModel()
) {

    val selectedItemIndex by appNavigationViewModel.bottomBarSelectedIndex.collectAsState()

    BottomAppBar {
        NavigationBar {
            bottomNavigationItemList.forEachIndexed { index, item ->
                val itemLabel = stringResource(item.itemLabel)
                NavigationBarItem(
                    selected = selectedItemIndex == index,
                    onClick = {
                        appNavigationViewModel.updateBottomBarSelectedIndex(index)
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