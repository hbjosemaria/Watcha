package com.simplepeople.watcha.ui.auth.auth

import com.simplepeople.watcha.ui.common.utils.SnackbarItem

data class AuthState (
    val authResult : AuthResult = AuthResult.Pending,
    val requestToken : String = "",
    val isTokenAuthorized: Boolean = false,
    val isSessionIdStored: Boolean = false,
    val snackbarItem: SnackbarItem = SnackbarItem()
)

sealed class AuthResult {
    data object Pending : AuthResult()
    data class Success(val requestTokenUrl: String) : AuthResult()
    data class Error(val errorType: ErrorType) : AuthResult()

    enum class ErrorType {
        URL_BUILD_FAILED
    }
}