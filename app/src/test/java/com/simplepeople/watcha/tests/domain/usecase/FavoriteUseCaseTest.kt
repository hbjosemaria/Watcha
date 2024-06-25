package com.simplepeople.watcha.tests.domain.usecase

import android.util.Log
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.google.common.truth.Truth.assertThat
import com.simplepeople.watcha.data.repository.MovieFavoriteRepository
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.usecase.FavoriteUseCase
import com.simplepeople.watcha.tests.data.FakeData
import com.simplepeople.watcha.tests.data.FakeMovieFavoriteRepository
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FavoriteUseCaseTest {

    private lateinit var fakeMovieFavoriteRepository: MovieFavoriteRepository
    private lateinit var favoriteUseCase: FavoriteUseCase

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.isLoggable(any(), any()) } returns false
        fakeMovieFavoriteRepository = FakeMovieFavoriteRepository()
        favoriteUseCase = FavoriteUseCase(fakeMovieFavoriteRepository)
    }

    @Test
    fun `Fetch favorite movies`() = runTest {
        val expectedFavoriteList = FakeData.fakeMovieFavoriteData.map {
            it.movie.toDomain()
        }
        val testPager = TestPager(
            PagingConfig(
                pageSize = 30
            ),
            fakeMovieFavoriteRepository.getFavorites()
        )

        val pagingSourceResult = testPager.refresh() as PagingSource.LoadResult.Page
        val favoriteList = pagingSourceResult.data.map {it.movie.toDomain()}

        assertThat(expectedFavoriteList).isEqualTo(favoriteList)
    }

    @Test
    fun `Save movie into favorites`() = runTest {
        val movie = Movie(
            movieId = 2369,
            title = "The ascension of the great Android Developer",
            overview = "This is the tale of one humble Android Developer who ascended to its peak expertise"
        )
        val previousList = FakeData.fakeMovieFavoriteData.toList()
        favoriteUseCase.saveFavorite(movie)
        assertThat(previousList).isNotEqualTo(FakeData.fakeMovieFavoriteData)
    }

    @Test
    fun `Remove movie from favorites`() = runTest {
        val movieFavorite = FakeData.fakeMovieFavoriteData.first()
        val previousList = FakeData.fakeMovieFavoriteData.toList()
        favoriteUseCase.deleteFavorite(movieFavorite.movie.movieId)
        assertThat(previousList).isNotEqualTo(FakeData.fakeMovieFavoriteData)

    }
}