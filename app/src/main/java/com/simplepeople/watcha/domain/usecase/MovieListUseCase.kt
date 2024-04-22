package com.simplepeople.watcha.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.simplepeople.watcha.data.pager.ExternalDefaultMoviePagingSource
import com.simplepeople.watcha.data.pager.ExternalFilteredMoviePagingSource
import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import com.simplepeople.watcha.domain.core.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieListUseCase @Inject constructor(
    private val deFaultMoviePaging: ExternalDefaultMoviePagingSource,
    private val repository : ExternalMovieRepository
) {

    fun getMovies() : Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 200
            ),
            pagingSourceFactory = {deFaultMoviePaging}
        ).flow

    fun getByTitle(searchText: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 200
            ),
            pagingSourceFactory = { ExternalFilteredMoviePagingSource(repository, searchText) }
        ).flow
    }


}