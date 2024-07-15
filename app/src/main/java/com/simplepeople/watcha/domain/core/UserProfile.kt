package com.simplepeople.watcha.domain.core

import com.simplepeople.watcha.data.model.local.UserProfileEntity

data class UserProfile(
    val avatar: Avatar = Avatar(),
    val name: String = "",
    val username: String = "",
) {
    data class Avatar(
        val gravatar: Gravatar = Gravatar(),
        val tmdbAvatar: TmdbAvatar = TmdbAvatar(),
    ) {
        data class Gravatar(
            val hash: String = "",
        ) {
            fun toEntity(): UserProfileEntity.AvatarEntity.GravatarEntity =
                UserProfileEntity.AvatarEntity.GravatarEntity(
                    hash = hash
                )
        }

        data class TmdbAvatar(
            val avatarPath: String? = "",
        ) {
            fun toEntity(): UserProfileEntity.AvatarEntity.TmdbAvatarEntity =
                UserProfileEntity.AvatarEntity.TmdbAvatarEntity(
                    avatarPath = avatarPath
                )
        }

        fun toEntity(): UserProfileEntity.AvatarEntity =
            UserProfileEntity.AvatarEntity(
                gravatar = gravatar.toEntity(),
                tmdbAvatar = tmdbAvatar.toEntity()
            )
    }

    companion object {
        var sessionId: String? = null
            private set

        fun setSessionId(newValue: String) {
            sessionId = newValue
        }
    }

    fun getUserImageUrl(): String =
        avatar.tmdbAvatar.avatarPath ?: avatar.gravatar.hash


    fun toEntity(): UserProfileEntity =
        UserProfileEntity(
            avatar = avatar.toEntity(),
            name = name,
            username = username,
            sessionId = sessionId ?: ""
        )
}