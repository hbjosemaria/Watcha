package com.simplepeople.watcha.tests.domain.usecase

import android.util.Log
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.google.common.truth.Truth.assertThat
import com.simplepeople.watcha.data.remotemediator.MovieRemoteMediator
import com.simplepeople.watcha.data.repository.CacheRepository
import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import com.simplepeople.watcha.data.repository.LocalMovieRepository
import com.simplepeople.watcha.data.repository.MovieCategoryRepository
import com.simplepeople.watcha.data.repository.RemoteKeysRepository
import com.simplepeople.watcha.data.services.WatchaDatabase
import com.simplepeople.watcha.domain.usecase.MovieListUseCase
import com.simplepeople.watcha.tests.data.FakeCacheRepositoryImpl
import com.simplepeople.watcha.tests.data.FakeData
import com.simplepeople.watcha.tests.data.FakeExternalMoviePagingSource
import com.simplepeople.watcha.tests.data.FakeExternalMovieRepositoryImpl
import com.simplepeople.watcha.tests.data.FakeLocalMovieRepositoryImpl
import com.simplepeople.watcha.tests.data.FakeMovieCategoryRepositoryImpl
import com.simplepeople.watcha.tests.data.FakeMovieRemoteMediator
import com.simplepeople.watcha.tests.data.FakeRemoteKeyRepositoryImpl
import com.simplepeople.watcha.ui.common.composables.topbar.HomeFilterOptions
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MovieListUseCaseTest {

    private lateinit var movieListUseCase: MovieListUseCase
    private lateinit var movieRemoteMediator: FakeMovieRemoteMediator.FakeMovieRemoteMediatorFactory
    private lateinit var fakeLocalMovieRepository: LocalMovieRepository
    private lateinit var fakeExternalMovieRepository: ExternalMovieRepository
    private lateinit var fakeRemoteKeysRepository: RemoteKeysRepository
    private lateinit var fakeMovieCategoryRepository: MovieCategoryRepository
    private lateinit var fakeCacheRepository: CacheRepository
    private lateinit var database : WatchaDatabase


    private val filterOption = HomeFilterOptions.NowPlaying

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.isLoggable(any(), any()) } returns false
        movieRemoteMediator = mockk()
        database = mockk()

        fakeLocalMovieRepository = FakeLocalMovieRepositoryImpl()
        fakeExternalMovieRepository = FakeExternalMovieRepositoryImpl()
        fakeRemoteKeysRepository = FakeRemoteKeyRepositoryImpl()
        fakeMovieCategoryRepository = FakeMovieCategoryRepositoryImpl()
        fakeCacheRepository = FakeCacheRepositoryImpl()

        every {movieRemoteMediator.create(filterOption)} returns MovieRemoteMediator(
            apiService = fakeExternalMovieRepository,
            localMovieRepository = fakeLocalMovieRepository,
            remoteKeysRepository = fakeRemoteKeysRepository,
            movieCategoryRepository = fakeMovieCategoryRepository,
            cacheRepository = fakeCacheRepository,
            filterOption = filterOption,
            database = database
        )

        movieListUseCase = MovieListUseCase(
            apiService = fakeExternalMovieRepository,
            roomService = fakeLocalMovieRepository,
            movieRemoteMediator = movieRemoteMediator
        )
    }

    @Test
    fun `Fetch movies from source with RemoteMediator`() = runTest {
        val movieList = movieListUseCase.getMovies(filterOption).first()
        assertThat(movieList).isNotNull()
    }

    @Test
    fun `Search movies by title with successful result`() = runTest {
        val title = "Movie 6"
        val testPager = TestPager(
            config = PagingConfig(
                pageSize = 30
            ),
            pagingSource = FakeExternalMoviePagingSource(title)
        )
        val pagingSourceResult = testPager.refresh() as PagingSource.LoadResult.Page
        val expectedFilteredMovieList = FakeData.fakeMovieData.filter{it.title.contains(title)}
        val filteredMovieList = pagingSourceResult.data
        assertThat(expectedFilteredMovieList).isEqualTo(filteredMovieList)
    }

    @Test
    fun `Search movies by title with failed result`() = runTest {
        val title = "I'm an alien"
        val testPager = TestPager(
            config = PagingConfig(
                pageSize = 30
            ),
            pagingSource = FakeExternalMoviePagingSource(title)
        )
        val pagingSourceResult = testPager.refresh() as PagingSource.LoadResult.Page
        val filteredMovieList = pagingSourceResult.data
        assertThat(filteredMovieList).isEmpty()
    }
}