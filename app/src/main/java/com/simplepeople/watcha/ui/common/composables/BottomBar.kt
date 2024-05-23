package com.simplepeople.watcha.ui.common.composables

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.simplepeople.watcha.ui.navigation.AppScreens

@Composable
fun SharedNavigationBar(
    selectedNavigationItemIndex: Int,
    navigateToNavigationBarItem: (String) -> Unit,
    updateNavigationBarSelectedIndex: (Int) -> Unit,
    scrollToTopAction: () -> Unit
) {
    NavigationBar(
        windowInsets = WindowInsets(47.dp, 0.dp, 47.dp, 6.dp)
    ) {
        BottomNavigationItemProvider.navigationItemLists.forEachIndexed { index, item ->
            val itemLabel = stringResource(item.itemLabel)
            NavigationBarItem(
                selected = selectedNavigationItemIndex == index,
                onClick = {
                    updateNavigationBarSelectedIndex(index)
                    if (selectedNavigationItemIndex != index) {
                        navigateToNavigationBarItem(item.navigationRoute)
                    } else scrollToTopAction()
                },
                icon = {
                    if (selectedNavigationItemIndex == index) {
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
                    Text(
                        text = itemLabel,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )
        }
    }

}

data class NavigationItem(
    val navigationRoute: String,
    val itemSelectedIcon: ImageVector,
    val itemNotSelectedIcon: ImageVector,
    val itemLabel: Int
)

class BottomNavigationItemProvider {
    companion object {
        val navigationItemLists: List<NavigationItem> = listOf(
            NavigationItem(
                AppScreens.HomeScreen.route,
                Icons.Filled.Home,
                Icons.Outlined.Home,
                AppScreens.HomeScreen.name
            ),
            NavigationItem(
                AppScreens.FavoriteScreen.route,
                Icons.Filled.Favorite,
                Icons.Outlined.FavoriteBorder,
                AppScreens.FavoriteScreen.name
            )
        )
    }
}

class NavigationBarItemSelection {
    companion object {
        var selectedNavigationItemIndex: Int = 0
    }
}