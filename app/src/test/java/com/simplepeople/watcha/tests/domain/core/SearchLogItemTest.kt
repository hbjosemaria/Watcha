package com.simplepeople.watcha.tests.domain.core

import com.simplepeople.watcha.data.model.local.SearchLogItemEntity
import com.simplepeople.watcha.domain.core.SearchLogItem
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchLogItemTest {

    private lateinit var baseSearchLogItem : SearchLogItem

    @Before
    fun setUp() {
        baseSearchLogItem = SearchLogItem(
            searchedText = "The great Android Developer ascension."
        )
    }

    @Test
    fun `Mapping SearchLogItem to Entity`() {
        val expectedSearchLogItemEntity = SearchLogItemEntity(
            searchedText = baseSearchLogItem.searchedText
        )

        assertEquals(expectedSearchLogItemEntity, baseSearchLogItem.toEntity())
    }
}