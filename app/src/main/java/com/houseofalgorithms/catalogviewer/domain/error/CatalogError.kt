package com.houseofalgorithms.catalogviewer.domain.error

/**
 * Represents errors that can occur in the catalog domain.
 */
sealed class CatalogError(message: String) : Exception(message) {
    /**
     * Failed to load catalog data.
     */
    data class LoadError(override val message: String = "Failed to load catalog") : CatalogError(message)

    /**
     * Item not found in catalog.
     */
    data class ItemNotFound(override val message: String = "Item not found") : CatalogError(message)

    /**
     * Failed to save favorites.
     */
    data class SaveError(override val message: String = "Failed to save favorites") : CatalogError(message)
}
