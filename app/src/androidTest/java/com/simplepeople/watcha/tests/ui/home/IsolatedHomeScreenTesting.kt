package com.simplepeople.watcha.tests.ui.home

import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.WatchaTheme
import com.simplepeople.watcha.MainActivity
import com.simplepeople.watcha.ui.home.HomeScreen
import com.simplepeople.watcha.ui.home.HomeViewModel
import com.simplepeople.watcha.ui.navigation.MainAppScreens
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class IsolatedHomeScreenTesting {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val isolatedAndroidComposeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        isolatedAndroidComposeRule.activity.setContent {
            homeViewModel = hiltViewModel()
            val navController = rememberNavController()
            WatchaTheme {
                NavHost(
                    navController = navController,
                    startDestination = MainAppScreens.HomeScreen.route,
                ) {
                    composable(route = MainAppScreens.HomeScreen.route) {
                        HomeScreen(
                            homeViewModel = homeViewModel,
                            lazyGridState = rememberLazyGridState(),
                            navigateToSettings = {},
                            navigateToNavigationBarItem = {},
                            navigateToMovieDetails = {},
                            isRefreshing = false
                        )
                    }
                }
            }
        }
    }

    @Test
    fun movieList_ErrorFetchingMoviesFromSource() {
        isolatedAndroidComposeRule.runOnIdle {
            assert(::homeViewModel.isInitialized)
        }
        homeViewModel.cleanMovieList()
        isolatedAndroidComposeRule.onNodeWithText("movie", substring = true, ignoreCase = true).assertIsNotDisplayed()
    }
}