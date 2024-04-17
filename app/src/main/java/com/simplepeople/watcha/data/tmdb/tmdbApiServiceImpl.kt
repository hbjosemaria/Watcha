package com.simplepeople.watcha.data.tmdb

import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.repo.ExternalMovieRepository
import retrofit2.http.GET
import retrofit2.http.Query

interface tmdbApiMovieImpl : ExternalMovieRepository {

    @GET("")
    override suspend fun getMovies(): Set<Movie>

    @GET("")
    override suspend fun getMovieById(
        @Query("") movieId: Int
    ): Set<Movie>

    @GET("")
    override suspend fun getMoviesByTitle(
        @Query("") searchText: String
    ): Set<Movie>

}