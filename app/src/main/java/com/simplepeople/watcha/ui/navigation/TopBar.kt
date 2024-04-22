package com.simplepeople.watcha.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.viewmodel.AppNavigationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedTopBar(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    appNavigationViewModel: AppNavigationViewModel = hiltViewModel()
) {

    val showTextTitle by appNavigationViewModel.showTextTitle.collectAsState()
    val showSearchIcon by appNavigationViewModel.showSearchIcon.collectAsState()
    val showBackIcon by appNavigationViewModel.showBackIcon.collectAsState()

    CenterAlignedTopAppBar(
        title = {
            when {
                showTextTitle -> {
                    Row {
                        Text(
                            text = stringResource(id = R.string.search)
                        )
                    }
                }
                !showBackIcon -> {
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.logo_main_screen_dark),
                            contentDescription = stringResource(id = R.string.app_name),
                            modifier = Modifier
                                .size(160.dp)
                                .padding(8.dp)
                        )
                    }
                }
                else -> {}
            }
        },
        navigationIcon = {
            if (showBackIcon) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        Icons.Filled.ArrowBack.name
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
        actions = {
            if (showSearchIcon) {
                IconButton(
                    onClick = {
                        navController.navigate(AppScreens.SearchScreen.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier
                        .size(46.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = Icons.Default.Search.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp)
                    )
                }
            }
        }
    )
}