package com.simplepeople.watcha.ui.navigation

import com.simplepeople.watcha.domain.core.UserProfile

data class AppNavigationState(
    val selectedNavigationItemIndex: Int = 0,
    val userProfileResult: UserProfileResult = UserProfileResult.Loading,
)

sealed class UserProfileResult {
    data object Loading : UserProfileResult()
    data class Success(val userProfile: UserProfile) : UserProfileResult()
    data class Error(val error: TypeError) : UserProfileResult()

    enum class TypeError {
        PROFILE_NOT_FOUND
    }
}