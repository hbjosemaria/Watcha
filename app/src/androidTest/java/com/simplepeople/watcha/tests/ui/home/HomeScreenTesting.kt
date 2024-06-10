package com.simplepeople.watcha.tests.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.simplepeople.watcha.MainActivity
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.navigation.MainAppScreens
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeScreenTesting {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val androidComposeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun topBarButtons_MovieListChange() {
        val homeTopBarButtons =
            listOf(
                androidComposeRule.activity.getString(R.string.home_popular),
                androidComposeRule.activity.getString(R.string.home_top_rated),
                androidComposeRule.activity.getString(R.string.home_upcoming),
                androidComposeRule.activity.getString(R.string.home_now_playing)
            )
        var seventhMovie = androidComposeRule.onAllNodesWithText(
            text = "Movie",
            substring = true
        )[6]
        homeTopBarButtons.forEach { topBarButtonName ->
            androidComposeRule.onNodeWithText(topBarButtonName).performClick()
            val newMovie = androidComposeRule.onAllNodesWithText(
                text = "Movie",
                substring = true
            )[6]
            assertNotEquals(seventhMovie, newMovie)
            seventhMovie = newMovie
        }
    }

    @Test
    fun navigation_Settings() {
        androidComposeRule.onNodeWithContentDescription(Icons.Default.Settings.name).performClick()
        androidComposeRule.onNodeWithText(androidComposeRule.activity.getString(R.string.settings))
            .assertIsDisplayed()
    }

    @Test
    fun navigationBar_Navigation() {
        val searchScreenButtonName =
            androidComposeRule.activity.getString(MainAppScreens.SearchScreen.name)
        val favoriteScreenButtonName =
            androidComposeRule.activity.getString(MainAppScreens.FavoriteScreen.name)
        val homeScreenButtonName =
            androidComposeRule.activity.getString(MainAppScreens.HomeScreen.name)

        androidComposeRule.onNodeWithContentDescription(searchScreenButtonName).performClick()
        androidComposeRule.onNodeWithText(androidComposeRule.activity.getString(R.string.search_label))
            .assertIsDisplayed()

        androidComposeRule.onNodeWithContentDescription(favoriteScreenButtonName).performClick()
        androidComposeRule.onNodeWithContentDescription(Icons.Filled.Settings.name)
            .assertIsDisplayed()

        androidComposeRule.onNodeWithContentDescription(homeScreenButtonName).performClick()
        androidComposeRule.onNodeWithText(androidComposeRule.activity.getString(R.string.home_now_playing))
            .assertIsDisplayed()
    }

    @Test
    fun movieDetails_Navigation() {
        val movieItemNode = androidComposeRule.onAllNodesWithText(
            text = "Movie",
            substring = true
        )[0]
        movieItemNode.performClick()
        androidComposeRule.onNodeWithText("Overview", substring = true).assertIsDisplayed()
    }
}

