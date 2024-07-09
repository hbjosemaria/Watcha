package com.simplepeople.watcha.data.modules

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.auth.FirebaseAuth
import com.simplepeople.watcha.data.repository.CacheRepository
import com.simplepeople.watcha.data.repository.CacheRepositoryImpl
import com.simplepeople.watcha.data.repository.CredentialRepository
import com.simplepeople.watcha.data.repository.CredentialRepositoryImpl
import com.simplepeople.watcha.data.repository.DataStoreRepository
import com.simplepeople.watcha.data.repository.DataStoreRepositoryImpl
import com.simplepeople.watcha.data.repository.ExternalAuthRepository
import com.simplepeople.watcha.data.repository.ExternalAuthRepositoryImpl
import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import com.simplepeople.watcha.data.repository.ExternalMovieRepositoryImpl
import com.simplepeople.watcha.data.repository.LocalAuthRepository
import com.simplepeople.watcha.data.repository.LocalAuthRepositoryImpl
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
import com.simplepeople.watcha.data.services.TmdbExternalAuthService
import com.simplepeople.watcha.data.services.TmdbMovieService
import com.simplepeople.watcha.data.services.TmdbSessionIdDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideExternalMovieRepository(apiService: TmdbMovieService): ExternalMovieRepository {
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
        roomService: MovieFavoriteDao, apiService: TmdbMovieService,
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

    @Provides
    @Singleton
    fun provideDataStoreRepository(dataStore: DataStore<Preferences>): DataStoreRepository {
        return DataStoreRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideCacheRepository(dataStore: DataStore<Preferences>): CacheRepository {
        return CacheRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideCredentialsRepository(
        @ApplicationContext context: Context,
        credentialManager: CredentialManager,
        firebaseAuth: FirebaseAuth,
    ): CredentialRepository {
        return CredentialRepositoryImpl(
            context = context,
            credentialManager = credentialManager,
            firebaseAuth = firebaseAuth
        )
    }

    @Provides
    @Singleton
    fun provideLocalAuthRepository(
        roomService: TmdbSessionIdDao,
        firebaseAuth: FirebaseAuth,
    ): LocalAuthRepository {
        return LocalAuthRepositoryImpl(roomService, firebaseAuth = firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideExternalAuthRepository(apiService: TmdbExternalAuthService): ExternalAuthRepository {
        return ExternalAuthRepositoryImpl(apiService)
    }
}