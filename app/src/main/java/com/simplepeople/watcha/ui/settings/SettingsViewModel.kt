package com.simplepeople.watcha.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplepeople.watcha.domain.core.Language
import com.simplepeople.watcha.domain.usecase.CacheUseCase
import com.simplepeople.watcha.domain.usecase.CredentialsUseCase
import com.simplepeople.watcha.domain.usecase.SettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCase: SettingsUseCase,
    private val cacheUseCase: CacheUseCase,
    private val credentialsUseCase: CredentialsUseCase
) : ViewModel() {

    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState = _settingsState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            _settingsState.value = _settingsState.value.copy(
                settings = settingsUseCase.loadSettings()
            )
        }

    }

    fun <T> updateSetting(parameter: T) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsUseCase.updateSettings(parameter = parameter)
            _settingsState.value = when (parameter) {
                is Language -> {
                    AppCompatDelegate.setApplicationLocales(
                        LocaleListCompat.forLanguageTags(
                            parameter.isoCode
                        )
                    )
                    cacheUseCase.forceCacheExpiration()
                    _settingsState.value.copy(
                        settings = _settingsState.value.settings.copy(
                            language = parameter
                        )
                    )

                }
                else -> {
                    _settingsState.value
                }
            }
        }
    }

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            _settingsState.value = _settingsState.value.copy(
                isLoggedOut = true
            )
            cacheUseCase.forceCacheExpiration()
            credentialsUseCase.logOut()
        }
    }
}