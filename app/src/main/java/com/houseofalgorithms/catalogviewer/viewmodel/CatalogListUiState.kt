package com.houseofalgorithms.catalogviewer.viewmodel

import com.houseofalgorithms.catalogviewer.domain.model.CatalogItem

sealed class CatalogListUiState {
    data object Loading : CatalogListUiState()

    data class Success(val items: List<CatalogItem>) : CatalogListUiState()

    data class Error(val message: String) : CatalogListUiState()
}
