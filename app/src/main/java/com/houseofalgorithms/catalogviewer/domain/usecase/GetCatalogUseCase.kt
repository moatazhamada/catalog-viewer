package com.houseofalgorithms.catalogviewer.domain.usecase

import com.houseofalgorithms.catalogviewer.domain.model.CatalogItem
import com.houseofalgorithms.catalogviewer.domain.repository.CatalogRepository

class GetCatalogUseCase(
    private val repository: CatalogRepository,
) {
    suspend operator fun invoke(): Result<List<CatalogItem>> {
        return repository.getCatalog()
    }
}
