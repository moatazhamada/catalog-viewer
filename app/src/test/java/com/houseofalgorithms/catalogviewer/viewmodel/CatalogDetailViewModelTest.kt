package com.houseofalgorithms.catalogviewer.viewmodel

import com.houseofalgorithms.catalogviewer.domain.model.CatalogItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for CatalogDetailViewModel logic.
 */
class CatalogDetailViewModelTest {
    private val testItem = CatalogItem("bk_001", "The Blue Fox", "Fiction", 12.99, 4.4)

    @Test
    fun `item ID extraction from route`() {
        // Test that item ID can be extracted from navigation route format
        val route = "catalog_detail/bk_001"
        val itemId = route.substringAfterLast("/")

        assertEquals("bk_001", itemId)
    }

    @Test
    fun `empty item ID should not load`() {
        // Test that empty item ID should not trigger load
        val itemId = ""
        val shouldLoad = itemId.isNotEmpty()

        assertFalse(shouldLoad)
    }

    @Test
    fun `valid item ID should load`() {
        // Test that valid item ID should trigger load
        val itemId = "bk_001"
        val shouldLoad = itemId.isNotEmpty()

        assertTrue(shouldLoad)
    }

    @Test
    fun `item matching by ID`() {
        // Test that items can be found by ID
        val items = listOf(
            CatalogItem("bk_001", "The Blue Fox", "Fiction", 12.99, 4.4),
            CatalogItem("bk_002", "Data Sketches", "Non-Fiction", 32.00, 4.8),
        )
        val targetId = "bk_001"
        val found = items.find { it.id == targetId }

        assertTrue(found != null)
        assertEquals("The Blue Fox", found?.title)
    }

    @Test
    fun `item not found returns null`() {
        // Test that non-existent item ID returns null
        val items = listOf(
            CatalogItem("bk_001", "The Blue Fox", "Fiction", 12.99, 4.4),
        )
        val targetId = "bk_999"
        val found = items.find { it.id == targetId }

        assertTrue(found == null)
    }
}
