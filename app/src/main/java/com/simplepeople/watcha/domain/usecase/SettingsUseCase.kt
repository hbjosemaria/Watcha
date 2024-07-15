package com.simplepeople.watcha.domain.usecase

import androidx.compose.ui.text.intl.Locale
import com.simplepeople.watcha.data.module.DataStoreVariableType
import com.simplepeople.watcha.data.repository.DataStoreRepository
import com.simplepeople.watcha.domain.core.Language
import com.simplepeople.watcha.domain.core.Settings
import javax.inject.Inject

class SettingsUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) {
    suspend fun loadSettings(): Settings {
        val (_, languageIsoCode) = dataStoreRepository.loadData<String>(
            listOf(
                Pair(DataStoreVariableType.StringType, "settings_language")
            )
        ).first()

        return if (languageIsoCode.isNullOrEmpty()) {
            val deviceLanguageIsoCode = Locale.current.language
            dataStoreRepository.saveData(
                dataType = DataStoreVariableType.StringType,
                dataName = "settings_language",
                data = deviceLanguageIsoCode
            )
            Settings(
                language = when (deviceLanguageIsoCode) {
                    Language.English.isoCode -> Language.English
                    Language.Spanish.isoCode -> Language.Spanish
                    else -> Language.English
                }
            )
        } else {
            Settings(
                language = when (languageIsoCode) {
                    Language.English.isoCode -> Language.English
                    Language.Spanish.isoCode -> Language.Spanish
                    else -> Language.English
                }
            )
        }
    }

    //If added new settings variables, then implement their class verification inside the When clause
    suspend fun <T> updateSettings(parameter: T) {
        when (parameter) {
            is Language -> {
                dataStoreRepository.saveData(
                    dataType = DataStoreVariableType.StringType,
                    dataName = parameter.parameterName,
                    data = parameter.isoCode
                )
            }
        }
    }
}