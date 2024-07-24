package com.simplepeople.watcha.ui.settings.language

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.core.Language
import com.simplepeople.watcha.ui.common.composables.TextFieldClickableSelected
import com.simplepeople.watcha.ui.common.composables.TextFieldClickableUnselected
import com.simplepeople.watcha.ui.common.composables.topbar.SingleScreenTopAppBar
import com.simplepeople.watcha.ui.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsLanguageScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    refreshMovieList: (Boolean) -> Unit,
) {
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val settingsState by settingsViewModel.settingsState.collectAsState()

    Scaffold(
        topBar = {
            SingleScreenTopAppBar(
                navigateBack = navigateBack,
                screenTitleResource = R.string.settings_language,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .scrollable(
                    state = scrollState,
                    orientation = Orientation.Vertical,
                    enabled = true
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 30.dp,
                        end = 30.dp,
                        top = 6.dp,
                        bottom = 6.dp
                    )
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.settings_language_explanation),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    lineHeight = 16.sp
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Language.languageList
                    .sortedBy {
                        if (it == settingsState.settings.language) 0 else 1
                    }
                    .forEach { itemLanguage ->
                        SettingsLanguageItem(
                            selectedLanguage = settingsState.settings.language,
                            itemLanguage = itemLanguage,
                            refreshMovieList = refreshMovieList,
                            updateSetting = { language: Language ->
                                settingsViewModel.updateSetting(language)
                            }
                        )
                    }
            }
        }
    }
}

@Composable
private fun SettingsLanguageItem(
    selectedLanguage: Language,
    itemLanguage: Language,
    refreshMovieList: (Boolean) -> Unit,
    updateSetting: (Language) -> Unit,
) {
    val action = {
        refreshMovieList(selectedLanguage != itemLanguage)
        updateSetting(itemLanguage)
    }

    if (selectedLanguage == itemLanguage)
        TextFieldClickableSelected(
            value = stringResource(id = itemLanguage.textRes),
            action = action,
            iconVector = Icons.Filled.Place
        )
    else
        TextFieldClickableUnselected(
            value = stringResource(id = itemLanguage.textRes),
            action = action,
            iconVector = Icons.Outlined.LocationOn
        )

}