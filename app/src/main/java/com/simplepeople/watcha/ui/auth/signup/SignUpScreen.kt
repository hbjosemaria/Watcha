package com.simplepeople.watcha.ui.auth.signup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.common.composables.topbar.SingleScreenTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navigateToAuth: () -> Unit,
    navigateBack: () -> Unit,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val signUpState by signUpViewModel.signUpState.collectAsState()

    LaunchedEffect(signUpState.signUpResult) {
        if (signUpState.signUpResult == SignUpResult.Success(true)) {
            navigateToAuth()
        }
    }

    LaunchedEffect(signUpState.snackbarItem) {
        if (signUpState.snackbarItem.show) {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = context.getString(signUpState.snackbarItem.message),
                duration = SnackbarDuration.Short
            )
            signUpViewModel.resetSnackbar()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SingleScreenTopAppBar(
                scrollBehavior = scrollBehavior,
                navigateBack = navigateBack,
                screenTitleResource = R.string.sign_up_screen
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(
                        start = 24.dp,
                        end = 24.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmailField(
                    email = signUpState.email,
                    isEmailValid = signUpState.isEmailValid,
                    updateEmail = { newValue ->
                        signUpViewModel.updateEmail(newValue)
                    }
                )

                PasswordField(
                    password = signUpState.password,
                    updatePassword = { newValue ->
                        signUpViewModel.updatePassword(newValue)
                    }
                )

                Button(
                    modifier = Modifier
                        .padding(
                            top = 16.dp
                        ),
                    onClick = {
                        keyboardController?.hide()
                        signUpViewModel.createPasswordCredential()
                    },
                    shape = ButtonDefaults.filledTonalShape,
                    enabled = signUpState.isEmailValid
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_up_account)
                    )
                }

            }
        }
    }
}

@Composable
private fun EmailField(
    email : String,
    isEmailValid : Boolean,
    updateEmail: (String) -> Unit
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 6.dp
            )
            .clip(
                shape = ShapeDefaults.Small
            ),
        value = email,
        onValueChange = { newValue: String ->
            updateEmail(newValue)
        },
        maxLines = 1,
        label = {
            Text(
                text = stringResource(id = R.string.sign_up_email_label)
            )
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.sign_up_email_placeholder)
            )
        },
        trailingIcon = {
            if (email.isNotBlank()) {
                Icon(
                    imageVector = if (isEmailValid) Icons.Default.Check else Icons.Default.Error,
                    contentDescription = if (isEmailValid) Icons.Default.Check.name else Icons.Default.Error.name
                )
            }
        },
        isError = !isEmailValid,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = Icons.Default.Email.name
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
private fun PasswordField(
    password: String,
    updatePassword: (String) -> Unit,
) {
    var showPassword by remember { mutableStateOf(false) }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                shape = ShapeDefaults.Small
            ),
        value = password,
        onValueChange = { newValue: String ->
            updatePassword(newValue)
        },
        maxLines = 1,
        label = {
            Text(
                text = stringResource(id = R.string.sign_up_password_label)
            )
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.sign_up_password_placeholder)
            )
        },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconToggleButton(
                checked = showPassword,
                onCheckedChange = {
                    showPassword = !showPassword
                }
            ) {
                Icon(
                    imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (showPassword) Icons.Default.Visibility.name else Icons.Default.VisibilityOff.name
                )
            }
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Password,
                contentDescription = Icons.Default.Password.name
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )
    )
}