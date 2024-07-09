package com.simplepeople.watcha.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.core.Language
import com.simplepeople.watcha.ui.common.composables.TextFieldClickableUnselected
import com.simplepeople.watcha.ui.common.composables.topbar.SingleScreenTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToLanguageSettings: () -> Unit,
    navigateToLogIn: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()
    val settingsState by settingsViewModel.settingsState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(settingsState.isLoggedOut) {
        if (settingsState.isLoggedOut) {
            navigateToLogIn()
        }
    }

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
                    navigateToLanguageSettings = navigateToLanguageSettings
                )
                SettingsDivider()
                LogOutButton (
                    action = { settingsViewModel.logOut() }
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
    HorizontalDivider(
        thickness = 1.dp,
        color = color,
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp
            )
    )
}

@Composable
private fun LanguageSetting(
    language: Language,
    navigateToLanguageSettings: () -> Unit,
) {
    //TODO: change the icon for another more appropriate
    TextFieldClickableUnselected(
        value = stringResource(id = language.textRes),
        action = navigateToLanguageSettings,
        iconVector = Icons.Default.Place,
        labelText = stringResource(id = R.string.app_language)
    )
}

@Composable
private fun LogOutButton(
    action: () -> Unit
) {
    TextFieldClickableUnselected(
        value = stringResource(id = R.string.log_out),
        action = action,
        iconVector = Icons.AutoMirrored.Default.ExitToApp
    )
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

