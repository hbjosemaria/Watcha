package com.simplepeople.watcha.tests.ui.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.simplepeople.watcha.domain.core.Language
import com.simplepeople.watcha.domain.core.Settings
import com.simplepeople.watcha.domain.usecase.CacheUseCase
import com.simplepeople.watcha.domain.usecase.CredentialsUseCase
import com.simplepeople.watcha.domain.usecase.SettingsUseCase
import com.simplepeople.watcha.ui.settings.SettingsViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SettingsViewModelTest {

    @MockK
    private lateinit var settingsUseCase: SettingsUseCase

    @MockK
    private lateinit var cacheUseCase: CacheUseCase

    @MockK(relaxed = true)
    private lateinit var credentialsUseCase: CredentialsUseCase

    private lateinit var settingsViewModel: SettingsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery {settingsUseCase.loadSettings()} returns Settings(
            language = Language.Spanish
        )
        coJustRun {settingsUseCase.updateSettings(Language.English) }
        coJustRun {cacheUseCase.forceCacheExpiration()}

        settingsViewModel = SettingsViewModel(
            settingsUseCase,
            cacheUseCase,
            credentialsUseCase
        )
    }

    @Test
    fun `Load settings into state holder`() = runTest {
        val settings = settingsViewModel.settingsState.value.settings
        val expectedSettings = Settings(
            language = Language.Spanish
        )

        assertThat(settings).isInstanceOf(Settings::class.java)
        assertThat(settings).isEqualTo(expectedSettings)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Update settings in viewModel and dataStore`() = runTest {
        val expectedNewSettings = Settings(
            language = Language.English
        )
        val languageSelection = Language.English

        settingsViewModel.settingsState.test {
            val previousSettings = awaitItem().settings

            settingsViewModel.updateSetting(languageSelection)

            advanceUntilIdle()

            val currentSettings = awaitItem().settings
            assertThat(previousSettings).isNotEqualTo(currentSettings)
            assertThat(currentSettings).isEqualTo(expectedNewSettings)

            cancelAndConsumeRemainingEvents()
        }
    }
}