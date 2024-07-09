package com.simplepeople.watcha.ui.auth.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplepeople.watcha.R
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
class SignUpViewModel @Inject constructor(
    private val credentialsUseCase: CredentialsUseCase
) : ViewModel(), SnackbarMessaging {

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState = _signUpState.asStateFlow()

    fun updateEmail(email: String) {
        _signUpState.value = _signUpState.value.copy(
            email = email,
            isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        )
    }

    fun updatePassword(password: String) {
        _signUpState.value = _signUpState.value.copy(
            password = password
        )
    }

    fun createPasswordCredential() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = credentialsUseCase.createPasswordCredential(
                email = _signUpState.value.email,
                password =  _signUpState.value.password
            )

            when (result) {
                true -> {
                    _signUpState.value = _signUpState.value.copy(
                        signUpResult = SignUpResult.Success(true)
                    )
                }
                false -> {
                    _signUpState.value = _signUpState.value.copy(
                        signUpResult = SignUpResult.Error(SignUpResult.ErrorType.SIGN_UP_NO_USER_FOUND_ERROR),
                        snackbarItem = SnackbarItem(
                            show = true,
                            isError = true,
                            message = R.string.sign_up_no_user_found_error
                        )
                    )
                }
                null -> {
                    _signUpState.value = _signUpState.value.copy(
                        signUpResult = SignUpResult.Error(SignUpResult.ErrorType.SIGN_UP_CREDENTIAL_ERROR),
                        snackbarItem = SnackbarItem(
                            show = true,
                            isError = true,
                            message = R.string.sign_up_credential_error
                        )
                    )
                }
            }
        }
    }

    override fun resetSnackbar () {
        _signUpState.value = _signUpState.value.copy(
            snackbarItem = SnackbarItem()
        )
    }
}