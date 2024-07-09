package com.simplepeople.watcha.ui.auth.signin

import com.simplepeople.watcha.ui.common.utils.SnackbarItem

data class LoginState (
    val loginResult : LoginResult = LoginResult.Success(false),
    val snackbarItem : SnackbarItem = SnackbarItem(),
    val userHasSessionId: Boolean = false
)

sealed class LoginResult {
    data class Success(val isUserSignedIn : Boolean = false) : LoginResult()
    data class Error(val errorType : ErrorType) : LoginResult()

    enum class ErrorType {
        NO_CREDENTIALS_AVAILABLE_ERROR,
        SIGN_IN_ERROR
    }
}