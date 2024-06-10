package com.simplepeople.watcha.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.simplepeople.watcha.data.model.local.MovieEntity
import com.simplepeople.watcha.data.model.local.MovieFavoriteEntity
import com.simplepeople.watcha.domain.core.Movie


class FakeLocalMoviePagingSource : PagingSource<Int, MovieEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieEntity> {
        return try {
            val list = FakeData.fakeMovieEntityData.shuffled()

            LoadResult.Page(
                data = list,
                prevKey = null,
                nextKey = null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieEntity>): Int? = null
}

class FakeExternalMoviePagingSource (
    private val searchText : String
) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val list = FakeData.fakeMovieData.filter {
                it.title.contains(searchText)
            }

            LoadResult.Page(
                data = list,
                prevKey = null,
                nextKey = null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = null
}

class FakeMovieFavoritePagingSource : PagingSource<Int, MovieFavoriteEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieFavoriteEntity> {
        return try {
            val list = FakeData.fakeMovieFavoriteData

            LoadResult.Page(
                data = list,
                prevKey = null,
                nextKey = null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieFavoriteEntity>): Int?  = null
}