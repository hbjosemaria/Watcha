package com.simplepeople.watcha.data

import androidx.paging.PagingSource
import com.simplepeople.watcha.data.model.external.MovieListResponseDto
import com.simplepeople.watcha.data.model.external.MovieResponseDto
import com.simplepeople.watcha.data.model.local.MovieCategoryEntity
import com.simplepeople.watcha.data.model.local.MovieEntity
import com.simplepeople.watcha.data.model.local.MovieFavoriteEntity
import com.simplepeople.watcha.data.model.local.RemoteKeysEntity
import com.simplepeople.watcha.data.model.local.SearchLogItemEntity
import com.simplepeople.watcha.data.repository.CacheRepository
import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import com.simplepeople.watcha.data.repository.LocalMovieRepository
import com.simplepeople.watcha.data.repository.MixedMovieRepository
import com.simplepeople.watcha.data.repository.MovieCategoryRepository
import com.simplepeople.watcha.data.repository.MovieFavoriteRepository
import com.simplepeople.watcha.data.repository.RemoteKeysRepository
import com.simplepeople.watcha.data.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.Calendar
import java.util.TimeZone

class FakeExternalMovieRepositoryImpl : ExternalMovieRepository {

    override suspend fun getMovieById(movieId: Long, language: String): MovieResponseDto {
        val movie = FakeData.fakeMovieData.find { it.movieId == movieId }
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
        val filteredMovies = ArrayList(FakeData.fakeMovieDtoData.results.filter {
            it.title.contains(searchText, ignoreCase = true)
        })
        return FakeData.fakeMovieDtoData.copy(
            page = 1,
            results = filteredMovies,
            totalPages = 1,
            totalResults = filteredMovies.size
        )
    }

    override suspend fun getNowPlayingByPage(page: Int, language: String): MovieListResponseDto =
        FakeData.fakeMovieDtoData.copy(
            results = ArrayList(FakeData.fakeMovieDtoData.results.sortedBy {
                it.id
            })
        )

    override suspend fun getPopularByPage(page: Int, language: String): MovieListResponseDto =
        FakeData.fakeMovieDtoData.copy(
            results = ArrayList(FakeData.fakeMovieDtoData.results.sortedByDescending {
                it.popularity
            })
        )

    override suspend fun getTopRatedByPage(page: Int, language: String): MovieListResponseDto =
        FakeData.fakeMovieDtoData.copy(
            results = ArrayList(FakeData.fakeMovieDtoData.results.sortedByDescending {
                it.voteAverage
            })
        )

    override suspend fun getUpcomingByPage(page: Int, language: String): MovieListResponseDto =
        FakeData.fakeMovieDtoData.copy(
            results = ArrayList(FakeData.fakeMovieDtoData.results.sortedBy {
                it.releaseDate
            })
        )
}

class FakeLocalMovieRepositoryImpl : LocalMovieRepository {

    override fun getAllMovies(): PagingSource<Int, MovieEntity> =
        FakeLocalMoviePagingSource()

    override fun getByCategory(category: Int): PagingSource<Int, MovieEntity> =
        FakeLocalMoviePagingSource()

    override suspend fun getMovieById(movieId: Long): MovieEntity =
        FakeData.fakeMovieEntityData.find {
            it.movieId == movieId
        } ?: MovieEntity()

    override suspend fun addMovie(movie: MovieEntity): Long {
        FakeData.fakeMovieEntityData.plus(movie)
        return 1L
    }

    override suspend fun deleteMovie(movieId: Long): Int {
        FakeData.fakeMovieEntityData.dropWhile {
            it.movieId == movieId
        }
        return 1
    }

    override suspend fun insertAllMovies(movieList: List<MovieEntity>) {
        FakeData.fakeMovieEntityData.plus(movieList)
    }

    override suspend fun clearCachedMovies(): Int {
        FakeData.fakeMovieEntityData.drop(FakeData.fakeMovieEntityData.size)
        return 1
    }
}

class FakeMixedMovieRepositoryImpl : MixedMovieRepository {
    override suspend fun getMovieById(
        movieId: Long,
        language: String,
    ): Pair<MovieResponseDto, Int> {
        val movie = FakeData.fakeMovieEntityData.find {
            it.movieId == movieId
        } ?: MovieEntity()
        return Pair(
            MovieResponseDto(
                id = movie.movieId,
                title = movie.title,
                overview = movie.overview
            ),
            if (FakeData.fakeMovieFavoriteData.find {
                    it.movie.movieId == movieId
                } != null) 1 else 0
        )
    }
}

class FakeMovieFavoriteRepository : MovieFavoriteRepository {
    override fun getFavorites(): PagingSource<Int, MovieFavoriteEntity> =
        FakeMovieFavoritePagingSource()

    override suspend fun insertFavorite(favorite: MovieFavoriteEntity) {
        FakeData.fakeMovieFavoriteData.add(favorite)
    }

    override suspend fun removeFavorite(movieId: Long) {
        FakeData.fakeMovieFavoriteData.remove(
            FakeData.fakeMovieFavoriteData.find {
                it.movie.movieId == movieId
            }
        )
    }

    override suspend fun checkIfMovieIsFavorite(movieId: Long): Int =
        if (
            FakeData.fakeMovieFavoriteData.find {
                it.movie.movieId == movieId
            } != null
        ) 1 else 0

}

class FakeMovieCategoryRepositoryImpl : MovieCategoryRepository {
    override suspend fun insertAll(categories: List<MovieCategoryEntity>) {
        FakeData.fakeMovieCategoryData.addAll(categories)
    }

    override suspend fun clearAll() {
        FakeData.fakeMovieCategoryData.drop(FakeData.fakeMovieCategoryData.size)
    }
}

class FakeRemoteKeyRepositoryImpl : RemoteKeysRepository {
    override suspend fun insertAll(keys: List<RemoteKeysEntity>) {
        FakeData.fakeRemoteKeyData.addAll(keys)
    }

    override suspend fun getRemoteKey(movieId: Long, categoryId: Int): RemoteKeysEntity? =
        FakeData.fakeRemoteKeyData.find {
            it.movieId == movieId && it.categoryId == categoryId
        }


    override suspend fun clearRemoteKeys() {
        FakeData.fakeRemoteKeyData.drop(FakeData.fakeRemoteKeyData.size)
    }

}

class FakeCacheRepositoryImpl : CacheRepository {

    override val cacheVariableNameSuffix = "last_timeout_cache_category"

    override suspend fun clearMovieCacheExpiration() {
        FakeData.fakeCacheData.drop(FakeData.fakeCacheData.size)
    }

    override suspend fun forceCacheExpiration() {
        val cacheTimeout = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cacheTimeout.add(Calendar.DAY_OF_YEAR, -1)

        FakeData.fakeCacheData.map {
            it.copy(
                second = cacheTimeout.timeInMillis
            )
        }
    }

    override suspend fun loadMovieCacheExpiration(categoryId: Int): Long? =
        FakeData.fakeCacheData.find { (cacheVariableName, _) ->
            cacheVariableName == cacheVariableNameSuffix.plus(categoryId)
        }?.second

    override suspend fun saveMovieCacheExpiration(categoryId: Int, cacheExpiration: Long) {
        FakeData.fakeCacheData.add(
            Pair(cacheVariableNameSuffix.plus(categoryId), cacheExpiration)
        )
    }

}

class FakeSearchRepositoryImpl : SearchRepository {
    override fun getRecentSearch(): Flow<List<SearchLogItemEntity>> {
        val searchLog =
            if (FakeData.fakeSearchData.size >= 5) {
                FakeData.fakeSearchData.subList(
                    FakeData.fakeSearchData.size - 5, FakeData.fakeSearchData.size
                )
            } else FakeData.fakeSearchData
        return flowOf(searchLog)
    }

    override fun addNewSearch(searchLogItemEntity: SearchLogItemEntity): Long {
        val isSearchAdded = FakeData.fakeSearchData.add(searchLogItemEntity)
        return if (isSearchAdded) 1L else 0L
    }

    override fun removeSearch(searchLogItemEntity: SearchLogItemEntity): Int {
        val isSearchRemoved = FakeData.fakeSearchData.removeIf {
            it.searchedText == searchLogItemEntity.searchedText
        }
        return if (isSearchRemoved) 1 else 0
    }

    override fun cleanSearchLog(): Int {
        val isSearchLogCleaned = FakeData.fakeSearchData.removeAll { true }
        return if (isSearchLogCleaned) 1 else 0
    }

}

