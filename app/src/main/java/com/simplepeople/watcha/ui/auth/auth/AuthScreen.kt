package com.simplepeople.watcha.ui.auth.auth

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.common.composables.DefaultTextIconButton
import com.simplepeople.watcha.ui.common.composables.ImageWithMessage
import com.simplepeople.watcha.ui.common.composables.LoadingIndicator
import com.simplepeople.watcha.ui.common.composables.topbar.SingleScreenTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
    navigateBack: () -> Unit,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()
    var isExplanationRead by remember { mutableStateOf(false) }

    LaunchedEffect(authState.snackbarItem) {
        if (authState.snackbarItem.show) {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = context.getString(authState.snackbarItem.message),
                duration = SnackbarDuration.Short
            )
            authViewModel.resetSnackbar()
        }
    }

    LaunchedEffect(authState.isTokenAuthorized) {
        if (authState.isTokenAuthorized) {
            authViewModel.createSession()
        }
    }

    LaunchedEffect(authState.isSessionIdStored) {
        if (authState.isSessionIdStored) {
            navigateToHome()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            SingleScreenTopAppBar(
                navigateBack = navigateBack,
                screenTitleResource = R.string.auth_screen,
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                !isExplanationRead -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier
                                .width(240.dp)
                                .padding(
                                    bottom = 20.dp
                                ),
                            painter = painterResource(id = R.drawable.watcha_and_tmdb_sync),
                            contentDescription = stringResource(id = R.string.watcha_and_tmdb_sync)
                        )
                        ImageWithMessage(
                            modifier = Modifier,
                            image = R.drawable.tmdb_sync
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = stringResource(id = R.string.auth_explanation),
                            textAlign = TextAlign.Center
                        )
                    }
                    DefaultTextIconButton(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(
                                bottom = 40.dp
                            ),
                        action = { isExplanationRead = true },
                        text =  R.string.auth_explanation_button
                    )
                }

                isExplanationRead && !authState.isTokenAuthorized -> {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxSize(),
                        factory = { context ->
                            WebView(context).apply {
                                settings.javaScriptEnabled = true
                                webViewClient = object : WebViewClient() {
                                    override fun shouldOverrideUrlLoading(
                                        view: WebView?,
                                        request: WebResourceRequest?,
                                    ): Boolean {
                                        request?.let {
                                            val url = it.url.toString()
                                            authViewModel.checkIfTokenIsAuthorized(url)
                                            view?.loadUrl(url)
                                        }
                                        return super.shouldOverrideUrlLoading(view, request)
                                    }
                                }
                            }
                        },
                        update = { webView ->
                            when (val state = authState.authResult) {
                                is AuthResult.Error,
                                AuthResult.Pending,
                                -> {
                                    webView.stopLoading()
                                }

                                is AuthResult.Success -> {
                                    webView.loadUrl(state.requestTokenUrl)
                                }
                            }
                        }
                    )
                }

                else -> {
                    LoadingIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}