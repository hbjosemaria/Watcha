package com.simplepeople.watcha.domain.usecase

import com.simplepeople.watcha.data.model.local.UserProfileEntity
import com.simplepeople.watcha.data.repository.UserProfileExternalRepository
import com.simplepeople.watcha.data.repository.UserProfileLocalRepository
import com.simplepeople.watcha.domain.core.UserProfile
import javax.inject.Inject

class UserProfileUseCase @Inject constructor(
    private val userProfileExternalRepository: UserProfileExternalRepository,
    private val userProfileLocalRepository: UserProfileLocalRepository,
) {
    suspend fun fetchAndSaveUserProfile(): UserProfile {
        val userProfile = userProfileExternalRepository.fetchUserProfile()
            .toEntity()
            .copy(
                sessionId = UserProfile.sessionId ?: ""
            )
        saveUserProfile(userProfile = userProfile)
        return userProfile.toDomain()
    }

    private suspend fun saveUserProfile(userProfile: UserProfileEntity) {
        userProfileLocalRepository.saveUserProfile(userProfile)
    }

    suspend fun getUserProfile(): UserProfile? =
        userProfileLocalRepository.getUserProfile()?.toDomain()

    suspend fun uploadAvatar(): UserProfile.Avatar? {
        return try {
            val avatar = userProfileExternalRepository.fetchUserProfile()
                .toDomain().avatar

            userProfileLocalRepository.updateAvatar(
                avatar = avatar.toEntity(),
                sessionId = UserProfile.sessionId
            )

            avatar
        } catch (e: Exception) {
            null
        }

    }
}