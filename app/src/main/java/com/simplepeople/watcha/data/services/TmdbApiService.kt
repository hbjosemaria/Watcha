package com.simplepeople.watcha.data.services

import com.simplepeople.watcha.data.model.MovieListResponse
import com.simplepeople.watcha.data.model.MovieResponse
import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

enum class TmdbApiUrl(val url: String) {
    BASE_URL("https://api.themoviedb.org/3/"),
    IMG_BASE_URL("https://image.tmdb.org/t/p/w1280/")
}

//Fetching data from API with all their params and then Gson maps them to their DTO model
interface TmdbApiService : ExternalMovieRepository {

    @GET("movie/{movie_id}")
    override suspend fun getMovieById(
        @Path("movie_id") movieId: Long
    ): MovieResponse

    @GET("search/movie")
    override suspend fun getMoviesByTitle(
        @Query("query") searchText: String,
        @Query("page") page: Int
    ): MovieListResponse

    @GET("movie/popular")
    override suspend fun getMoviesByPage(
        @Query("page") page: Int
    ): MovieListResponse
}