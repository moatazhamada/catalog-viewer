package com.houseofalgorithms.catalogviewer.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CatalogItem(
    val id: String,
    val title: String,
    val category: String,
    val price: Double,
    val rating: Double,
)
