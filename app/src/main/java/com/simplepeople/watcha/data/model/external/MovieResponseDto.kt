package com.simplepeople.watcha.data.model.external

import com.google.gson.annotations.SerializedName
import com.simplepeople.watcha.data.service.TmdbApiUrl
import com.simplepeople.watcha.domain.core.Genre
import com.simplepeople.watcha.domain.core.Movie

data class MovieResponseDto(

    //Movie details data
    @SerializedName("adult")
    val adult: Boolean = false,
    @SerializedName("backdrop_path")
    val backdropPath: String = "",
    @SerializedName("belongs_to_collection")
    val belongsToCollection: BelongsToCollectionDto? = null,
    @SerializedName("budget")
    val budget: Long = 0,
    @SerializedName("genres")
    val genres: List<GenresDto> = emptyList(),
    @SerializedName("homepage")
    val homepage: String = "",
    @SerializedName("id")
    val id: Long = 1,
    @SerializedName("imdb_id")
    val imdbId: String = "",
    @SerializedName("origin_country")
    val originCountry: List<String> = emptyList(),
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
    val productionCompanies: List<ProductionCompaniesDto> = emptyList(),
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountriesDto> = emptyList(),
    @SerializedName("release_date")
    val releaseDate: String = "",
    @SerializedName("revenue")
    val revenue: Long = 0,
    @SerializedName("runtime")
    val runtime: Int = 0,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguagesDto> = emptyList(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("tagline")
    val tagline: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("video")
    val video: Boolean = false,
    @SerializedName("vote_average")
    val voteAverage: Double? = null,
    @SerializedName("vote_count")
    val voteCount: Int = 0,

    //Images data
    @SerializedName("images")
    val images: ImagesDto = ImagesDto(),

    //Videos data
    @SerializedName("videos")
    val videos: VideosDto = VideosDto(),

    //Credits data
    @SerializedName("credits")
    val credits: CreditsDto = CreditsDto(),

    //Reviews data
    @SerializedName("reviews")
    val reviews: ReviewsDto = ReviewsDto(),

    //Recommendations data
    @SerializedName("recommendations")
    val recommendations: RecommendationsDto = RecommendationsDto(),

    //Similar data
    @SerializedName("similar")
    val similar: SimilarDto = SimilarDto(),
) {
    data class BelongsToCollectionDto(
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("poster_path")
        val posterPath: String = "",
        @SerializedName("backdrop_path")
        val backdropPath: String = "",
    )

    data class GenresDto(
        @SerializedName("id")
        val id: Int = 1,
        @SerializedName("name")
        val name: String = "",
    ) {
        fun toDomain(): Genre = Genre.entries.find { it.id == this.id }
            ?: Genre.NOT_SPECIFIED
    }

    data class ProductionCompaniesDto(
        @SerializedName("id")
        val id: Int = 1,
        @SerializedName("logo_path")
        val logoPath: String? = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("origin_country")
        val originCountry: String = "",
    )

    data class ProductionCountriesDto(
        @SerializedName("iso_3166_1")
        val iso31661: String = "",
        @SerializedName("name")
        val name: String = "",
    )

    data class SpokenLanguagesDto(
        @SerializedName("english_name")
        val englishName: String = "",
        @SerializedName("iso_639_1")
        val iso6391: String = "",
        @SerializedName("name")
        val name: String = "",
    )

    data class ImagesDto(
        @SerializedName("backdrops")
        val backdrops: List<ImageDto> = emptyList(),
        @SerializedName("logos")
        val logos: List<ImageDto> = emptyList(),
        @SerializedName("posters")
        val posters: List<ImageDto> = emptyList(),
    ) {
        data class ImageDto(
            @SerializedName("aspect_ratio")
            val aspectRatio: Double = 0.0,
            @SerializedName("height")
            val height: Int = 0,
            @SerializedName("iso_639_1")
            val iso6391: String = "",
            @SerializedName("file_path")
            val filePath: String = "",
            @SerializedName("vote_average")
            val voteAverage: Double = 0.0,
            @SerializedName("vote_count")
            val voteCount: Int = 0,
            @SerializedName("width")
            val width: Int = 0,
        )

        fun toDomain(): List<String> =
            backdrops.map { TmdbApiUrl.BACKDROP_BASE_URL.url + it.filePath }

    }

    data class VideosDto(
        val results: List<VideoDto> = emptyList(),
    ) {
        data class VideoDto(
            @SerializedName("iso_639_1")
            val iso6391: String = "",
            @SerializedName("iso_3166_1")
            val iso31661: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("key")
            val key: String = "",
            @SerializedName("site")
            val site: String = "",
            @SerializedName("size")
            val size: Int = 0,
            @SerializedName("type")
            val type: String = "",
            @SerializedName("official")
            val official: Boolean = true,
            @SerializedName("published_at")
            val publishedAt: String = "",
            @SerializedName("id")
            val id: String = "",
        )

        fun toDomain(): String? =
            results.lastOrNull {
                it.site.contains("Youtube", ignoreCase = true) && it.type.contains(
                    "Trailer",
                    ignoreCase = true
                )
            }?.key

    }

    data class CreditsDto(
        @SerializedName("cast")
        val cast: List<CastMemberDto> = emptyList(),
        @SerializedName("crew")
        val crew: List<CrewMemberDto> = emptyList(),
    ) {
        data class CastMemberDto(
            @SerializedName("adult")
            val adult: Boolean = false,
            @SerializedName("gender")
            val gender: Int = 0,
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("known_for_department")
            val knownForDepartment: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("original_name")
            val originalName: String = "",
            @SerializedName("popularity")
            val popularity: Double = 0.0,
            @SerializedName("profile_path")
            val profilePath: String? = null,
            @SerializedName("cast_id")
            val castId: Int = 0,
            @SerializedName("character")
            val character: String = "",
            @SerializedName("credit_id")
            val creditId: String = "",
            @SerializedName("order")
            val order: Int = 0,
        )

        data class CrewMemberDto(
            @SerializedName("adult")
            val adult: Boolean = false,
            @SerializedName("gender")
            val gender: Int = 0,
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("known_for_department")
            val knownForDepartment: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("original_name")
            val originalName: String = "",
            @SerializedName("popularity")
            val popularity: Double = 0.0,
            @SerializedName("profile_path")
            val profilePath: String? = null,
            @SerializedName("credit_id")
            val creditId: String = "",
            @SerializedName("department")
            val department: String = "",
            @SerializedName("job")
            val job: String = "",
        )

        fun toDomain(): List<Movie.CastMember> =
            cast.filter { it.knownForDepartment.contains("Acting", ignoreCase = true) }
                .map { castMember ->
                    Movie.CastMember(
                        name = castMember.name,
                        profilePath = TmdbApiUrl.CAST_IMG_BASE_URL.url + castMember.profilePath,
                        character = castMember.character,
                        order = castMember.order
                    )
                }
    }

    data class ReviewsDto(
        @SerializedName("page")
        val page: Int = 0,
        @SerializedName("results")
        val results: List<ReviewDto> = emptyList(),
        @SerializedName("total_pages")
        val totalPages: Int = 0,
        @SerializedName("total_results")
        val totalResults: Int = 0,
    ) {
        data class ReviewDto(
            @SerializedName("author")
            val author: String = "",
            @SerializedName("author_details")
            val authorDetails: AuthorDetailsDto = AuthorDetailsDto(),
            @SerializedName("content")
            val content: String = "",
            @SerializedName("created_at")
            val createdAt: String = "",
            @SerializedName("id")
            val id: String = "",
            @SerializedName("updated_at")
            val updatedAt: String = "",
            @SerializedName("url")
            val url: String = "",
        ) {
            data class AuthorDetailsDto(
                @SerializedName("name")
                val name: String = "",
                @SerializedName("username")
                val username: String = "",
                @SerializedName("avatar_path")
                val avatarPath: String? = null,
                @SerializedName("rating")
                val rating: Int? = 0,
            )
        }

        fun toDomain(): Movie.Review? {
            val mostRecentReview = results.lastOrNull()
            return mostRecentReview?.let {
                Movie.Review(
                    author = it.author,
                    avatarPath = it.authorDetails.avatarPath?.let { path ->
                        TmdbApiUrl.REVIEW_PROFILE_IMG_BASE_URL.url + path
                    },
                    rating = it.authorDetails.rating,
                    content = it.content,
                    updatedAt = it.updatedAt
                )
            }
        }
    }

    data class RecommendationsDto(
        @SerializedName("page")
        val page: Int = 0,
        @SerializedName("results")
        val results: List<MovieItemDto> = emptyList(),
        @SerializedName("total_pages")
        val totalPages: Int = 0,
        @SerializedName("total_results")
        val totalResults: Int = 0,
    ) {
        fun toDomain(): List<Movie.MovieItem> =
            results.map { movieItem ->
                Movie.MovieItem(
                    id = movieItem.id,
                    title = movieItem.title,
                    posterPath = TmdbApiUrl.POSTER_BASE_URL.url + movieItem.posterPath
                )
            }
    }

    data class SimilarDto(
        @SerializedName("page")
        val page: Int = 0,
        @SerializedName("results")
        val results: List<MovieItemDto> = emptyList(),
        @SerializedName("total_pages")
        val totalPages: Int = 0,
        @SerializedName("total_results")
        val totalResults: Int = 0,
    ) {
        fun toDomain(): List<Movie.MovieItem> =
            results.map { movieItem ->
                Movie.MovieItem(
                    id = movieItem.id,
                    title = movieItem.title,
                    posterPath = TmdbApiUrl.POSTER_BASE_URL.url + movieItem.posterPath
                )
            }
    }

    data class MovieItemDto(
        @SerializedName("backdrop_path")
        val backdropPath: String? = null,
        @SerializedName("id")
        val id: Long = 0,
        @SerializedName("title")
        val title: String = "",
        @SerializedName("original_title")
        val originalTitle: String = "",
        @SerializedName("overview")
        val overview: String = "",
        @SerializedName("poster_path")
        val posterPath: String? = null,
        @SerializedName("media_type")
        val mediaType: String = "",
        @SerializedName("adult")
        val adult: Boolean = false,
        @SerializedName("original_language")
        val originalLanguage: String = "",
        @SerializedName("genre_ids")
        val genreIds: List<Int> = emptyList(),
        @SerializedName("popularity")
        val popularity: Double = 0.0,
        @SerializedName("release_date")
        val releaseDate: String = "",
        @SerializedName("video")
        val video: Boolean = false,
        @SerializedName("vote_average")
        val voteAverage: Double? = null,
        @SerializedName("vote_count")
        val voteCount: Int = 0,
    )

    fun toDomain(): Movie {
        return Movie(
            movieId = this.id,
            title = this.title,
            overview = this.overview,
            tagline = this.tagline,
            runTime = this.runtime,
            picture = TmdbApiUrl.POSTER_BASE_URL.url + this.posterPath,
            backdrop = TmdbApiUrl.BACKDROP_BASE_URL.url + this.backdropPath,
            genres = this.genres.map { it.toDomain() },
            releaseDate = this.releaseDate,
            voteAverage = this.voteAverage?.let{(this.voteAverage * 10).toInt()},
            webUrl = TmdbApiUrl.MOVIE_WEB_BASE_URL.url + this.id,
            isFavorite = false,
            images = this.images.toDomain(),
            trailer = this.videos.toDomain(),
            credits = this.credits.toDomain(),
            review = this.reviews.toDomain(),
            recommendations = this.recommendations.toDomain(),
            similar = this.similar.toDomain()
        )
    }

}
