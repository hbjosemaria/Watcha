package com.simplepeople.watcha.tests.domain.usecase

import com.simplepeople.watcha.data.repository.CacheRepository
import com.simplepeople.watcha.ui.common.composables.topbar.HomeFilterOptions
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class CacheUseCaseTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var cacheRepository : CacheRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun forceCacheExpiration() {
        runTest {
            val currentTimeInMillis = System.currentTimeMillis()
            cacheRepository.saveMovieCacheExpiration(HomeFilterOptions.Popular.categoryId, currentTimeInMillis)
            var cacheExpirationInMillis = cacheRepository.loadMovieCacheExpiration(HomeFilterOptions.Popular.categoryId)
            assertEquals(currentTimeInMillis, cacheExpirationInMillis)
            cacheRepository.clearMovieCacheExpiration()
            cacheExpirationInMillis = cacheRepository.loadMovieCacheExpiration(HomeFilterOptions.Popular.categoryId)
            assertNull(cacheExpirationInMillis)
        }
    }
}