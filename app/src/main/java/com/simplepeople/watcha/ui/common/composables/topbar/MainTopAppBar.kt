package com.simplepeople.watcha.ui.common.composables.topbar

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    topBarAlpha: Float? = null,
    navigateToSettings: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background.copy(
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
                action = navigateToSettings,
                iconImage = Icons.Default.Settings,
                contentDescription = Icons.Default.Settings.name,
                modifier = Modifier
                    .size(35.dp)
                    .padding(end = 8.dp)
            )
        }
    )

}