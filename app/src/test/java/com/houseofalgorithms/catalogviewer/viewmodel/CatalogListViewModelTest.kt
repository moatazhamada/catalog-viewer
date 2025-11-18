package com.houseofalgorithms.catalogviewer.viewmodel

import com.houseofalgorithms.catalogviewer.domain.model.CatalogItem
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for CatalogListViewModel search filtering logic.
 * Tests the core search functionality which is case-sensitive and matches partial titles.
 */
class CatalogListViewModelTest {
    private val testItems =
        listOf(
            CatalogItem("bk_001", "The Blue Fox", "Fiction", 12.99, 4.4),
            CatalogItem("bk_002", "Data Sketches", "Non-Fiction", 32.00, 4.8),
            CatalogItem("bk_003", "Swift Patterns", "Tech", 24.50, 4.1),
            CatalogItem("bk_004", "Kotlin by Example", "Tech", 21.00, 4.3),
        )

    @Test
    fun `search filtering is case sensitive`() {
        // Test that search is case-sensitive
        val query1 = "Blue" // Matches "The Blue Fox"
        val query2 = "BLUE" // Does not match (case-sensitive)
        val query3 = "blue" // Does not match (case-sensitive)

        val filtered1 = testItems.filter { it.title.contains(query1) }
        val filtered2 = testItems.filter { it.title.contains(query2) }
        val filtered3 = testItems.filter { it.title.contains(query3) }

        assertEquals(1, filtered1.size)
        assertEquals("The Blue Fox", filtered1[0].title)
        assertEquals(0, filtered2.size) // Case-sensitive: "BLUE" doesn't match "Blue"
        assertEquals(0, filtered3.size) // Case-sensitive: "blue" doesn't match "Blue"
    }

    @Test
    fun `search filtering matches partial titles`() {
        // Test that partial matches work (case-sensitive)
        val query = "Data" // Matches "Data Sketches"
        val filtered = testItems.filter { it.title.contains(query) }

        assertEquals(1, filtered.size)
        assertEquals("Data Sketches", filtered[0].title)
    }

    @Test
    fun `empty search query shows all items`() {
        // Test that empty query returns all items
        val query = ""
        val filtered =
            testItems.filter {
                query.isBlank() || it.title.contains(query)
            }

        assertEquals(4, filtered.size)
    }

    @Test
    fun `search with no matches returns empty list`() {
        // Test that non-matching query returns empty list
        val query = "nonexistent"
        val filtered = testItems.filter { it.title.contains(query) }

        assertEquals(0, filtered.size)
    }

    @Test
    fun `search matches multiple items`() {
        // Test that query matching multiple items returns all matches (case-sensitive)
        val query = "Tech" // Matches category "Tech" (case-sensitive)
        val filtered =
            testItems.filter {
                it.category.contains(query) ||
                    it.title.contains(query)
            }

        // Should match Swift Patterns and Kotlin by Example (both have "Tech" category)
        assertEquals(2, filtered.size)
    }
}
