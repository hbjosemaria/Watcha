package com.simplepeople.watcha.data.repository

import com.simplepeople.watcha.data.model.local.RemoteKeys
import com.simplepeople.watcha.data.services.RemoteKeysDao
import javax.inject.Inject

interface RemoteKeysRepository {
    suspend fun insertAll(keys: List<RemoteKeys>)
    suspend fun getRemoteKey(
        movieId: Long,
        categoryId: Int
    ): RemoteKeys?

    suspend fun clearRemoteKeys()
}

class RemoteKeyRepositoryImpl @Inject constructor(
    private val apiService: RemoteKeysDao
) : RemoteKeysRepository {
    override suspend fun insertAll(keys: List<RemoteKeys>) =
        apiService.insertAll(keys)


    override suspend fun getRemoteKey(movieId: Long, categoryId: Int): RemoteKeys? =
        apiService.getRemoteKey(
            movieId = movieId,
            categoryId = categoryId
        )


    override suspend fun clearRemoteKeys() =
        apiService.clearRemoteKeys()

}