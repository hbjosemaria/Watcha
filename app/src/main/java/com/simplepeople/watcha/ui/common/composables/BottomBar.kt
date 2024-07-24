package com.simplepeople.watcha.ui.common.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.navigation.MainAppScreens
import kotlinx.coroutines.Dispatchers

@Composable
fun SharedNavigationBar(
    selectedNavigationItemIndex: Int,
    navigateToNavigationBarItem: (String) -> Unit,
    updateNavigationBarSelectedIndex: (Int) -> Unit,
    scrollToTopAction: () -> Unit = {},
    avatarUrl: String,
) {
    val borderColor = MaterialTheme.colorScheme.onTertiaryContainer.copy(
        alpha = .25f
    )

    val image = ImageRequest.Builder(LocalContext.current)
        .dispatcher(Dispatchers.IO)
        .data(avatarUrl)
        .placeholder(R.drawable.poster_placeholder)
        .fallback(R.drawable.user_image_placeholder)
        .error(R.drawable.user_image_placeholder)
        .allowConversionToBitmap(true)
        .crossfade(true)
        .memoryCacheKey(avatarUrl)
        .diskCacheKey(avatarUrl)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()

    NavigationBar(
        modifier = Modifier
            .height(70.dp)
            .drawBehind {
                drawLine(
                    brush = SolidColor(borderColor),
                    strokeWidth = 2.dp.toPx(),
                    cap = Stroke.DefaultCap,
                    start = Offset(0f, 2f),
                    end = Offset(size.width, 2f)
                )
            },
        windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
    ) {
        BottomNavigationItemProvider.navigationItemLists.forEachIndexed { index, item ->
            val itemLabel = stringResource(item.itemLabel)
            NavigationBarItem(
                modifier = Modifier
                    .padding(bottom = 10.dp),
                selected = selectedNavigationItemIndex == index,
                onClick = {
                    updateNavigationBarSelectedIndex(index)
                    if (selectedNavigationItemIndex != index) {
                        navigateToNavigationBarItem(item.navigationRoute)
                    } else scrollToTopAction()
                },
                icon = {
                    if (selectedNavigationItemIndex == index) {
                        item.itemSelectedIcon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = itemLabel,
                                modifier = Modifier
                                    .size(28.dp)
                            )
                        } ?: AsyncImage(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(
                                    shape = CircleShape
                                )
                                .border(
                                    border = BorderStroke(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    ),
                                    shape = CircleShape
                                ),
                            model = image,
                            contentDescription = stringResource(id = R.string.user_profile),
                            contentScale = ContentScale.FillWidth
                        )
                    } else {
                        item.itemNotSelectedIcon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = itemLabel,
                                modifier = Modifier
                                    .size(28.dp)
                            )
                        } ?: AsyncImage(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(
                                    shape = CircleShape
                                ),
                            model = image,
                            contentDescription = stringResource(id = R.string.user_profile),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.background
                )
            )
        }
    }
}

data class NavigationItem(
    val navigationRoute: String,
    val itemSelectedIcon: ImageVector? = null,
    val itemNotSelectedIcon: ImageVector? = null,
    val itemLabel: Int,
)

class BottomNavigationItemProvider {
    companion object {
        val navigationItemLists: List<NavigationItem> = listOf(
            NavigationItem(
                MainAppScreens.HomeScreen.route,
                Icons.Filled.Home,
                Icons.Outlined.Home,
                MainAppScreens.HomeScreen.name
            ),
            NavigationItem(
                MainAppScreens.SearchScreen.route,
                Icons.Filled.Search,
                Icons.Filled.Search,
                MainAppScreens.SearchScreen.name
            ),
            NavigationItem(
                MainAppScreens.FavoriteScreen.route,
                Icons.Filled.Favorite,
                Icons.Outlined.FavoriteBorder,
                MainAppScreens.FavoriteScreen.name
            ),
            NavigationItem(
                navigationRoute = MainAppScreens.ProfileScreen.route,
                itemLabel = MainAppScreens.ProfileScreen.name
            )
        )
    }
}