package com.simplepeople.watcha.data

import android.content.Context
import androidx.room.Room
import com.simplepeople.watcha.data.modules.RoomModule
import com.simplepeople.watcha.data.services.MovieCategoryDao
import com.simplepeople.watcha.data.services.MovieDao
import com.simplepeople.watcha.data.services.MovieFavoriteDao
import com.simplepeople.watcha.data.services.RemoteKeysDao
import com.simplepeople.watcha.data.services.SearchLogDao
import com.simplepeople.watcha.data.services.WatchaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RoomModule::class])
@Module
object FakeRoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WatchaDatabase =
        Room
            .inMemoryDatabaseBuilder(context, WatchaDatabase::class.java)
            .allowMainThreadQueries()
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

}
