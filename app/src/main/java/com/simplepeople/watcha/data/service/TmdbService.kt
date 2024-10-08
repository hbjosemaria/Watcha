package com.simplepeople.watcha.data.service

import com.simplepeople.watcha.data.model.external.MovieListResponseDto
import com.simplepeople.watcha.data.model.external.MovieResponseDto
import com.simplepeople.watcha.data.model.external.RequestTokenDto
import com.simplepeople.watcha.data.model.external.SessionDto
import com.simplepeople.watcha.data.model.external.TokenDto
import com.simplepeople.watcha.data.model.external.UserProfileDto
import com.simplepeople.watcha.data.repository.ExternalAuthRepository
import com.simplepeople.watcha.data.repository.ExternalMovieRepository
import com.simplepeople.watcha.data.repository.UserProfileExternalRepository
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

enum class TmdbApiUrl(val url: String) {
    BASE_URL("https://api.themoviedb.org/3/"),
    POSTER_BASE_URL("https://image.tmdb.org/t/p/w500"),
    BACKDROP_BASE_URL("https://image.tmdb.org/t/p/w1280"),
    CAST_IMG_BASE_URL("https://image.tmdb.org/t/p/w276_and_h350_face"),
    PROFILE_GRAVATAR_IMG_URL("https://secure.gravatar.com/avatar/{hash}.jpg?s=300"),
    PROFILE_TMDB_IMG_URL("https://media.themoviedb.org/t/p/w300_and_h300_face"),
    REVIEW_PROFILE_IMG_BASE_URL("https://media.themoviedb.org/t/p/w300_and_h300_face"),
    AUTH_URL("https://www.themoviedb.org/authenticate/"),
    AUTH_REDIRECT_URL("https://simplepeople.watcha.com/approved"),
    MOVIE_WEB_BASE_URL("https://www.themoviedb.org/movie/")
}

interface TmdbMovieService : ExternalMovieRepository {
    @GET("movie/{movie_id}")
    override suspend fun getMovieById(
        @Path("movie_id") movieId: Long,
        @Query("language") language: String,
        @Query("include_image_language") imageLanguage: String,
        @Query("append_to_response") append: String
    ): MovieResponseDto

    @GET("search/movie")
    override suspend fun getMoviesByTitle(
        @Query("query") searchText: String,
        @Query("page") page: Int,
        @Query("language") language: String,
    ): MovieListResponseDto

    @GET("movie/now_playing")
    override suspend fun getNowPlayingByPage(
        @Query("page") page: Int,
        @Query("language") language: String,
    ): MovieListResponseDto

    @GET("movie/popular")
    override suspend fun getPopularByPage(
        @Query("page") page: Int,
        @Query("language") language: String,
    ): MovieListResponseDto

    @GET("movie/top_rated")
    override suspend fun getTopRatedByPage(
        @Query("page") page: Int,
        @Query("language") language: String,
    ): MovieListResponseDto

    @GET("movie/upcoming")
    override suspend fun getUpcomingByPage(
        @Query("page") page: Int,
        @Query("language") language: String,
    ): MovieListResponseDto
}

interface TmdbExternalAuthService : ExternalAuthRepository {

    @POST("authentication/session/new")
    override suspend fun createSession(@Body tokenDto: TokenDto): SessionDto

    @GET("authentication/token/new")
    override suspend fun getRequestToken(): RequestTokenDto

}

interface TmdbUserProfileService : UserProfileExternalRepository {
    @GET("account")
    override suspend fun fetchUserProfile(
        @Query("session_id") sessionId: String?,
    ): UserProfileDto
}