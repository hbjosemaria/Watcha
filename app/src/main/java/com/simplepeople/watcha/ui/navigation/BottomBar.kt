package com.simplepeople.watcha.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun SharedBottomBar(
    navController: NavController,
    selectedBottomItemIndex : Int,
    showBottomBar : Boolean,
    updateBottomBarSelectedIndex : (Int) -> Unit,
    emitScrollToTopEvent : () -> Unit
) {

    if (showBottomBar) {
        BottomAppBar {
            NavigationBar {
                BottomNavigationItemProvider.bottomNavigationItemList.forEachIndexed { index, item ->
                    val itemLabel = stringResource(item.itemLabel)
                    NavigationBarItem(
                        selected = selectedBottomItemIndex == index,
                        onClick = {
                            updateBottomBarSelectedIndex(index)
                            if (selectedBottomItemIndex != index) {
                                navController.navigate(item.navigationRoute) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            else emitScrollToTopEvent()
                        },
                        icon = {
                            if (selectedBottomItemIndex == index) {
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
                        }
                    )
                }
            }
        }
    }
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