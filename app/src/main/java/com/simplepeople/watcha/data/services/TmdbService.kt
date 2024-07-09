package com.simplepeople.watcha.data.services

import com.simplepeople.watcha.data.model.external.MovieListResponseDto
import com.simplepeople.watcha.data.model.external.MovieResponseDto
import com.simplepeople.watcha.data.model.external.RequestTokenDto
import com.simplepeople.watcha.data.model.external.SessionDto
import com.simplepeople.watcha.data.model.external.TokenDto
import com.simplepeople.watcha.data.repository.ExternalAuthRepository
import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

enum class TmdbApiUrl(val url: String) {
    BASE_URL("https://api.themoviedb.org/3/"),
    IMG_BASE_URL("https://image.tmdb.org/t/p/w500/"),
    AUTH_URL("https://www.themoviedb.org/authenticate/"),
    AUTH_REDIRECT_URL("https://simplepeople.watcha.com/approved")
}

interface TmdbMovieService : ExternalMovieRepository {

    @GET("movie/{movie_id}")
    override suspend fun getMovieById(
        @Path("movie_id") movieId: Long,
        @Query("language") language: String
    ): MovieResponseDto

    @GET("search/movie")
    override suspend fun getMoviesByTitle(
        @Query("query") searchText: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): MovieListResponseDto

    @GET("movie/now_playing")
    override suspend fun getNowPlayingByPage(
        @Query("page") page: Int,
        @Query("language") language: String
    ): MovieListResponseDto

    @GET("movie/popular")
    override suspend fun getPopularByPage(
        @Query("page") page: Int,
        @Query("language") language: String
    ): MovieListResponseDto

    @GET("movie/top_rated")
    override suspend fun getTopRatedByPage(
        @Query("page") page: Int,
        @Query("language") language: String
    ): MovieListResponseDto

    @GET("movie/upcoming")
    override suspend fun getUpcomingByPage(
        @Query("page") page: Int,
        @Query("language") language: String
    ): MovieListResponseDto
}

interface TmdbExternalAuthService : ExternalAuthRepository {

    @POST("authentication/session/new")
    override suspend fun createSession(@Body tokenDto: TokenDto): SessionDto

    @GET("authentication/token/new")
    override suspend fun getRequestToken(): RequestTokenDto

}