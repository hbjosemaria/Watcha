package com.simplepeople.watcha.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.navigation.BottomNavigationItemProvider.Companion.bottomNavigationItemList
import com.simplepeople.watcha.ui.viewmodel.AppNavigationViewModel

sealed class AppScreens(
    val route: String,
    val name: Int
) {
    data object HomeScreen : AppScreens("home", R.string.home)
    data object MovieDetailsScreen : AppScreens("movie_details", R.string.movie_details) {
        fun buildArgRoute(value: Int) : String {
            return "$route/${value}"
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    appNavigationViewModel: AppNavigationViewModel = hiltViewModel()
) {

    val navController = rememberNavController()
    val showBottomBar by appNavigationViewModel.showBottomBar.collectAsState()
    val showTopBar by appNavigationViewModel.showTopBar.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    navController.addOnDestinationChangedListener { _, destination, _ ->
        if (destination.route == AppScreens.HomeScreen.route) {
            appNavigationViewModel.updateBottomBarSelectedIndex(
                bottomNavigationItemList.indexOfFirst { it.navigationRoute == AppScreens.HomeScreen.route }
            )
        }
    }

    Scaffold(
        topBar = {
            if (showTopBar) {
                SharedTopBar(
                    navController,
                    scrollBehavior
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                SharedBottomBar(navController)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Content(
            innerPadding = innerPadding,
            navController = navController
        )
    }
}



