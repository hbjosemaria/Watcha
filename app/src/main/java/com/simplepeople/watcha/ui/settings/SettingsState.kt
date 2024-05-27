package com.simplepeople.watcha.ui.settings

import com.simplepeople.watcha.domain.core.Settings

data class SettingsState (
    val settings : Settings = Settings()
)