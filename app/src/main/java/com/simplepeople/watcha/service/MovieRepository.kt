package com.simplepeople.watcha.service

import com.simplepeople.watcha.data.local.RoomMovieServiceImpl
import com.simplepeople.watcha.data.tmdb.TmdbApiServiceImpl
import com.simplepeople.watcha.domain.repo.ExternalMovieRepository
import com.simplepeople.watcha.domain.repo.ExternalMovieRepositoryImpl
import com.simplepeople.watcha.domain.repo.LocalMovieRepository
import com.simplepeople.watcha.domain.repo.LocalMovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieRepository {

    @Provides
    @Singleton
    fun provideExternalMovieRepository(apiService: TmdbApiServiceImpl): ExternalMovieRepository {
        return ExternalMovieRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideLocalMovieRepository(apiService: RoomMovieServiceImpl): LocalMovieRepository {
        return LocalMovieRepositoryImpl(apiService)
    }

}