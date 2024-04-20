package com.simplepeople.watcha.data.tmdb

import com.simplepeople.watcha.data.model.MovieListResponse
import com.simplepeople.watcha.data.model.MovieResponse
import com.simplepeople.watcha.domain.repo.ExternalMovieRepository
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

enum class TmdbApiUrl(val url: String) {
    BASE_URL("https://api.themoviedb.org/3/"),
    IMG_BASE_URL("https://image.tmdb.org/t/p/w1280/")
}

//Fetching data from API with all their params and then Gson maps them to their DTO model
interface TmdbApiServiceImpl : ExternalMovieRepository {

    @GET("movie/popular")
    override suspend fun getMovies(): MovieListResponse

    @GET("movie/{movie_id}")
    override suspend fun getMovieById(
        @Path("movie_id") movieId: Int
    ): MovieResponse

    @GET("search/movie")
    override suspend fun getMoviesByTitle(
        @Query("query") searchText: String
    ): MovieListResponse

    @GET("movie/popular")
    override suspend fun getMoviesByPage(
        @Query("page") page: Int
    ): MovieListResponse
}