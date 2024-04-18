package com.simplepeople.watcha.data.local

import com.simplepeople.watcha.domain.core.Movie
import com.simplepeople.watcha.domain.repo.LocalMovieRepository

interface RoomMovieServiceImpl : LocalMovieRepository {

    override suspend fun getFavoriteMovies(): Set<Movie>

}