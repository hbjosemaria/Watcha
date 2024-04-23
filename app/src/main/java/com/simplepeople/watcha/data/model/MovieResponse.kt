package com.simplepeople.watcha.data.model

import com.google.gson.annotations.SerializedName
import com.simplepeople.watcha.data.services.TmdbApiUrl
import com.simplepeople.watcha.domain.core.Genre
import com.simplepeople.watcha.domain.core.Movie

//DTO Class
data class MovieResponse(
    @SerializedName("adult")
    val adult: Boolean = false,
    @SerializedName("backdrop_path")
    val backdropPath: String = "",
    @SerializedName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection? = null,
    @SerializedName("budget")
    val budget: Long = 0,
    @SerializedName("genres")
    val genres: ArrayList<Genres> = arrayListOf(),
    @SerializedName("homepage")
    val homepage: String = "",
    @SerializedName("id")
    val id: Long = 1,
    @SerializedName("imdb_id")
    val imdbId: String = "",
    @SerializedName("origin_country")
    val originCountry: ArrayList<String> = arrayListOf(),
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
    @SerializedName("production_companies")
    val productionCompanies: ArrayList<ProductionCompanies> = arrayListOf(),
    @SerializedName("production_countries")
    val productionCountries: ArrayList<ProductionCountries> = arrayListOf(),
    @SerializedName("release_date")
    val releaseDate: String = "",
    @SerializedName("revenue")
    val revenue: Long = 0,
    @SerializedName("runtime")
    val runtime: Int = 0,
    @SerializedName("spoken_languages")
    val spokenLanguages: ArrayList<SpokenLanguages> = arrayListOf(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("tagline")
    val tagline: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("video")
    val video: Boolean = false,
    @SerializedName("vote_average")
    val voteAverage: Double = 0.0,
    @SerializedName("vote_count")
    val voteCount: Int = 0
) {
    data class BelongsToCollection(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("name")
        var name: String = "",
        @SerializedName("poster_path")
        var posterPath: String = "",
        @SerializedName("backdrop_path")
        var backdropPath: String = ""
    )

    data class Genres(
        @SerializedName("id")
        val id: Int = 1,
        @SerializedName("name")
        val name: String = ""
    ) {
        fun toDomain(): Genre = Genre.entries.find { it.id == this.id }
            ?: Genre.NOT_SPECIFIED
    }

    data class ProductionCompanies(
        @SerializedName("id")
        val id: Int = 1,
        @SerializedName("logo_path")
        val logoPath: String? = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("origin_country")
        val originCountry: String = ""
    )

    data class ProductionCountries(
        @SerializedName("iso_3166_1")
        val iso31661: String = "",
        @SerializedName("name")
        val name: String = ""
    )

    data class SpokenLanguages(
        @SerializedName("english_name")
        val englishName: String = "",
        @SerializedName("iso_639_1")
        val iso6391: String = "",
        @SerializedName("name")
        val name: String = ""
    )

    //Mapping function to Domain class
    fun toDomain(): Movie {
        return Movie(
            movieId = this.id,
            title = this.title,
            overview = this.overview,
            picture = TmdbApiUrl.IMG_BASE_URL.url + this.posterPath,
            genres = this.genres.map { it.toDomain() },
            releaseDate = this.releaseDate,
            voteAverage = "%.1f".format(this.voteAverage),
            isFavorite = false
        )
    }

}
