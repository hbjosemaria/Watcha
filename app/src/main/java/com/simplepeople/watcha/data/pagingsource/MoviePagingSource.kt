package com.simplepeople.watcha.data.pagingsource

import androidx.compose.ui.text.intl.Locale
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import com.simplepeople.watcha.domain.core.Movie

const val TMDB_API_MAX_PAGES = 11

class ExternalFilteredMoviePagingSource(
    private val repository: ExternalMovieRepository,
    private val searchText: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        return try {
            val position = params.key ?: 1
            val response = repository.getMoviesByTitle(searchText, position, Locale.current.language)

            if (response.results.isNotEmpty()) {
                LoadResult.Page(
                    data = response.toDomain(),
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

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}