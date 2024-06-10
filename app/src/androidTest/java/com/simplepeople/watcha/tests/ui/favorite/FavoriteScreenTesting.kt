package com.simplepeople.watcha.tests.ui.favorite

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import com.simplepeople.watcha.MainActivity
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.navigation.MainAppScreens
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class FavoriteScreenTesting {

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
    fun home_OpenMovieDetails_AddFavorite_Then_CheckFavorite() {
        val movieTitle = "Movie"
        androidComposeRule.onAllNodesWithContentDescription(movieTitle, substring = true, ignoreCase = true).onFirst().performClick()
        androidComposeRule.onNodeWithContentDescription(androidComposeRule.activity.getString(R.string.movie_mark_favorite))
            .performClick()
        androidComposeRule.onNodeWithText(androidComposeRule.activity.getString(R.string.favorite_add_success)).assertIsDisplayed()
        Espresso.pressBack()
        androidComposeRule.onNodeWithContentDescription(androidComposeRule.activity.getString(MainAppScreens.FavoriteScreen.name))
            .performClick()
        androidComposeRule.onNodeWithContentDescription(movieTitle, substring = true, ignoreCase = true).assertIsDisplayed()
    }

    @Test
    fun favorite_OpenMovieDetails_RemoveFavorite_Then_CheckNotFavorite() {
        val movieTitle = "Movie"
        androidComposeRule.onAllNodesWithContentDescription(movieTitle, substring = true, ignoreCase = true).onFirst().performClick()
        androidComposeRule.onNodeWithContentDescription(androidComposeRule.activity.getString(R.string.movie_mark_favorite))
            .performClick()
        Espresso.pressBack()
        androidComposeRule.onNodeWithContentDescription(androidComposeRule.activity.getString(MainAppScreens.FavoriteScreen.name))
            .performClick()
        androidComposeRule.onNodeWithContentDescription(movieTitle, substring = true, ignoreCase = true).performClick()
        androidComposeRule.onNodeWithContentDescription(androidComposeRule.activity.getString(R.string.movie_mark_favorite))
            .performClick()
        androidComposeRule.onNodeWithText(androidComposeRule.activity.getString(R.string.favorite_remove_success)).assertIsDisplayed()
        Espresso.pressBack()
        androidComposeRule.onNodeWithText(androidComposeRule.activity.getString(R.string.favorite_list_empty)).assertIsDisplayed()

    }
}