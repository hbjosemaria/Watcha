package com.simplepeople.watcha.data.service.Room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.simplepeople.watcha.data.model.local.UserProfileEntity
import com.simplepeople.watcha.domain.core.Genre
import javax.inject.Singleton

@Singleton
class GenreConverter {
    @TypeConverter
    fun genresToJson(value: List<Genre>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToGenres(value: String): List<Genre> {
        return Gson().fromJson(value, object : TypeToken<List<Genre>>() {}.type)
    }
}

@Singleton
class AvatarConverter {
    @TypeConverter
    fun avatarToJson(value: UserProfileEntity.AvatarEntity): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToAvatar(value: String): UserProfileEntity.AvatarEntity {
        return Gson().fromJson(value, object : TypeToken<UserProfileEntity.AvatarEntity>() {}.type)
    }

    @TypeConverter
    fun gravatarToJson(value: UserProfileEntity.AvatarEntity.GravatarEntity): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToGravatar(value: String): UserProfileEntity.AvatarEntity.GravatarEntity {
        return Gson().fromJson(
            value,
            object : TypeToken<UserProfileEntity.AvatarEntity.GravatarEntity>() {}.type
        )
    }

    @TypeConverter
    fun tmdbAvatarToJson(value: UserProfileEntity.AvatarEntity.TmdbAvatarEntity): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToTmdbAvatar(value: String): UserProfileEntity.AvatarEntity.TmdbAvatarEntity {
        return Gson().fromJson(
            value,
            object : TypeToken<UserProfileEntity.AvatarEntity.TmdbAvatarEntity>() {}.type
        )
    }
}