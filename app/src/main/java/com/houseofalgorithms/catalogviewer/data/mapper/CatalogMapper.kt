package com.houseofalgorithms.catalogviewer.data.mapper

import com.houseofalgorithms.catalogviewer.data.model.CatalogItem as DataCatalogItem
import com.houseofalgorithms.catalogviewer.domain.model.CatalogItem as DomainCatalogItem

/**
 * Mapper between data and domain models.
 * Keeps data and domain layers independent.
 */
object CatalogMapper {
    fun toDomain(dataItem: DataCatalogItem): DomainCatalogItem {
        return DomainCatalogItem(
            id = dataItem.id,
            title = dataItem.title,
            category = dataItem.category,
            price = dataItem.price,
            rating = dataItem.rating,
        )
    }

    fun toDomainList(dataItems: List<DataCatalogItem>): List<DomainCatalogItem> {
        return dataItems.map { toDomain(it) }
    }
}
