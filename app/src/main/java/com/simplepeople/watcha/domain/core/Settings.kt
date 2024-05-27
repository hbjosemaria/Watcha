package com.simplepeople.watcha.domain.core

import com.simplepeople.watcha.R

data class Settings(
    val language: Language = Language.English,
)

sealed class Language(
    val isoCode: String,
    val textRes: Int,
    val parameterName: String = "settings_language",
) {

    companion object {
        val languageList by lazy {
            listOf(Spanish, English)
        }
    }

    data object Spanish : Language(
        isoCode = "es",
        textRes = R.string.language_spanish
    )

    data object English : Language(
        isoCode = "en",
        textRes = R.string.language_english
    )
}