package com.simplepeople.watcha.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.simplepeople.watcha.data.pager.ExternalMoviePagingSource
import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import com.simplepeople.watcha.domain.core.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieListUseCase @Inject constructor(
    private val repository: ExternalMovieRepository,
    private val moviePaging: ExternalMoviePagingSource
) {
    suspend fun getByTitle(searchText: String): List<Movie> =
        repository.getMoviesByTitle(searchText).toDomain()

    fun getMovies() : Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 30
            ),
            pagingSourceFactory = {moviePaging}
        ).flow
}