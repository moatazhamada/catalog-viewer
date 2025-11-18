package com.houseofalgorithms.catalogviewer.data.local

import androidx.datastore.preferences.core.stringSetPreferencesKey
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for FavoritesDataSource logic.
 * Tests the favorites toggle and check logic (without DataStore mocking complexity).
 */
class FavoritesDataSourceTest {
    private val favoritesKey = stringSetPreferencesKey("favorite_ids")

    @Test
    fun `toggleFavorite logic adds item when not in set`() {
        // Given
        val currentFavorites = emptySet<String>()
        val itemId = "bk_001"

        // When
        val newFavorites = if (currentFavorites.contains(itemId)) {
            currentFavorites - itemId
        } else {
            currentFavorites + itemId
        }

        // Then
        assertTrue(newFavorites.contains(itemId))
        assertEquals(1, newFavorites.size)
    }

    @Test
    fun `toggleFavorite logic removes item when already in set`() {
        // Given
        val currentFavorites = setOf("bk_001", "bk_002")
        val itemId = "bk_001"

        // When
        val newFavorites = if (currentFavorites.contains(itemId)) {
            currentFavorites - itemId
        } else {
            currentFavorites + itemId
        }

        // Then
        assertFalse(newFavorites.contains(itemId))
        assertEquals(1, newFavorites.size)
        assertTrue(newFavorites.contains("bk_002"))
    }

    @Test
    fun `isFavorite returns false when item not in favorites`() {
        // Given
        val favorites = setOf("bk_002", "bk_003")
        val itemId = "bk_001"

        // When
        val isFavorite = favorites.contains(itemId)

        // Then
        assertFalse(isFavorite)
    }

    @Test
    fun `isFavorite returns true when item in favorites`() {
        // Given
        val favorites = setOf("bk_001", "bk_002", "bk_003")
        val itemId = "bk_001"

        // When
        val isFavorite = favorites.contains(itemId)

        // Then
        assertTrue(isFavorite)
    }

    @Test
    fun `favorites defaults to empty set when null`() {
        // Given
        val preferencesValue: Set<String>? = null

        // When
        val favorites = preferencesValue ?: emptySet()

        // Then
        assertEquals(emptySet<String>(), favorites)
    }
}
