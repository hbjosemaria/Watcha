package com.simplepeople.watcha.data.model.external

import com.google.gson.annotations.SerializedName
import com.simplepeople.watcha.data.model.local.UserProfileEntity
import com.simplepeople.watcha.data.service.TmdbApiUrl
import com.simplepeople.watcha.domain.core.UserProfile

data class UserProfileDto(
    @SerializedName("avatar")
    val avatar: AvatarDto = AvatarDto(),
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("iso_639_1")
    val iso_639_1: String = "",
    @SerializedName("iso_3166_1")
    val iso_3166_1: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("include_adult")
    val includeAdult: Boolean = false,
    @SerializedName("username")
    val username: String = "",
) {
    data class AvatarDto(
        @SerializedName("gravatar")
        val gravatar: GravatarDto = GravatarDto(),
        @SerializedName("tmdb")
        val tmdbAvatar: TmdbAvatarDto = TmdbAvatarDto(),
    ) {
        data class GravatarDto(
            @SerializedName("hash")
            val hash: String = "",
        ) {
            fun toEntity(): UserProfileEntity.AvatarEntity.GravatarEntity =
                UserProfileEntity.AvatarEntity.GravatarEntity(
                    hash = if (hash.isNotEmpty()) {
                        TmdbApiUrl.PROFILE_GRAVATAR_IMG_URL.url.replace("{hash}", hash)
                    } else hash
                )

            fun toDomain(): UserProfile.Avatar.Gravatar =
                UserProfile.Avatar.Gravatar(
                    hash = if (hash.isNotEmpty()) {
                        TmdbApiUrl.PROFILE_GRAVATAR_IMG_URL.url.replace("{hash}", hash)
                    } else hash
                )
        }

        data class TmdbAvatarDto(
            @SerializedName("avatar_path")
            val avatarPath: String? = "",
        ) {
            fun toEntity(): UserProfileEntity.AvatarEntity.TmdbAvatarEntity =
                UserProfileEntity.AvatarEntity.TmdbAvatarEntity(
                    avatarPath = avatarPath?.let {
                        TmdbApiUrl.PROFILE_TMDB_IMG_URL.url.plus(it)
                    }
                )

            fun toDomain(): UserProfile.Avatar.TmdbAvatar =
                UserProfile.Avatar.TmdbAvatar(
                    avatarPath = avatarPath?.let {
                        TmdbApiUrl.PROFILE_TMDB_IMG_URL.url.plus(it)
                    }
                )
        }

        fun toEntity(): UserProfileEntity.AvatarEntity =
            UserProfileEntity.AvatarEntity(
                gravatar = gravatar.toEntity(),
                tmdbAvatar = tmdbAvatar.toEntity()
            )

        fun toDomain(): UserProfile.Avatar =
            UserProfile.Avatar(
                gravatar = gravatar.toDomain(),
                tmdbAvatar = tmdbAvatar.toDomain()
            )
    }

    fun toEntity(): UserProfileEntity =
        UserProfileEntity(
            avatar = avatar.toEntity(),
            name = name,
            username = username
        )

    fun toDomain(): UserProfile =
        UserProfile(
            avatar = avatar.toDomain(),
            name = name,
            username = username
        )
}