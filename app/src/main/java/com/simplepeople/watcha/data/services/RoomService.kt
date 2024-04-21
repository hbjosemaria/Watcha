package com.simplepeople.watcha.data.services

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.simplepeople.watcha.data.repository.LocalMovieRepository
import com.simplepeople.watcha.domain.core.Genre
import com.simplepeople.watcha.domain.core.Movie
import javax.inject.Singleton

@Database(entities = [Movie::class], version = 1, exportSchema = false)
@TypeConverters(GenreConverter::class)
abstract class WatchaDatabase : RoomDatabase() {
    abstract fun movieDao() : MovieDao
}

@Singleton
class GenreConverter {
    @TypeConverter
    fun genresToJson(value: List<Genre>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToGenres(value: String): List<Genre> {
        return Gson().fromJson(value, object : TypeToken<List<Genre>>(){}.type)
    }
}

interface RoomService : LocalMovieRepository {
    override fun getFavoriteMovies(): PagingSource<Int, Movie>
    override suspend fun getFavoriteById(movieId: Int): Movie
    override suspend fun saveFavoriteMovie(movie: Movie): Long
    override suspend fun deleteFavoriteMovie(movie: Movie): Int
    override suspend fun checkIfMovieIsFavorite(movieId: Int): Long
}

@Dao
interface MovieDao : RoomService {

    @Query("select * from movie order by title asc")
    override fun getFavoriteMovies(): PagingSource<Int, Movie>

    @Query("select * from movie where movieId = :movieId")
    override suspend fun getFavoriteById(movieId: Int): Movie

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override suspend fun saveFavoriteMovie(movie: Movie): Long

    @Delete
    override suspend fun deleteFavoriteMovie(movie: Movie): Int

    @Query("select count(1) from movie where movieId = :movieId")
    override suspend fun checkIfMovieIsFavorite(movieId: Int): Long
}
