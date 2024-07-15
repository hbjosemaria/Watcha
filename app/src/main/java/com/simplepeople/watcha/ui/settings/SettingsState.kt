package com.simplepeople.watcha.ui.settings

import com.simplepeople.watcha.domain.core.Settings

data class SettingsState(
    val isLoggedOut: Boolean = false,
    val settings: Settings = Settings(),
)