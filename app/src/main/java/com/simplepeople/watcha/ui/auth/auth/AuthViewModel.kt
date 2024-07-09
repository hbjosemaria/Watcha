package com.simplepeople.watcha.ui.auth.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplepeople.watcha.R
import com.simplepeople.watcha.domain.usecase.AuthUseCase
import com.simplepeople.watcha.ui.common.SnackbarMessaging
import com.simplepeople.watcha.ui.common.utils.SnackbarItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
) : ViewModel(), SnackbarMessaging {

    private val _authState = MutableStateFlow(AuthState())
    val authState = _authState.asStateFlow()

    init {
        initializeRequestUrl()
    }

    private fun initializeRequestUrl() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestToken = authUseCase.getRequestToken()
                _authState.value = _authState.value.copy(
                    requestToken = requestToken,
                    authResult = AuthResult.Success(
                        requestTokenUrl = authUseCase.buildRequestTokenUrl(requestToken)
                    )
                )
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    authResult = AuthResult.Error(AuthResult.ErrorType.URL_BUILD_FAILED),
                    snackbarItem = SnackbarItem(
                        show = true,
                        isError = true,
                        message = R.string.auth_request_token_failed
                    )
                )
            }
        }
    }

    fun checkIfTokenIsAuthorized(url: String) {
        if (authUseCase.checkIfTokenIsAuthorized(url)) {
            _authState.value = _authState.value.copy(
                isTokenAuthorized = true,
                snackbarItem = SnackbarItem(
                    show = true,
                    message = R.string.auth_session_being_created
                )
            )
        }
    }

    fun createSession() {
        viewModelScope.launch {
            if (authUseCase.createSession(_authState.value.requestToken)) {
                _authState.value = _authState.value.copy(
                    isSessionIdStored = true,
                    snackbarItem = SnackbarItem(
                        show = true,
                        message = R.string.auth_session_created
                    )
                )
            } else {
                _authState.value = _authState.value.copy(
                    snackbarItem = SnackbarItem(
                        show = true,
                        isError = true,
                        message = R.string.auth_session_not_created
                    )
                )
            }


        }
    }

    override fun resetSnackbar() {
        _authState.value = _authState.value.copy(
            snackbarItem = SnackbarItem()
        )
    }
}