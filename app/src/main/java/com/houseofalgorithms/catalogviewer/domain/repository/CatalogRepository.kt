package com.houseofalgorithms.catalogviewer.domain.repository

import com.houseofalgorithms.catalogviewer.domain.model.CatalogItem

/**
 * Domain repository interface.
 * Domain layer defines the contract, data layer implements it.
 */
interface CatalogRepository {
    suspend fun getCatalog(): Result<List<CatalogItem>>
}
