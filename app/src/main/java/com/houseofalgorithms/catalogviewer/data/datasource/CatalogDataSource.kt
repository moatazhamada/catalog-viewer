package com.houseofalgorithms.catalogviewer.data.datasource

import com.houseofalgorithms.catalogviewer.data.model.CatalogResponse

/**
 * Data source interface for catalog data.
 * Allows swapping between different data sources (local JSON, remote API, Room DB, etc.)
 * without affecting the repository or domain layer.
 */
interface CatalogDataSource {
    suspend fun getCatalog(): Result<CatalogResponse>
}
