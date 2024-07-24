package com.simplepeople.watcha.ui.common.composables.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import com.simplepeople.watcha.ui.common.composables.topbar.common.TopAppBarLogo

@OptIn(ExperimentalMaterial3Api::class)
/**
 * TopBar with logo
 */
@Composable
fun MainTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.background.copy(
                alpha = .5f
            )
        ),
        title = {},
        navigationIcon = {
            TopAppBarLogo()
        },
        scrollBehavior = scrollBehavior
    )

}