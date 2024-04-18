package com.simplepeople.watcha.data.tmdb

import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.repo.ExternalMovieRepository
import retrofit2.http.GET
import retrofit2.http.Query

enum class TmdbApiUrl(val url: String) {
    BASE_URL("https://api.themoviedb.org/3/")
}
//TODO: Fetch REST API endpoints with the precise data that JSON brings after the request and then map them to the data classes
//TODO: Add field names to domain data classes OR create model data classes to fetch the requested data and then cast them to domain data (this would be the best solution)
interface TmdbApiServiceImpl : ExternalMovieRepository {

    @GET("movie/popular")
    override suspend fun getMovies(): Set<Movie>
    @GET("movie/{movie_id}")
    override suspend fun getMovieById(
        @Query("movie_id") movieId: Int
    ): Movie

    @GET("movie?query={search_text}")
    override suspend fun getMoviesByTitle(
        @Query("search_text") searchText: String
    ): Set<Movie>

}