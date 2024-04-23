package com.simplepeople.watcha.data.services

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.simplepeople.watcha.data.model.MovieDAO
import com.simplepeople.watcha.data.repository.LocalMovieRepository
import com.simplepeople.watcha.domain.core.Genre
import javax.inject.Singleton

@Database(entities = [MovieDAO::class], version = 2, exportSchema = false)
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
    override fun getFavoriteMovies(): PagingSource<Int, MovieDAO>
    override suspend fun getFavoriteById(movieId: Long): MovieDAO
    override suspend fun saveFavoriteMovie(movie: MovieDAO): Long
    override suspend fun deleteFavoriteMovie(movieId: Long): Int
    override suspend fun checkIfMovieIsFavorite(movieId: Long): Int
}

@Dao
interface MovieDao : RoomService {

    @Query("select * from movie order by title asc")
    override fun getFavoriteMovies(): PagingSource<Int, MovieDAO>

    @Query("select * from movie where movieId = :movieId")
    override suspend fun getFavoriteById(movieId: Long): MovieDAO

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override suspend fun saveFavoriteMovie(movie: MovieDAO): Long

    @Query("delete from movie where movieId = :movieId")
    override suspend fun deleteFavoriteMovie(movieId: Long): Int

    @Query("select count(1) from movie where movieId = :movieId")
    override suspend fun checkIfMovieIsFavorite(movieId: Long): Int
}
