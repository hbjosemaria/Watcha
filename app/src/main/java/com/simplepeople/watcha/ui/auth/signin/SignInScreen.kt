package com.simplepeople.watcha.ui.auth.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.common.composables.DefaultTextIconButton
import com.simplepeople.watcha.ui.common.composables.ImageWithMessage

@Composable
fun SignInScreen(
    signInViewModel: SignInViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
    navigateToSignUp: () -> Unit,
    navigateToAuth: () -> Unit,
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val loginState by signInViewModel.loginState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(loginState.loginResult, loginState.userHasSessionId) {
        if (loginState.loginResult == LoginResult.Success(true) && loginState.userHasSessionId) {
            navigateToHome()
        } else if (loginState.loginResult == LoginResult.Success(true) && !loginState.userHasSessionId) {
            navigateToAuth()
        }
    }

    LaunchedEffect(loginState.snackbarItem) {
        if (loginState.snackbarItem.show) {
            snackBarHostState.currentSnackbarData?.dismiss()
            snackBarHostState.showSnackbar(
                message = context.getString(loginState.snackbarItem.message),
                duration = SnackbarDuration.Short
            )
            signInViewModel.resetSnackbar()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .width(140.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(
                                bottom = 12.dp
                            ),
                        painter = painterResource(id = R.drawable.logo_main_screen),
                        contentDescription = stringResource(id = R.string.app_logo),
                        contentScale = ContentScale.Fit
                    )
                    ImageWithMessage(
                        modifier = Modifier,
                        image = R.drawable.login,
                        message = R.string.sign_in_header
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                    )
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = stringResource(id = R.string.sign_in_subheader),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        bottom = 40.dp
                    )
            ) {
                DefaultTextIconButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    action = { signInViewModel.signIn() },
                    iconImage = Icons.Filled.Key,
                    contentDescription = Icons.Filled.Key.name,
                    text = R.string.sign_in
                )

                DefaultTextIconButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    action = navigateToSignUp,
                    text = R.string.sign_up
                )
            }
        }
    }
}
