package com.houseofalgorithms.catalogviewer.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Test
import com.houseofalgorithms.catalogviewer.data.model.CatalogItem as DataCatalogItem

/**
 * Unit tests for CatalogMapper.
 * Tests the mapping between data layer and domain layer models.
 */
class CatalogMapperTest {
    @Test
    fun `toDomain maps data item to domain item correctly`() {
        // Given
        val dataItem =
            DataCatalogItem(
                id = "bk_001",
                title = "The Blue Fox",
                category = "Fiction",
                price = 12.99,
                rating = 4.4,
            )

        // When
        val domainItem = CatalogMapper.toDomain(dataItem)

        // Then
        assertEquals("bk_001", domainItem.id)
        assertEquals("The Blue Fox", domainItem.title)
        assertEquals("Fiction", domainItem.category)
        assertEquals(12.99, domainItem.price, 0.01)
        assertEquals(4.4, domainItem.rating, 0.01)
    }

    @Test
    fun `toDomainList maps list of data items to domain items`() {
        // Given
        val dataItems =
            listOf(
                DataCatalogItem("bk_001", "The Blue Fox", "Fiction", 12.99, 4.4),
                DataCatalogItem("bk_002", "Data Sketches", "Non-Fiction", 32.00, 4.8),
            )

        // When
        val domainItems = CatalogMapper.toDomainList(dataItems)

        // Then
        assertEquals(2, domainItems.size)
        assertEquals("bk_001", domainItems[0].id)
        assertEquals("The Blue Fox", domainItems[0].title)
        assertEquals("bk_002", domainItems[1].id)
        assertEquals("Data Sketches", domainItems[1].title)
    }

    @Test
    fun `toDomainList handles empty list`() {
        // Given
        val dataItems = emptyList<DataCatalogItem>()

        // When
        val domainItems = CatalogMapper.toDomainList(dataItems)

        // Then
        assertEquals(0, domainItems.size)
    }
}
