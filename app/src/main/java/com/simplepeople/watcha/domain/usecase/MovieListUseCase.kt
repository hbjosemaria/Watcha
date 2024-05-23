package com.simplepeople.watcha.domain.usecase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.simplepeople.watcha.data.model.local.MovieModel
import com.simplepeople.watcha.data.pager.ExternalFilteredMoviePagingSource
import com.simplepeople.watcha.data.remotemediator.MovieRemoteMediator
import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import com.simplepeople.watcha.data.repository.LocalMovieRepository
import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.ui.common.composables.topbar.HomeFilterOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)

class MovieListUseCase @Inject constructor(
    private val apiService: ExternalMovieRepository,
    private val roomService: LocalMovieRepository,
    private val movieRemoteMediator: MovieRemoteMediator.MovieRemoteMediatorFactory
) {

    //This case uses a cache system with RemoteMediator as it will speed up its fetching and displaying data speed
    fun getMovies(filterOption: HomeFilterOptions): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 220,
                initialLoadSize = 220,
                enablePlaceholders = false
            ),
            remoteMediator = movieRemoteMediator.create(filterOption),
            pagingSourceFactory = {
                roomService.getByCategory(filterOption.categoryId)
            }
        ).flow
            .map { pagingData ->
                pagingData.map { movie: MovieModel ->
                    movie.toDomain()
                }
            }

    fun getByTitle(searchText: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ExternalFilteredMoviePagingSource(apiService, searchText) }
        ).flow
    }

}