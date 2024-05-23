package com.simplepeople.watcha.data.module

import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import com.simplepeople.watcha.data.repository.ExternalMovieRepositoryImpl
import com.simplepeople.watcha.data.repository.LocalMovieRepository
import com.simplepeople.watcha.data.repository.LocalMovieRepositoryImpl
import com.simplepeople.watcha.data.repository.MixedMovieRepository
import com.simplepeople.watcha.data.repository.MixedMovieRepositoryImpl
import com.simplepeople.watcha.data.repository.MovieCategoryRepository
import com.simplepeople.watcha.data.repository.MovieCategoryRepositoryImpl
import com.simplepeople.watcha.data.repository.MovieFavoriteRepository
import com.simplepeople.watcha.data.repository.MovieFavoriteRepositoryImpl
import com.simplepeople.watcha.data.repository.RemoteKeyRepositoryImpl
import com.simplepeople.watcha.data.repository.RemoteKeysRepository
import com.simplepeople.watcha.data.repository.SearchRepository
import com.simplepeople.watcha.data.repository.SearchRepositoryImpl
import com.simplepeople.watcha.data.services.MovieCategoryDao
import com.simplepeople.watcha.data.services.MovieDao
import com.simplepeople.watcha.data.services.MovieFavoriteDao
import com.simplepeople.watcha.data.services.RemoteKeysDao
import com.simplepeople.watcha.data.services.SearchLogDao
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
    fun provideMixedMovieRepository(
        roomService: MovieFavoriteDao,
        apiService: TmdbApiService
    ): MixedMovieRepository {
        return MixedMovieRepositoryImpl(roomService, apiService)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(roomService: SearchLogDao): SearchRepository {
        return SearchRepositoryImpl(roomService)
    }

    @Provides
    @Singleton
    fun provideRemoteKeyRepository(roomService: RemoteKeysDao): RemoteKeysRepository {
        return RemoteKeyRepositoryImpl(roomService)
    }

    @Provides
    @Singleton
    fun provideMovieFavoriteRepository(roomService: MovieCategoryDao): MovieCategoryRepository {
        return MovieCategoryRepositoryImpl(roomService)
    }

    @Provides
    @Singleton
    fun provideMovieCategoryRepository(roomService: MovieFavoriteDao): MovieFavoriteRepository {
        return MovieFavoriteRepositoryImpl(roomService)
    }

}