package com.simplepeople.watcha.tests.ui.settings

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsRule
import com.simplepeople.watcha.MainActivity
import com.simplepeople.watcha.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SettingsScreenTesting {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 2)
    val intentsRule = IntentsRule()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun appLocaleChange() {
        composeRule.onNodeWithContentDescription(Icons.Default.Settings.name).performClick()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.language_spanish)).performClick()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.language_english)).performClick()
        composeRule.onNodeWithText("English").assertIsDisplayed()
    }

    @Test
    fun externalLinks_GitHubIntent() {
        composeRule.onNodeWithContentDescription(Icons.Default.Settings.name).performClick()

        Intents.intending(IntentMatchers.hasAction(Intent.ACTION_VIEW))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null))
        composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.github)).performClick()
        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_VIEW))
        Intents.intended(IntentMatchers.hasData(composeRule.activity.getString(R.string.github_url)))
    }

    @Test
    fun externalLinks_LinkedInIntent() {
        composeRule.onNodeWithContentDescription(Icons.Default.Settings.name).performClick()

        Intents.intending(IntentMatchers.hasAction(Intent.ACTION_VIEW))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null))
        composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.linkedin)).performClick()
        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_VIEW))
        Intents.intended(IntentMatchers.hasData(composeRule.activity.getString(R.string.linkedin_url)))
    }

    @Test
    fun externalLinks_TMDBIntent() {
        composeRule.onNodeWithContentDescription(Icons.Default.Settings.name).performClick()

        Intents.intending(IntentMatchers.hasAction(Intent.ACTION_VIEW))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null))
        composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.tmdb_logo)).performClick()
        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_VIEW))
        Intents.intended(IntentMatchers.hasData(composeRule.activity.getString(R.string.tmdb_url)))
    }

    @Test
    fun externalLinks_MailToIntent() {
        composeRule.onNodeWithContentDescription(Icons.Default.Settings.name).performClick()

        Intents.intending(IntentMatchers.hasAction(Intent.ACTION_SENDTO))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null))
        composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.gmail)).performClick()
        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_SENDTO))
        Intents.intended(IntentMatchers.hasData(composeRule.activity.getString(R.string.mail_to_gmail)))
    }
}

