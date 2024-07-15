package com.simplepeople.watcha.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplepeople.watcha.domain.usecase.UserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase,
) : ViewModel() {

    private val _appNavigationState = MutableStateFlow(AppNavigationState())
    val appNavigationState = _appNavigationState.asStateFlow()

    fun loadUserProfile() {
        viewModelScope.launch(
            context = Dispatchers.IO
        ) {
            try {
                _appNavigationState.value = _appNavigationState.value.copy(
                    userProfileResult = UserProfileResult.Success(
                        userProfile = userProfileUseCase.getUserProfile()
                            ?: userProfileUseCase.fetchAndSaveUserProfile()
                    )
                )
            } catch (e: Exception) {
                _appNavigationState.value = _appNavigationState.value.copy(
                    userProfileResult = UserProfileResult.Error(
                        error = UserProfileResult.TypeError.PROFILE_NOT_FOUND
                    )
                )
            }
        }
    }

    fun updateAvatar() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val userProfileResult = _appNavigationState.value.userProfileResult) {
                    is UserProfileResult.Error,
                    UserProfileResult.Loading,
                    -> {
                    }

                    is UserProfileResult.Success -> {
                        val updatedAvatar = userProfileUseCase.uploadAvatar()
                        if (updatedAvatar != null && userProfileResult.userProfile.avatar != updatedAvatar) {
                            val updatedUserProfile =
                                userProfileResult.userProfile.copy(avatar = updatedAvatar)
                            val updatedUserProfileResult = userProfileResult.copy(
                                userProfile = updatedUserProfile
                            )
                            _appNavigationState.value = _appNavigationState.value.copy(
                                userProfileResult = updatedUserProfileResult
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                //Add logs if correspond
            }
        }
    }

    fun updateNavigationBarIndex(newIndex: Int) {
        _appNavigationState.value = _appNavigationState.value.copy(
            selectedNavigationItemIndex = newIndex
        )
    }
}