package com.simplepeople.watcha.data.services

import com.simplepeople.watcha.data.model.external.MovieListResponseDto
import com.simplepeople.watcha.data.model.external.MovieResponseDto
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
    ): MovieResponseDto

    @GET("search/movie")
    override suspend fun getMoviesByTitle(
        @Query("query") searchText: String,
        @Query("page") page: Int
    ): MovieListResponseDto

    @GET("movie/now_playing")
    override suspend fun getNowPlayingByPage(
        @Query("page") page: Int
    ): MovieListResponseDto

    @GET("movie/popular")
    override suspend fun getPopularByPage(
        @Query("page") page: Int
    ): MovieListResponseDto

    @GET("movie/top_rated")
    override suspend fun getTopRatedByPage(
        @Query("page") page: Int
    ): MovieListResponseDto

    @GET("movie/upcoming")
    override suspend fun getUpcomingByPage(
        @Query("page") page: Int
    ): MovieListResponseDto
}