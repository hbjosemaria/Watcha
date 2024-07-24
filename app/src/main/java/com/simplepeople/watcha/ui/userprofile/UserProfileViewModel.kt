package com.simplepeople.watcha.ui.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplepeople.watcha.domain.usecase.CacheUseCase
import com.simplepeople.watcha.domain.usecase.CredentialsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val cacheUseCase: CacheUseCase,
    private val credentialsUseCase: CredentialsUseCase,
) : ViewModel() {

    private val _userProfileState = MutableStateFlow(UserProfileState())
    val userProfileState = _userProfileState.asStateFlow()

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            _userProfileState.value = _userProfileState.value.copy(
                isLoggedOut = true
            )
            cacheUseCase.forceCacheExpiration()
            credentialsUseCase.logOut()
        }
    }

    fun deleteAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            _userProfileState.value = _userProfileState.value.copy(
                isLoggedOut = true
            )
            cacheUseCase.forceCacheExpiration()
            credentialsUseCase.deleteAccount()
        }
    }
}