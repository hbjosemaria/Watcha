package com.simplepeople.watcha.data.modules

import android.content.Context
import androidx.room.Room
import com.simplepeople.watcha.data.services.MovieCategoryDao
import com.simplepeople.watcha.data.services.MovieDao
import com.simplepeople.watcha.data.services.MovieFavoriteDao
import com.simplepeople.watcha.data.services.RemoteKeysDao
import com.simplepeople.watcha.data.services.SearchLogDao
import com.simplepeople.watcha.data.services.TmdbSessionIdDao
import com.simplepeople.watcha.data.services.WatchaDatabase
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

}

