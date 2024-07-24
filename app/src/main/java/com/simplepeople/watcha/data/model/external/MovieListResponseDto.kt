package com.simplepeople.watcha.data.model.external

import com.google.gson.annotations.SerializedName
import com.simplepeople.watcha.data.model.local.MovieEntity
import com.simplepeople.watcha.data.service.TmdbApiUrl
import com.simplepeople.watcha.domain.core.Movie

data class MovieListResponseDto(
    @SerializedName("page")
    val page: Int = 1,
    @SerializedName("results")
    val results: ArrayList<Results> = arrayListOf(),
    @SerializedName("total_pages")
    val totalPages: Int = 1,
    @SerializedName("total_results")
    val totalResults: Int = results.size,
) {
    data class Results(
        @SerializedName("adult")
        val adult: Boolean = false,
        @SerializedName("backdrop_path")
        val backdropPath: String = "",
        @SerializedName("genre_ids")
        val genreIds: ArrayList<Int> = arrayListOf(),
        @SerializedName("id")
        val id: Long = 1,
        @SerializedName("original_language")
        val originalLanguage: String = "",
        @SerializedName("original_title")
        val originalTitle: String = "",
        @SerializedName("overview")
        val overview: String = "",
        @SerializedName("popularity")
        val popularity: Double = 0.0,
        @SerializedName("poster_path")
        val posterPath: String = "",
        @SerializedName("release_date")
        val releaseDate: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("video")
        val video: Boolean = false,
        @SerializedName("vote_average")
        val voteAverage: Double = 0.0,
        @SerializedName("vote_count")
        val voteCount: Int = 0,
    )

    fun toDomain(): List<Movie> =
        results.map {
            Movie(
                movieId = it.id,
                title = it.title,
                overview = it.overview,
                picture = TmdbApiUrl.POSTER_BASE_URL.url + it.posterPath,
                backdrop = TmdbApiUrl.POSTER_BASE_URL.url + it.backdropPath,
                releaseDate = it.releaseDate,
                voteAverage = (it.voteAverage * 10).toInt()
            )
        }

    fun toEntity(): List<MovieEntity> =
        results.map {
            MovieEntity(
                movieId = it.id,
                title = it.title,
                overview = it.overview,
                picture = TmdbApiUrl.POSTER_BASE_URL.url + it.posterPath,
                backdrop = TmdbApiUrl.POSTER_BASE_URL.url + it.backdropPath,
                releaseDate = it.releaseDate,
                voteAverage = (it.voteAverage * 10).toInt()
            )
        }

}