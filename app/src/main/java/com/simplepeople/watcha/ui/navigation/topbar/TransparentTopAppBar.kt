package com.simplepeople.watcha.ui.navigation.topbar

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.simplepeople.watcha.ui.common.composables.DefaultIconButton

//Screens for detailed info with no bar, like MovieDetailsScreen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentTopAppBar(
    navigateBack: () -> Unit,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {},
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