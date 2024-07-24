package com.simplepeople.watcha.data.service.Room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.simplepeople.watcha.data.model.local.MovieCategoryEntity
import com.simplepeople.watcha.data.model.local.MovieEntity
import com.simplepeople.watcha.data.model.local.MovieFavoriteEntity
import com.simplepeople.watcha.data.model.local.RemoteKeysEntity
import com.simplepeople.watcha.data.model.local.SearchLogItemEntity
import com.simplepeople.watcha.data.model.local.TmdbSessionIdEntity
import com.simplepeople.watcha.data.model.local.UserProfileEntity
import com.simplepeople.watcha.data.repository.LocalAuthRepository
import com.simplepeople.watcha.data.repository.LocalMovieRepository
import com.simplepeople.watcha.data.repository.MovieCategoryRepository
import com.simplepeople.watcha.data.repository.MovieFavoriteRepository
import com.simplepeople.watcha.data.repository.RemoteKeysRepository
import com.simplepeople.watcha.data.repository.SearchRepository
import com.simplepeople.watcha.data.repository.UserProfileLocalRepository
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [
        MovieEntity::class,
        SearchLogItemEntity::class,
        RemoteKeysEntity::class,
        MovieCategoryEntity::class,
        MovieFavoriteEntity::class,
        TmdbSessionIdEntity::class,
        UserProfileEntity::class],
    version = 22,
    exportSchema = false
)
@TypeConverters(
    GenreConverter::class,
    AvatarConverter::class
)
abstract class WatchaDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun searchLogDao(): SearchLogDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun movieCategoryDao(): MovieCategoryDao
    abstract fun movieFavoriteDao(): MovieFavoriteDao
    abstract fun tmdbSessionIdDao(): TmdbSessionIdDao
    abstract fun userProfileDao(): UserProfileDao
}

@Dao
interface MovieDao : LocalMovieRepository {

    @Query("select * from movie")
    override fun getAllMovies(): PagingSource<Int, MovieEntity>

    @Query("select m.* from movie m inner join movie_category mc on m.movieId = mc.movieId where mc.categoryId = :category order by mc.position")
    override fun getByCategory(category: Int): PagingSource<Int, MovieEntity>

    @Query("select * from movie where movieId = :movieId")
    override suspend fun getMovieById(movieId: Long): MovieEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun addMovie(movie: MovieEntity): Long

    @Query("delete from movie where movieId = :movieId")
    override suspend fun deleteMovie(movieId: Long): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertAllMovies(movieList: List<MovieEntity>)

    @Query("delete from movie")
    override suspend fun clearCachedMovies(): Int
}

@Dao
interface SearchLogDao : SearchRepository {
    @Query("select * from search order by id desc limit 5")
    override fun getRecentSearch(): Flow<List<SearchLogItemEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override fun addNewSearch(searchLogItemEntity: SearchLogItemEntity): Long

    @Delete
    override fun removeSearch(searchLogItemEntity: SearchLogItemEntity): Int

    @Query("delete from search")
    override fun cleanSearchLog(): Int
}

@Dao
interface RemoteKeysDao : RemoteKeysRepository {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertAll(keys: List<RemoteKeysEntity>)

    @Query("select * from remote_keys where movieId = :movieId and categoryId = :categoryId")
    override suspend fun getRemoteKey(movieId: Long, categoryId: Int): RemoteKeysEntity?

    @Query("delete from remote_keys")
    override suspend fun clearRemoteKeys()
}

@Dao
interface MovieCategoryDao : MovieCategoryRepository {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertAll(categories: List<MovieCategoryEntity>)

    @Query("delete from movie_category")
    override suspend fun clearAll()
}

@Dao
interface MovieFavoriteDao : MovieFavoriteRepository {
    @Query("select * from movie_favorite order by id asc")
    override fun getFavorites(): PagingSource<Int, MovieFavoriteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertFavorite(favorite: MovieFavoriteEntity)

    @Query("delete from movie_favorite where movieId = :movieId")
    override suspend fun removeFavorite(movieId: Long)

    @Query("select count(1) from movie_favorite where movieId = :movieId")
    override suspend fun checkIfMovieIsFavorite(movieId: Long): Int
}

@Dao
interface TmdbSessionIdDao : LocalAuthRepository {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun saveSessionId(sessionId: TmdbSessionIdEntity): Long

    @Query("select sessionId from tmdb_session_id where email = :email")
    override suspend fun getSessionId(email: String?): String?
}

@Dao
interface UserProfileDao : UserProfileLocalRepository {

    @Query("select * from user_profile where sessionId = :sessionId")
    override suspend fun getUserProfile(sessionId: String?): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun saveUserProfile(userProfile: UserProfileEntity): Long

    @Query("update user_profile set avatar = :avatar where sessionId = :sessionId")
    override suspend fun updateAvatar(
        avatar: UserProfileEntity.AvatarEntity,
        sessionId: String?,
    )
}
