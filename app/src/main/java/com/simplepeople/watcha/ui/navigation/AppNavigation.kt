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
import androidx.compose.runtime.collectAsState
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
    val appNavigationUiState by appNavigationViewModel.appNavigationUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect (appNavigationUiState.showSnackbar) {
        if (appNavigationUiState.showSnackbar) {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = context.getString(appNavigationUiState.textSnackbar),
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
                appBarOption = appNavigationUiState.appBarOption
            )
        },
        bottomBar = {
            SharedBottomBar(
                navController = navController,
                selectedBottomItemIndex = appNavigationUiState.selectedBottomItemIndex,
                showBottomBar = appNavigationUiState.showBottomBar,
                updateBottomBarSelectedIndex = { index ->
                    appNavigationViewModel.updateBottomBarSelectedIndex(index)
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





