package com.simplepeople.watcha.ui.common.composables.topbar

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.simplepeople.watcha.ui.common.composables.DefaultIconButton
import com.simplepeople.watcha.ui.common.composables.topbar.common.TopAppBarText

/**
 * TopBar with optional title and navigate back action
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleScreenTopAppBar(
    navigateBack: () -> Unit,
    screenTitleResource: Int? = null,
    scrollBehavior: TopAppBarScrollBehavior,
    backgroundColor: Color = MaterialTheme.colorScheme.background
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = backgroundColor,
            scrolledContainerColor = backgroundColor
        ),
        scrollBehavior = scrollBehavior,
        title = {
            screenTitleResource?.let {
                TopAppBarText(
                    textReference = it
                )
            }
        },
        navigationIcon = {
            DefaultIconButton(
                action = navigateBack,
                iconImage = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = Icons.AutoMirrored.Filled.ArrowBack.name,
                modifier = Modifier
                    .size(35.dp)
                    .padding(start = 8.dp)
            )
        },
    )
}