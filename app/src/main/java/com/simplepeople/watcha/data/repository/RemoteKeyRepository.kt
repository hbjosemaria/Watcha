package com.simplepeople.watcha.data.repository

import com.simplepeople.watcha.data.model.local.RemoteKeysEntity
import com.simplepeople.watcha.data.services.RemoteKeysDao
import javax.inject.Inject

interface RemoteKeysRepository {
    suspend fun insertAll(keys: List<RemoteKeysEntity>)
    suspend fun getRemoteKey(
        movieId: Long,
        categoryId: Int
    ): RemoteKeysEntity?

    suspend fun clearRemoteKeys()
}

class RemoteKeyRepositoryImpl @Inject constructor(
    private val apiService: RemoteKeysDao
) : RemoteKeysRepository {
    override suspend fun insertAll(keys: List<RemoteKeysEntity>) =
        apiService.insertAll(keys)


    override suspend fun getRemoteKey(movieId: Long, categoryId: Int): RemoteKeysEntity? =
        apiService.getRemoteKey(
            movieId = movieId,
            categoryId = categoryId
        )


    override suspend fun clearRemoteKeys() =
        apiService.clearRemoteKeys()

}