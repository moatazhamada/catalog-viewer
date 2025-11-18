package com.houseofalgorithms.catalogviewer.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CatalogResponse(
    val updatedAt: String,
    val items: List<CatalogItem>,
)
