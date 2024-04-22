package com.simplepeople.watcha.ui.navigation

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.simplepeople.watcha.ui.viewmodel.AppNavigationViewModel

@Composable
fun SharedBottomBar(
    navController: NavController,
    appNavigationViewModel: AppNavigationViewModel = hiltViewModel()
) {

    val selectedItemIndex by appNavigationViewModel.bottomBarSelectedIndex.collectAsState()

    BottomAppBar {
        NavigationBar {
            BottomNavigationItemProvider.bottomNavigationItemList.forEachIndexed { index, item ->
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
                    }
                )
            }
        }
    }
}