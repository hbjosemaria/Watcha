package com.simplepeople.watcha.tests.domain.usecase

import androidx.compose.ui.text.intl.Locale
import com.google.common.truth.Truth.assertThat
import com.simplepeople.watcha.data.module.DataStoreVariableType
import com.simplepeople.watcha.data.repository.DataStoreRepository
import com.simplepeople.watcha.domain.core.Language
import com.simplepeople.watcha.domain.core.Settings
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SettingsUseCaseTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun loadSettings_languageIsoCodeNullPath() = runTest {
        val deviceLanguageIsoCode = Locale.current.language
        dataStoreRepository.saveData(
            dataType = DataStoreVariableType.StringType,
            dataName = "settings_language",
            data = deviceLanguageIsoCode
        )
        val settings = Settings(
            language = when (deviceLanguageIsoCode) {
                Language.English.isoCode -> Language.English
                Language.Spanish.isoCode -> Language.Spanish
                else -> Language.English
            }
        )

        val expectedSettings = Settings(
            language = Language.Spanish
        )

        assertThat(settings).isEqualTo(expectedSettings)
    }


    @Test
    fun loadSettings_languageIsoCodeExistsPath() = runTest {
        //Simulating language preference is already stored
        dataStoreRepository.saveData(
            dataType = DataStoreVariableType.StringType,
            dataName = "settings_language",
            data = Locale.current.language
        )

        val (_, languageIsoCode) = dataStoreRepository.loadData<String>(
            listOf(
                Pair(DataStoreVariableType.StringType, "settings_language")
            )
        ).first()

        val settings = Settings(
            language = when (languageIsoCode) {
                Language.English.isoCode -> Language.English
                Language.Spanish.isoCode -> Language.Spanish
                else -> Language.English
            }
        )

        val expectedSettings = Settings(
            language = Language.English
        )

        assertThat(settings).isNotEqualTo(expectedSettings)
    }

    @Test
    fun updateSettings_LanguageChange() = runTest {
        val parameter = Language.Spanish
        dataStoreRepository.saveData(
            dataType = DataStoreVariableType.StringType,
            dataName = parameter.parameterName,
            data = parameter.isoCode
        )
    }
}
