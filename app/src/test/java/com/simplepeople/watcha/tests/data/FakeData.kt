package com.simplepeople.watcha.tests.data

import com.simplepeople.watcha.data.model.external.MovieListResponseDto
import com.simplepeople.watcha.data.model.local.MovieCategoryEntity
import com.simplepeople.watcha.data.model.local.RemoteKeysEntity
import com.simplepeople.watcha.data.model.local.SearchLogItemEntity
import com.simplepeople.watcha.domain.core.Movie

object FakeData {
    val fakeMovieData = MutableList(30) {
        Movie(
            movieId = it.toLong(),
            title = "Movie $it",
            overview = "Overview of Movie $it",
            voteAverage = "${Math.random() * 1000}",
            releaseDate = "2024-${(Math.random() * 12).toInt()}-${(Math.random() * 30).toInt()}"
        )
    }

    val fakeMovieFavoriteData = fakeMovieData.map {
        it.toFavoriteEntity()
    }.toMutableList()

    val fakeMovieEntityData = fakeMovieData.map {
        it.toEntity()
    }.toMutableList()

    val fakeMovieDtoData = MovieListResponseDto(
        page = 1,
        results = ArrayList(
            fakeMovieData.map {
                MovieListResponseDto.Results(
                    id = it.movieId,
                    title = it.title,
                    overview = it.overview,
                    voteAverage = Math.random() * 1000,
                    popularity = Math.random() * 100,
                    releaseDate = "2024-${(Math.random() * 12).toInt()}-${(Math.random() * 30).toInt()}"
                )
            }
        ),
        totalResults = 30,
        totalPages = 1
    )

    val fakeMovieCategoryData = mutableListOf<MovieCategoryEntity>()
    val fakeRemoteKeyData = mutableListOf<RemoteKeysEntity>()
    val fakeCacheData = mutableListOf<Pair<String, Long?>>(
        Pair("last_timeout_cache_category1", null),
        Pair("last_timeout_cache_category2", null),
        Pair("last_timeout_cache_category3", null),
        Pair("last_timeout_cache_category4", null)
    )
    val fakeSearchData = mutableListOf(
        SearchLogItemEntity (
            searchedText = "Ratatouille"
        ),
        SearchLogItemEntity(
            searchedText = "Blade"
        ),
        SearchLogItemEntity(
            searchedText = "Shrek"
        ),
        SearchLogItemEntity(
            searchedText = "Avatar"
        ),
        SearchLogItemEntity(
            searchedText = "Rocky"
        ),
        SearchLogItemEntity(
            searchedText = "The Thing"
        ),
        SearchLogItemEntity(
            searchedText = "The Lord of The Rings"
        )
    )
}



