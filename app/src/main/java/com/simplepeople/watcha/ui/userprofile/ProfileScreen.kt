package com.simplepeople.watcha.ui.userprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.common.composables.ColumnElementDivider
import com.simplepeople.watcha.ui.common.composables.SharedNavigationBar
import com.simplepeople.watcha.ui.common.composables.TextFieldClickableUnselected
import com.simplepeople.watcha.ui.navigation.UserProfileResult
import kotlinx.coroutines.Dispatchers

@Composable
fun ProfileScreen(
    navigateToSettings: () -> Unit,
    navigateToSignIn: () -> Unit,
    navigateToNavigationBarItem: (String) -> Unit,
    updateNavigationItemIndex: (Int) -> Unit,
    selectedNavigationItemIndex: Int,
    userProfileResult: UserProfileResult,
    updateAvatar: () -> Unit,
    userProfileViewModel: UserProfileViewModel = hiltViewModel(),
) {

    val userProfileState by userProfileViewModel.userProfileState.collectAsState()

    LaunchedEffect(userProfileState.isLoggedOut) {
        if (userProfileState.isLoggedOut) {
            navigateToSignIn()
        }
    }

    LaunchedEffect(Unit) {
        updateAvatar()
    }

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        bottomBar = {
            SharedNavigationBar(
                navigateToNavigationBarItem = navigateToNavigationBarItem,
                selectedNavigationItemIndex = selectedNavigationItemIndex,
                updateNavigationBarSelectedIndex = updateNavigationItemIndex,
                avatarUrl = if (userProfileResult is UserProfileResult.Success) {
                    userProfileResult.userProfile.getUserImageUrl()
                } else ""
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            UserHeader(
                avatarUrl = if (userProfileResult is UserProfileResult.Success) {
                    userProfileResult.userProfile.getUserImageUrl()
                } else "",
                name = if (userProfileResult is UserProfileResult.Success) {
                    userProfileResult.userProfile.name
                } else ""
            )

            ReviewsItem()

            SettingsItem(
                navigateToSettings = navigateToSettings
            )

            ColumnElementDivider()

            LogOutItem(
                action = {
                    userProfileViewModel.logOut()
                }
            )
        }
    }

}

@Composable
private fun UserHeader(
    avatarUrl: String,
    name: String,
) {

    val surfaceColor = MaterialTheme.colorScheme.surface

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, surfaceColor),
                            startY = size.height - 40,
                            endY = size.height
                        ),
                        size = size
                    )
                }
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = painterResource(id = R.drawable.profile_header_background),
                contentDescription = stringResource(id = R.string.user_profile),
                contentScale = ContentScale.FillWidth
            )

        }
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentSize()
                .offset(
                    y = (-15).dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(80.dp)
                    .clip(
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = CircleShape
                    ),
                model = ImageRequest.Builder(LocalContext.current)
                    .dispatcher(Dispatchers.IO)
                    .data(avatarUrl)
                    .placeholder(R.drawable.user_image_placeholder)
                    .fallback(R.drawable.user_image_placeholder)
                    .error(R.drawable.user_image_placeholder)
                    .allowConversionToBitmap(true)
                    .crossfade(true)
                    .memoryCacheKey(avatarUrl)
                    .diskCacheKey(avatarUrl)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = name,
                contentScale = ContentScale.FillWidth,
                placeholder = painterResource(id = R.drawable.movie_placeholder)
            )
            Text(
                modifier = Modifier
                    .padding(
                        top = 8.dp
                    ),
                text = name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun LogOutItem(
    modifier: Modifier = Modifier,
    action: () -> Unit,
) {
    TextFieldClickableUnselected(
        modifier = modifier,
        value = stringResource(id = R.string.log_out),
        action = action,
        iconVector = Icons.AutoMirrored.Default.ExitToApp
    )
}

@Composable
private fun SettingsItem(
    modifier: Modifier = Modifier,
    navigateToSettings: () -> Unit,
) {
    TextFieldClickableUnselected(
        modifier = modifier,
        value = stringResource(id = R.string.settings),
        action = navigateToSettings,
        iconVector = Icons.Default.Settings
    )
}

//TODO: implement ReviewScreen and attach its navigation to this item
@Composable
private fun ReviewsItem(
    modifier: Modifier = Modifier,
) {
    TextFieldClickableUnselected(
        modifier = modifier,
        value = stringResource(id = R.string.reviews),
        action = {},
        iconVector = Icons.Default.Star
    )
}