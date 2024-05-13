package com.simplepeople.watcha.data.module

import android.content.Context
import androidx.room.Room
import com.simplepeople.watcha.data.services.MovieDao
import com.simplepeople.watcha.data.services.SearchLogDao
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
    fun provideDatabase(@ApplicationContext context: Context): WatchaDatabase {
        return Room
            .databaseBuilder(context, WatchaDatabase::class.java, "Watcha")
            .fallbackToDestructiveMigration()
            .build()


    }

    @Provides
    fun provideMovieDao(database: WatchaDatabase): MovieDao {
        return database.movieDao()
    }

    @Provides
    fun provideSearchItemDao(database: WatchaDatabase) : SearchLogDao {
        return database.searchLogDao()
    }

}

