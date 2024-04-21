package com.simplepeople.watcha.data.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.simplepeople.watcha.data.model.MovieListResponse
import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import javax.inject.Inject

class MoviePagingSource @Inject constructor(
    private val apiService : ExternalMovieRepository
) : PagingSource<Int, MovieListResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieListResponse> {
        return try {
            val position = params.key ?: 1
            val response = apiService.getMoviesByPage(position)

            if (response.results.isNotEmpty()) {
                LoadResult.Page(
                    data = listOf(response),
                    prevKey = if (position == 1) null else (position - 1),
                    nextKey = if (position == response.totalPages) null else (position + 1)
                )
            } else {
                LoadResult.Error(throw Exception("No Response"))
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieListResponse>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}