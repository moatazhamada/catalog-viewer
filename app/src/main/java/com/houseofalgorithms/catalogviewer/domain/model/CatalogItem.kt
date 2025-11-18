package com.houseofalgorithms.catalogviewer.domain.model

/**
 * Domain model for catalog items.
 * This is the domain representation, independent of data layer implementations.
 */
data class CatalogItem(
    val id: String,
    val title: String,
    val category: String,
    val price: Double,
    val rating: Double,
)
