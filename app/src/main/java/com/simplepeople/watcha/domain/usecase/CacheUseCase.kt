package com.simplepeople.watcha.domain.usecase

import com.simplepeople.watcha.data.repository.CacheRepository
import javax.inject.Inject

class CacheUseCase @Inject constructor (
    private val cacheRepository: CacheRepository
) {
    suspend fun forceCacheExpiration() {
        cacheRepository.forceCacheExpiration()
    }

}