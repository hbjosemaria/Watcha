package com.simplepeople.watcha.ui.auth.signup

import com.simplepeople.watcha.ui.common.utils.SnackbarItem

data class SignUpState(
    val email : String = "",
    val isEmailValid : Boolean = false,
    val password : String = "",
    val signUpResult : SignUpResult = SignUpResult.Success(false),
    val snackbarItem: SnackbarItem = SnackbarItem()
)

sealed class SignUpResult {
    data class Success(val isSignedUp: Boolean = false) : SignUpResult()
    data class Error(val errorType: ErrorType) : SignUpResult()

    enum class ErrorType {
        SIGN_UP_NO_USER_FOUND_ERROR,
        SIGN_UP_CREDENTIAL_ERROR
    }
}

