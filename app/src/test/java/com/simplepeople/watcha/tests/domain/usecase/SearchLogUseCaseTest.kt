package com.simplepeople.watcha.tests.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.simplepeople.watcha.data.model.local.SearchLogItemEntity
import com.simplepeople.watcha.data.repository.SearchRepository
import com.simplepeople.watcha.tests.data.FakeSearchRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchLogUseCaseTest {

    private lateinit var fakeSearchRepository : SearchRepository
    private lateinit var newSearchLogItem : SearchLogItemEntity

    @Before
    fun setUp() {
        fakeSearchRepository = FakeSearchRepositoryImpl()
        newSearchLogItem = SearchLogItemEntity(
            searchedText = "Dune"
        )
        fakeSearchRepository.addNewSearch(newSearchLogItem)
    }

    @Test
    fun `Fetching last search from log`() = runTest {
        val searchLog = fakeSearchRepository.getRecentSearch().first()
        assertThat(searchLog).isNotEmpty()
    }

    @Test
    fun `Add new search to log`() = runTest {
        val searchLog = fakeSearchRepository.getRecentSearch().first()
        val currentSearchLogItem = searchLog.find {
            it.searchedText == newSearchLogItem.searchedText
        }
        assertThat(currentSearchLogItem).isNotNull()
        assertThat(currentSearchLogItem).isEqualTo(newSearchLogItem)
    }

    @Test
    fun `Remove search from log`() = runTest {
        fakeSearchRepository.removeSearch(newSearchLogItem)
        val currentSearchLog = fakeSearchRepository.getRecentSearch().first()
        val expectedNullResult = currentSearchLog.find {
            it.searchedText == newSearchLogItem.searchedText
        }
        assertThat(expectedNullResult).isNull()
    }

    @Test
    fun `Clean whole search log`() = runTest {
        fakeSearchRepository.cleanSearchLog()
        val currentSearchLog = fakeSearchRepository.getRecentSearch().first()
        assertThat(currentSearchLog).isEqualTo(emptyList<SearchLogItemEntity>())
    }
}