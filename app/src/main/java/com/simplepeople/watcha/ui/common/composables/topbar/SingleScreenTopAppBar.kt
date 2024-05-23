package com.simplepeople.watcha.ui.common.composables.topbar

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.simplepeople.watcha.ui.common.composables.DefaultIconButton
import com.simplepeople.watcha.ui.common.composables.topbar.common.TopAppBarText

//Screens that displays a title and back navigation
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleScreenTopAppBar(
    navigateBack: () -> Unit,
    screenTitleResource: Int,
    scrollBehavior: TopAppBarScrollBehavior
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            TopAppBarText(
                textReference = screenTitleResource
            )
        },
        navigationIcon = {
            DefaultIconButton(
                action = navigateBack,
                iconImage = Icons.Filled.ArrowBack,
                contentDescription = Icons.Filled.ArrowBack.name,
                modifier = Modifier
                    .size(30.dp)
            )
        }
    )

}