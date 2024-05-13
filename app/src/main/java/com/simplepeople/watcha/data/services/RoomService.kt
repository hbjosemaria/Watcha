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
import com.simplepeople.watcha.data.model.MovieModel
import com.simplepeople.watcha.data.model.SearchLogItemModel
import com.simplepeople.watcha.data.repository.LocalMovieRepository
import com.simplepeople.watcha.data.repository.SearchRepository
import com.simplepeople.watcha.domain.core.Genre
import javax.inject.Singleton

@Database(entities = [MovieModel::class, SearchLogItemModel::class], version = 7, exportSchema = false)
@TypeConverters(GenreConverter::class)
abstract class WatchaDatabase : RoomDatabase() {
    abstract fun movieDao() : MovieDao
    abstract fun searchLogDao() : SearchLogDao
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

@Dao
interface MovieDao : LocalMovieRepository {

    @Query("select * from movie order by title asc")
    override fun getFavoriteMovies(): PagingSource<Int, MovieModel>

    @Query("select * from movie where movieId = :movieId")
    override suspend fun getFavoriteById(movieId: Long): MovieModel

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override suspend fun saveFavoriteMovie(movie: MovieModel): Long

    @Query("delete from movie where movieId = :movieId")
    override suspend fun deleteFavoriteMovie(movieId: Long): Int

    @Query("select count(1) from movie where movieId = :movieId")
    override suspend fun checkIfMovieIsFavorite(movieId: Long): Int
}

@Dao
interface SearchLogDao : SearchRepository {
    @Query("select * from search order by id desc limit 5")
    override fun getRecentSearch(): PagingSource<Int, SearchLogItemModel>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override fun addNewSearch(searchLogItemModel: SearchLogItemModel): Long

    @Delete
    override fun removeSearch(searchLogItemModel: SearchLogItemModel): Int

    @Query("delete from search")
    override fun cleanSearchLog(): Int
}
