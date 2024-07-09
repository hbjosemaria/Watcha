package com.simplepeople.watcha.ui.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.usecase.AuthUseCase
import com.simplepeople.watcha.domain.usecase.CredentialsUseCase
import com.simplepeople.watcha.ui.common.SnackbarMessaging
import com.simplepeople.watcha.ui.common.utils.SnackbarItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val credentialsUseCase: CredentialsUseCase,
    private val authUseCase: AuthUseCase
) : ViewModel(), SnackbarMessaging {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    fun signIn() {
        viewModelScope.launch(
            context = Dispatchers.IO
        ) {
            when (credentialsUseCase.signIn()) {
                true -> {
                    _loginState.value = _loginState.value.copy(
                        loginResult = LoginResult.Success(true),
                        userHasSessionId = authUseCase.loadSessionId()
                    )
                }

                false -> {
                    _loginState.value = _loginState.value.copy(
                        loginResult = LoginResult.Error(LoginResult.ErrorType.SIGN_IN_ERROR),
                        snackbarItem = SnackbarItem(
                            show = true,
                            isError = true,
                            message = R.string.sign_in_error
                        )
                    )
                }

                null -> {
                    _loginState.value = _loginState.value.copy(
                        loginResult = LoginResult.Error(LoginResult.ErrorType.NO_CREDENTIALS_AVAILABLE_ERROR),
                        snackbarItem = SnackbarItem(
                            show = true,
                            isError = true,
                            message = R.string.sign_in_no_credentials_error
                        )
                    )
                }
            }

        }
    }

    override fun resetSnackbar() {
        _loginState.value = _loginState.value.copy(
            snackbarItem = SnackbarItem()
        )
    }
}