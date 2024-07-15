package com.simplepeople.watcha.tests.ui.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.simplepeople.watcha.MainActivity
import com.simplepeople.watcha.R
import com.simplepeople.watcha.data.model.local.SearchLogItemEntity
import com.simplepeople.watcha.data.service.Room.WatchaDatabase
import com.simplepeople.watcha.ui.navigation.MainAppScreens
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SearchScreenTesting {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val androidComposeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var database: WatchaDatabase

    @Before
    fun setUp() {
        hiltRule.inject()

        (4..7).forEach {
            database.searchLogDao()
                .addNewSearch(
                    SearchLogItemEntity(
                        searchedText = "Movie $it"
                    )
                )
        }
        database.searchLogDao()
            .addNewSearch(
                SearchLogItemEntity(
                    searchedText = "I don't belong here, therefore I'm an alien"
                )
            )

        androidComposeRule.onNodeWithContentDescription(androidComposeRule.activity.getString(MainAppScreens.SearchScreen.name))
            .performClick()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun search_SuccessfulSearch() {
        val movieSearchTitle = "Movie 6"
        androidComposeRule.onNodeWithText(androidComposeRule.activity.getString(R.string.search_placeholder))
            .performTextInput(movieSearchTitle)
        androidComposeRule.waitUntilAtLeastOneExists(
            matcher = hasContentDescription(movieSearchTitle)
        )
        androidComposeRule.onNodeWithContentDescription(movieSearchTitle).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun search_FailedSearch() {
        val movieSearchTitle = "I don't belong here, therefore I'm an alien"
        androidComposeRule.onNodeWithText(androidComposeRule.activity.getString(R.string.search_placeholder))
            .performTextInput(movieSearchTitle)
        androidComposeRule.waitUntilAtLeastOneExists(
            matcher = hasText(androidComposeRule.activity.getString(R.string.search_no_result))
        )
        androidComposeRule.onNodeWithText(androidComposeRule.activity.getString(R.string.search_no_result))
            .assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun searchLog_clickSearchLogItem_successfulSearch() {
        val nodeSearchText = "Movie 6"
        androidComposeRule.onAllNodesWithText(nodeSearchText, substring = true, ignoreCase = true)
            .onFirst().performClick()
        androidComposeRule.waitUntilAtLeastOneExists(
            matcher = hasContentDescription(nodeSearchText)
        )
        androidComposeRule.onAllNodesWithContentDescription(nodeSearchText).assertAny(
            matcher = hasContentDescription(nodeSearchText, substring = true, ignoreCase = true)
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun searchLog_clickSearchLogItem_FailedSearch() {
        val nodeSearchText = "I don't belong here, therefore I'm an alien"
        androidComposeRule.onNodeWithText(nodeSearchText, ignoreCase = true).performClick()
        androidComposeRule.waitUntilAtLeastOneExists(
            matcher = hasText(androidComposeRule.activity.getString(R.string.search_no_result))
        )
        androidComposeRule.onNodeWithText(androidComposeRule.activity.getString(R.string.search_no_result))
            .assertIsDisplayed()
    }

    @Test
    fun searchLog_removeSingleItem() {
        val searchText = "I don't belong here, therefore I'm an alien"
        androidComposeRule.onAllNodesWithContentDescription(Icons.Default.Clear.name).onFirst().performClick()
        androidComposeRule.onNodeWithText(searchText).assertDoesNotExist()
    }

    @Test
    fun searchLog_clearWholeLog() {
        androidComposeRule.onNodeWithText(androidComposeRule.activity.getString(R.string.search_clean_log)).performClick()
        androidComposeRule.onAllNodesWithContentDescription(Icons.Default.Clear.name).assertAll(
            matcher = hasNoClickAction()
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun search_Then_NavigateToMovieDetails() {
        val movieSearchTitle = "Movie 6"
        androidComposeRule.onNodeWithText(androidComposeRule.activity.getString(R.string.search_placeholder))
            .performTextInput(movieSearchTitle)
        androidComposeRule.waitUntilAtLeastOneExists(matcher = hasContentDescription(movieSearchTitle))
        androidComposeRule.onNodeWithContentDescription(movieSearchTitle).performClick()
        androidComposeRule.onNodeWithText("Overview", ignoreCase = true, substring = true).assertIsDisplayed()
    }
}