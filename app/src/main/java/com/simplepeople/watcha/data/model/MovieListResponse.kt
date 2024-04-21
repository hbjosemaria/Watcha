package com.simplepeople.watcha.data.model

import com.google.gson.annotations.SerializedName
import com.simplepeople.watcha.data.services.TmdbApiUrl
import com.simplepeople.watcha.domain.core.Movie

//DTO Class
data class MovieListResponse(
    @SerializedName("page") var page: Int = 1,
    @SerializedName("results") var results: ArrayList<Results> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int = 0,
    @SerializedName("total_results") var totalResults: Int = 0
) {
    data class Results(
        @SerializedName("adult") var adult: Boolean = false,
        @SerializedName("backdrop_path") var backdropPath: String = "",
        @SerializedName("genre_ids") var genreIds: ArrayList<Int> = arrayListOf(),
        @SerializedName("id") var id: Int = 1,
        @SerializedName("original_language") var originalLanguage: String = "",
        @SerializedName("original_title") var originalTitle: String = "",
        @SerializedName("overview") var overview: String = "",
        @SerializedName("popularity") var popularity: Double = 0.0,
        @SerializedName("poster_path") var posterPath: String = "",
        @SerializedName("release_date") var releaseDate: String = "",
        @SerializedName("title") var title: String = "",
        @SerializedName("video") var video: Boolean = false,
        @SerializedName("vote_average") var voteAverage: Double = 0.0,
        @SerializedName("vote_count") var voteCount: Int = 0
    )

    //Function for mapping to Domain class
    fun toDomain(): List<Movie> {
        return results.map {
            Movie(
                movieId = it.id,
                title = it.title,
                overview = it.overview,
                picture = TmdbApiUrl.IMG_BASE_URL.url + it.posterPath,
                releaseDate = it.releaseDate,
                voteAverage = it.voteAverage.toString()
            )
        }
    }
}