package com.simplepeople.watcha.data

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.auth.FirebaseAuth
import com.simplepeople.watcha.data.model.external.MovieListResponseDto
import com.simplepeople.watcha.data.model.external.MovieResponseDto
import com.simplepeople.watcha.data.model.local.MovieEntity
import com.simplepeople.watcha.data.module.RepoModule
import com.simplepeople.watcha.data.repository.CacheRepository
import com.simplepeople.watcha.data.repository.CacheRepositoryImpl
import com.simplepeople.watcha.data.repository.CredentialRepository
import com.simplepeople.watcha.data.repository.CredentialRepositoryImpl
import com.simplepeople.watcha.data.repository.DataStoreRepository
import com.simplepeople.watcha.data.repository.DataStoreRepositoryImpl
import com.simplepeople.watcha.data.repository.ExternalAuthRepository
import com.simplepeople.watcha.data.repository.ExternalAuthRepositoryImpl
import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import com.simplepeople.watcha.data.repository.LocalAuthRepository
import com.simplepeople.watcha.data.repository.LocalAuthRepositoryImpl
import com.simplepeople.watcha.data.repository.LocalMovieRepository
import com.simplepeople.watcha.data.repository.MixedMovieRepository
import com.simplepeople.watcha.data.repository.MovieCategoryRepository
import com.simplepeople.watcha.data.repository.MovieCategoryRepositoryImpl
import com.simplepeople.watcha.data.repository.MovieFavoriteRepository
import com.simplepeople.watcha.data.repository.MovieFavoriteRepositoryImpl
import com.simplepeople.watcha.data.repository.RemoteKeyRepositoryImpl
import com.simplepeople.watcha.data.repository.RemoteKeysRepository
import com.simplepeople.watcha.data.repository.SearchRepository
import com.simplepeople.watcha.data.repository.SearchRepositoryImpl
import com.simplepeople.watcha.data.service.Room.MovieCategoryDao
import com.simplepeople.watcha.data.service.Room.MovieFavoriteDao
import com.simplepeople.watcha.data.service.Room.RemoteKeysDao
import com.simplepeople.watcha.data.service.Room.SearchLogDao
import com.simplepeople.watcha.data.service.Room.TmdbSessionIdDao
import com.simplepeople.watcha.data.service.TmdbExternalAuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Inject
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepoModule::class])
@Module
object FakeRepoModule {

    @Provides
    @Singleton
    fun provideExternalMovieRepository(): ExternalMovieRepository {
        return FakeExternalMovieRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideLocalMovieRepository(): LocalMovieRepository {
        return FakeLocalMovieRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideMixedMovieRepository(roomService: MovieFavoriteDao): MixedMovieRepository {
        return FakeMixedMovieRepositoryImpl(roomService)
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

private val fakeAPIMovieData = MovieListResponseDto(
    page = 1,
    results = ArrayList(
        (1..30).map {
            MovieListResponseDto.Results(
                id = it.toLong(),
                title = "Movie $it",
                overview = "Overview of movie $it",
                voteAverage = Math.random() * 1000,
                popularity = Math.random() * 100,
                releaseDate = "2024-${(Math.random() * 12).toInt()}-${(Math.random() * 30).toInt()}"
            )
        }
    ),
    totalResults = 30,
    totalPages = 1
)

private val fakeDaoMovieData = fakeAPIMovieData.toEntity()

class FakeExternalMovieRepositoryImpl : ExternalMovieRepository {

    override suspend fun getMovieById(movieId: Long, language: String): MovieResponseDto {
        val movie = fakeDaoMovieData.find { it.movieId == movieId }
        return movie.let {
            MovieResponseDto(
                id = it!!.movieId,
                title = it.title,
                overview = it.overview
            )
        }
    }

    override suspend fun getMoviesByTitle(
        searchText: String,
        page: Int,
        language: String,
    ): MovieListResponseDto {
        val filteredMovies = ArrayList(fakeAPIMovieData.results.filter {
            it.title.contains(searchText, ignoreCase = true)
        })
        return fakeAPIMovieData.copy(
            page = 1,
            results = filteredMovies,
            totalPages = 1,
            totalResults = filteredMovies.size
        )
    }


    override suspend fun getNowPlayingByPage(page: Int, language: String): MovieListResponseDto =
        fakeAPIMovieData.copy(
            results = ArrayList(fakeAPIMovieData.results.sortedBy {
                it.id
            })
        )

    override suspend fun getPopularByPage(page: Int, language: String): MovieListResponseDto =
        fakeAPIMovieData.copy(
            results = ArrayList(fakeAPIMovieData.results.sortedByDescending {
                it.popularity
            })
        )

    override suspend fun getTopRatedByPage(page: Int, language: String): MovieListResponseDto =
        fakeAPIMovieData.copy(
            results = ArrayList(fakeAPIMovieData.results.sortedByDescending {
                it.voteAverage
            })
        )

    override suspend fun getUpcomingByPage(page: Int, language: String): MovieListResponseDto =
        fakeAPIMovieData.copy(
            results = ArrayList(fakeAPIMovieData.results.sortedBy {
                it.releaseDate
            })
        )
}

class FakeLocalMovieRepositoryImpl : LocalMovieRepository {

    override fun getAllMovies(): PagingSource<Int, MovieEntity> =
        FakePagingSource()

    override fun getByCategory(category: Int): PagingSource<Int, MovieEntity> =
        FakePagingSource()

    override suspend fun getMovieById(movieId: Long): MovieEntity =
        fakeDaoMovieData.find {
            it.movieId == movieId
        } ?: MovieEntity()

    override suspend fun addMovie(movie: MovieEntity): Long {
        fakeDaoMovieData.plus(movie)
        return 1L
    }

    override suspend fun deleteMovie(movieId: Long): Int {
        fakeDaoMovieData.dropWhile {
            it.movieId == movieId
        }
        return 1
    }

    override suspend fun insertAllMovies(movieList: List<MovieEntity>) {
        fakeDaoMovieData.plus(movieList)
    }

    override suspend fun clearCachedMovies(): Int {
        fakeDaoMovieData.drop(fakeDaoMovieData.size)
        return 1
    }
}

class FakeMixedMovieRepositoryImpl @Inject constructor(
    private val favoriteDao: MovieFavoriteDao
) : MixedMovieRepository {
    override suspend fun getMovieById(
        movieId: Long,
        language: String,
    ): Pair<MovieResponseDto, Int> {
        val movie = fakeAPIMovieData.toEntity().find {
            it.movieId == movieId
        } ?: MovieEntity()
        return Pair(
            MovieResponseDto(
                id = movie.movieId,
                title = movie.title,
                overview = movie.overview
            ),
            favoriteDao.checkIfMovieIsFavorite(movie.movieId)
        )
    }
}

private class FakePagingSource() : PagingSource<Int, MovieEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieEntity> {
        val position = params.key ?: 0
        return try {
            val loadSize = params.loadSize
            val start = position * loadSize
            val end = minOf(start + loadSize, fakeDaoMovieData.size)
            val sublist = fakeDaoMovieData.shuffled().subList(start, end)

            LoadResult.Page(
                data = sublist,
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (end == fakeDaoMovieData.size) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}