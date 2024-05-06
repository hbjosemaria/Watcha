package com.simplepeople.watcha.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.simplepeople.watcha.data.pager.ExternalFilteredMoviePagingSource
import com.simplepeople.watcha.data.pager.ExternalMoviePagingSource
import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.appscreen.common.clases.HomeFilterOptions
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieListUseCase @Inject constructor(
    private val repository : ExternalMovieRepository
) {

    fun getMovies(filterOption: HomeFilterOptions) : Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(
                pageSize = 40,
                maxSize = 200
            ),
            pagingSourceFactory = {ExternalMoviePagingSource(repository, filterOption)}
        ).flow

    fun getByTitle(searchText: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 40,
                maxSize = 200
            ),
            pagingSourceFactory = { ExternalFilteredMoviePagingSource(repository, searchText) }
        ).flow
    }


}