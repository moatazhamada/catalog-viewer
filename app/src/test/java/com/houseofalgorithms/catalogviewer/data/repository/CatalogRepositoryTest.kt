package com.houseofalgorithms.catalogviewer.data.repository

import com.houseofalgorithms.catalogviewer.data.model.CatalogResponse
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Unit tests for CatalogRepository JSON parsing logic.
 * Tests the JSON deserialization that the repository uses.
 */
class CatalogRepositoryTest {
    private val json =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

    @Test
    fun `parse valid catalog JSON successfully`() {
        // Given
        val jsonString =
            """
            {
              "updatedAt": "2025-01-15T10:00:00Z",
              "items": [
                { "id": "bk_001", "title": "The Blue Fox", "category": "Fiction", "price": 12.99, "rating": 4.4 },
                { "id": "bk_002", "title": "Data Sketches", "category": "Non-Fiction", "price": 32.00, "rating": 4.8 }
              ]
            }
            """.trimIndent()

        // When
        val response = json.decodeFromString<CatalogResponse>(jsonString)

        // Then
        assertNotNull(response)
        assertEquals("2025-01-15T10:00:00Z", response.updatedAt)
        assertEquals(2, response.items.size)
        assertEquals("bk_001", response.items[0].id)
        assertEquals("The Blue Fox", response.items[0].title)
        assertEquals("Fiction", response.items[0].category)
        assertEquals(12.99, response.items[0].price, 0.01)
        assertEquals(4.4, response.items[0].rating, 0.01)
    }

    @Test
    fun `parse empty items array`() {
        // Given
        val jsonString =
            """
            {
              "updatedAt": "2025-01-15T10:00:00Z",
              "items": []
            }
            """.trimIndent()

        // When
        val response = json.decodeFromString<CatalogResponse>(jsonString)

        // Then
        assertNotNull(response)
        assertEquals(0, response.items.size)
    }

    @Test
    fun `parse JSON with all required fields`() {
        // Given
        val jsonString =
            """
            {
              "updatedAt": "2025-01-15T10:00:00Z",
              "items": [
                { "id": "bk_001", "title": "The Blue Fox", "category": "Fiction", "price": 12.99, "rating": 4.4 }
              ]
            }
            """.trimIndent()

        // When
        val response = json.decodeFromString<CatalogResponse>(jsonString)

        // Then
        val item = response.items[0]
        assertEquals("bk_001", item.id)
        assertEquals("The Blue Fox", item.title)
        assertEquals("Fiction", item.category)
        assertEquals(12.99, item.price, 0.01)
        assertEquals(4.4, item.rating, 0.01)
    }
}
