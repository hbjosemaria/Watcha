package com.simplepeople.watcha.ui.common.composables.topbar

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.simplepeople.watcha.ui.common.composables.DefaultIconButton
import com.simplepeople.watcha.ui.common.composables.topbar.common.TopAppBarLogo

//FavoriteScreen TopBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    navigateToSearchScreen: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    topBarAlpha: Float? = null
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                alpha = topBarAlpha ?: 1f
            )
        ),
        title = {},
        navigationIcon = {
            TopAppBarLogo()
        },
        scrollBehavior = scrollBehavior,
        actions = {
            DefaultIconButton(
                action = navigateToSearchScreen,
                iconImage = Icons.Default.Search,
                contentDescription = Icons.Default.Search.name,
                modifier = Modifier
                    .size(30.dp)
            )
        }
    )

}