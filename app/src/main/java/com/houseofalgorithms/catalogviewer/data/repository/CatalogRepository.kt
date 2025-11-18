package com.houseofalgorithms.catalogviewer.data.repository

import com.houseofalgorithms.catalogviewer.data.datasource.CatalogDataSource
import com.houseofalgorithms.catalogviewer.data.mapper.CatalogMapper
import com.houseofalgorithms.catalogviewer.domain.model.CatalogItem
import com.houseofalgorithms.catalogviewer.domain.repository.CatalogRepository as DomainCatalogRepository

/**
 * Data layer implementation of domain repository interface.
 * Uses data source abstraction to fetch data and maps to domain models.
 * This allows easy swapping of data sources (local, remote, Room, etc.)
 * without affecting the domain or presentation layers.
 */
class CatalogRepositoryImpl(
    private val catalogDataSource: CatalogDataSource,
) : DomainCatalogRepository {
    override suspend fun getCatalog(): Result<List<CatalogItem>> {
        return catalogDataSource.getCatalog()
            .fold(
                onSuccess = { response ->
                    Result.success(CatalogMapper.toDomainList(response.items))
                },
                onFailure = { error ->
                    Result.failure(error)
                },
            )
    }
}
