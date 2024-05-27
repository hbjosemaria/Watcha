package com.simplepeople.watcha.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.core.Language
import com.simplepeople.watcha.ui.common.composables.topbar.SingleScreenTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()
    val settingsState by settingsViewModel.settingsState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SingleScreenTopAppBar(
                navigateBack = navigateBack,
                screenTitleResource = R.string.settings,
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .scrollable(
                    state = scrollState,
                    orientation = Orientation.Vertical,
                    enabled = true
                )
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
            ) {
                LanguageSetting(
                    language = settingsState.settings.language,
                    selectLanguage = { language ->
                        settingsViewModel.updateSetting(language)
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                TMDBCredits(
                    context = context
                )
                SettingsDivider()
                AboutMeSection(
                    context = context
                )
            }
        }
    }
}

@Composable
private fun SettingsDivider(
    color: Color = Color.Gray,
) {
    Divider(
        thickness = 1.dp,
        color = color,
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp
            )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageSetting(
    language: Language,
    selectLanguage: (Language) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    SettingsDivider()
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                start = 32.dp
            ),
        text = stringResource(id = R.string.api_settings),
        textAlign = TextAlign.Left,
        fontSize = 14.sp
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 6.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth(),
            expanded = isExpanded,
            onExpandedChange = { expanded ->
                isExpanded = expanded
            },
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                value = stringResource(id = language.textRes),
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                label = {
                    Text(
                        text = stringResource(id = R.string.language)
                    )
                }
            )

            ExposedDropdownMenu(
                modifier = Modifier
                    .fillMaxWidth(),
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }) {
                Language.languageList.forEachIndexed { index, languageItem ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(id = languageItem.textRes)
                            )
                        },
                        onClick = {
                            isExpanded = false
                            selectLanguage(
                                languageItem
                            )
                        },
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                    )
                    if (index != Language.languageList.size - 1) {
                        SettingsDivider(
                            color = Color.Transparent
                        )
                    }
                }
            }
        }
    }
    SettingsDivider()
}


@Composable
private fun TMDBCredits(
    context: Context,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(context.getString(R.string.tmdb_url))
                    )
                )
            }
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(140.dp),
            painter = painterResource(id = R.drawable.tmdb_logo),
            contentDescription = stringResource(id = R.string.tmdb_logo)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.tmdb_credits),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AboutMeSection(
    context: Context,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            text = stringResource(id = R.string.reach_me),
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AboutMeSectionItem(
                context = context,
                action = Intent.ACTION_VIEW,
                modifier = Modifier
                    .weight(1f),
                url = stringResource(id = R.string.github_url),
                imageVector = R.drawable.github_logo,
                contentDescription = R.string.github,
            )
            AboutMeSectionItem(
                context = context,
                action = Intent.ACTION_VIEW,
                modifier = Modifier
                    .weight(1f),
                url = stringResource(id = R.string.linkedin_url),
                imageVector = R.drawable.linkedin_logo,
                contentDescription = R.string.linkedin,
            )
            AboutMeSectionItem(
                context = context,
                action = Intent.ACTION_SENDTO,
                modifier = Modifier
                    .weight(1f),
                url = stringResource(id = R.string.mail_to_gmail),
                imageVector = R.drawable.gmail_logo,
                contentDescription = R.string.gmail,
            )
        }
    }
}

@Composable
private fun AboutMeSectionItem(
    context: Context,
    action: String,
    modifier: Modifier,
    url: String,
    imageVector: Int,
    contentDescription: Int,
) {
    Image(
        modifier = modifier
            .size(80.dp)
            .padding(16.dp)
            .clickable {
                context.startActivity(
                    Intent(action, Uri.parse(url))
                )
            },
        painter = painterResource(id = imageVector),
        contentDescription = stringResource(id = contentDescription),
        colorFilter = ColorFilter.tint(
            color = MaterialTheme.colorScheme.tertiary
        )
    )
}

