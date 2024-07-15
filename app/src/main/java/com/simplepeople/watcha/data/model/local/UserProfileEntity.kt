package com.simplepeople.watcha.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.simplepeople.watcha.data.service.Room.AvatarConverter
import com.simplepeople.watcha.domain.core.UserProfile

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val username: String = "",
    val name: String = "",
    @TypeConverters(AvatarConverter::class)
    val avatar: AvatarEntity = AvatarEntity(),
    val sessionId: String = "",
) {
    data class AvatarEntity(
        @TypeConverters(AvatarConverter::class)
        val gravatar: GravatarEntity = GravatarEntity(),
        @TypeConverters(AvatarConverter::class)
        val tmdbAvatar: TmdbAvatarEntity = TmdbAvatarEntity(),
    ) {
        data class GravatarEntity(
            val hash: String = "",
        ) {
            fun toDomain(): UserProfile.Avatar.Gravatar =
                UserProfile.Avatar.Gravatar(
                    hash = hash
                )

        }

        data class TmdbAvatarEntity(
            val avatarPath: String? = "",
        ) {
            fun toDomain(): UserProfile.Avatar.TmdbAvatar =
                UserProfile.Avatar.TmdbAvatar(
                    avatarPath = avatarPath
                )
        }

        fun toDomain(): UserProfile.Avatar =
            UserProfile.Avatar(
                gravatar = gravatar.toDomain(),
                tmdbAvatar = tmdbAvatar.toDomain()
            )

    }

    fun toDomain(): UserProfile {
        return UserProfile(
            avatar = avatar.toDomain(),
            name = name,
            username = username
        )
    }
}