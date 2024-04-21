package com.simplepeople.watcha.data.module

import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import com.simplepeople.watcha.data.repository.ExternalMovieRepositoryImpl
import com.simplepeople.watcha.data.repository.LocalMovieRepository
import com.simplepeople.watcha.data.repository.LocalMovieRepositoryImpl
import com.simplepeople.watcha.data.repository.MixedMovieRepository
import com.simplepeople.watcha.data.repository.MixedMovieRepositoryImpl
import com.simplepeople.watcha.data.services.MovieDao
import com.simplepeople.watcha.data.services.TmdbApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideExternalMovieRepository(apiService: TmdbApiService): ExternalMovieRepository {
        return ExternalMovieRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideLocalMovieRepository(apiService: MovieDao): LocalMovieRepository {
        return LocalMovieRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideMixedMovieRepository(roomService: MovieDao, apiService: TmdbApiService): MixedMovieRepository {
        return MixedMovieRepositoryImpl(roomService, apiService)
    }

}