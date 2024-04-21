package com.simplepeople.watcha.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.simplepeople.watcha.data.pager.MoviePagingSource
import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import com.simplepeople.watcha.domain.core.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieListUseCase @Inject constructor(
    private val repository: ExternalMovieRepository,
    private val moviePaging: MoviePagingSource
) {
    suspend fun getFirstPage(): List<Movie> =
        repository.getMovies().toDomain()

    suspend fun getNextPage(page: Int): List<Movie> =
        repository.getMoviesByPage(page).toDomain()


    suspend fun getByTitle(searchText: String): List<Movie> =
        repository.getMoviesByTitle(searchText).toDomain()

    fun getMoviesWithPager() : Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 50
            ),
            pagingSourceFactory = {moviePaging}
        ).flow

}