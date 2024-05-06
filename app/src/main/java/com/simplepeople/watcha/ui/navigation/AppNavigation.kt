package com.simplepeople.watcha.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.simplepeople.watcha.ui.viewmodel.AppNavigationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    appNavigationViewModel: AppNavigationViewModel = hiltViewModel()
) {

    val navController = rememberNavController()
    val appNavigationUiState by appNavigationViewModel.appNavigationUiState
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarItem by appNavigationViewModel.snackBarItem
    val context = LocalContext.current

    LaunchedEffect (snackBarItem) {
        if (snackBarItem.showSnackbar) {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = context.getString(snackBarItem.textSnackbar),
                duration = SnackbarDuration.Short
            )
            appNavigationViewModel.resetSnackbar()
        }
    }

    Scaffold(
        topBar = {
            SharedTopBar(
                navigateToSearchScreen = {
                    navController.navigate(AppScreens.SearchScreen.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateBack = {
                    navController.popBackStack()
                },
                scrollBehavior = scrollBehavior,
                filterNowPlaying = {
                    appNavigationViewModel.emitFilterNowPlayingEvent()
                },
                filterPopular = {
                    appNavigationViewModel.emitFilterPopularEvent()
                },
                filterTopRated = {
                    appNavigationViewModel.emitFilterTopRatedEvent()
                },
                filterUpcoming = {
                    appNavigationViewModel.emitFilterUpcomingEvent()
                },
                appBarOption = appNavigationUiState.appBarOption,
                selectedTopBarItem = appNavigationUiState.selectedTopBarItem
            )
        },
        bottomBar = {
            SharedNavigationBar(
                navController = navController,
                selectedNavigationItemIndex = appNavigationUiState.selectedNavigationItemIndex,
                showNavigationBar = appNavigationUiState.showNavigationBar,
                updateNavigationBarSelectedIndex = { index ->
                    appNavigationViewModel.updateNavigationBarSelectedIndex(index)
                },
                emitScrollToTopEvent = {
                    appNavigationViewModel.emitScrollToTopEvent()
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Content(
            innerPadding = innerPadding,
            navController = navController,
            appBarOption = appNavigationUiState.appBarOption
        )
    }
}





