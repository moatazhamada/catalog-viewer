package com.houseofalgorithms.catalogviewer.viewmodel

import com.houseofalgorithms.catalogviewer.domain.model.CatalogItem

sealed class CatalogDetailUiState {
    data object Loading : CatalogDetailUiState()

    data class Success(val item: CatalogItem, val isFavorite: Boolean) : CatalogDetailUiState()

    data class Error(val message: String) : CatalogDetailUiState()
}
