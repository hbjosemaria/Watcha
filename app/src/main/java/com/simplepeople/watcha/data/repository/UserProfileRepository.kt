package com.simplepeople.watcha.data.repository

import com.simplepeople.watcha.data.model.external.UserProfileDto
import com.simplepeople.watcha.data.model.local.UserProfileEntity
import com.simplepeople.watcha.data.service.Room.UserProfileDao
import com.simplepeople.watcha.data.service.TmdbUserProfileService
import com.simplepeople.watcha.domain.core.UserProfile
import javax.inject.Inject

interface UserProfileExternalRepository {
    suspend fun fetchUserProfile(sessionId: String? = UserProfile.sessionId): UserProfileDto
}

interface UserProfileLocalRepository {
    suspend fun getUserProfile(sessionId: String? = UserProfile.sessionId): UserProfileEntity?
    suspend fun saveUserProfile(userProfile: UserProfileEntity): Long
    suspend fun updateAvatar(avatar: UserProfileEntity.AvatarEntity, sessionId: String?)
}

class UserProfileRepositoryImpl @Inject constructor(
    private val roomService: UserProfileDao,
    private val apiService: TmdbUserProfileService,
) : UserProfileLocalRepository, UserProfileExternalRepository {

    override suspend fun fetchUserProfile(sessionId: String?): UserProfileDto =
        apiService.fetchUserProfile(sessionId)

    override suspend fun getUserProfile(sessionId: String?): UserProfileEntity? =
        roomService.getUserProfile(sessionId)

    override suspend fun saveUserProfile(userProfile: UserProfileEntity): Long =
        roomService.saveUserProfile(userProfile)

    override suspend fun updateAvatar(
        avatar: UserProfileEntity.AvatarEntity,
        sessionId: String?,
    ) {
        roomService.updateAvatar(avatar, sessionId)
    }
}