package com.simplepeople.watcha.data.module

import android.content.Context
import androidx.room.Room
import com.simplepeople.watcha.data.service.Room.MovieCategoryDao
import com.simplepeople.watcha.data.service.Room.MovieDao
import com.simplepeople.watcha.data.service.Room.MovieFavoriteDao
import com.simplepeople.watcha.data.service.Room.RemoteKeysDao
import com.simplepeople.watcha.data.service.Room.SearchLogDao
import com.simplepeople.watcha.data.service.Room.TmdbSessionIdDao
import com.simplepeople.watcha.data.service.Room.UserProfileDao
import com.simplepeople.watcha.data.service.Room.WatchaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WatchaDatabase =
        Room
            .databaseBuilder(context, WatchaDatabase::class.java, "Watcha")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideMovieDao(database: WatchaDatabase): MovieDao =
        database.movieDao()

    @Provides
    fun provideSearchItemDao(database: WatchaDatabase): SearchLogDao =
        database.searchLogDao()

    @Provides
    fun provideRemoteKeysDao(database: WatchaDatabase): RemoteKeysDao =
        database.remoteKeysDao()

    @Provides
    fun provideMovieCategoryDao(database: WatchaDatabase): MovieCategoryDao =
        database.movieCategoryDao()

    @Provides
    fun provideMovieFavoriteDao(database: WatchaDatabase): MovieFavoriteDao =
        database.movieFavoriteDao()

    @Provides
    fun provideTmdbSessionIdDao(database: WatchaDatabase): TmdbSessionIdDao =
        database.tmdbSessionIdDao()

    @Provides
    fun provideUserProfileDao(database: WatchaDatabase): UserProfileDao =
        database.userProfileDao()

}

