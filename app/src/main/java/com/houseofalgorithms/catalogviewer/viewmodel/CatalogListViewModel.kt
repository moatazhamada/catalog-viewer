package com.houseofalgorithms.catalogviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.houseofalgorithms.catalogviewer.domain.model.CatalogItem
import com.houseofalgorithms.catalogviewer.domain.repository.CatalogRepository
import com.houseofalgorithms.catalogviewer.domain.repository.FavoritesRepository
import com.houseofalgorithms.catalogviewer.domain.usecase.GetCatalogUseCase
import com.houseofalgorithms.catalogviewer.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val STOP_TIMEOUT_MILLIS = 5_000L
private val WhileUiSubscribed = SharingStarted.WhileSubscribed(stopTimeoutMillis = STOP_TIMEOUT_MILLIS)

@HiltViewModel
class CatalogListViewModel @Inject constructor(
    private val repository: CatalogRepository,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {
    private val getCatalogUseCase = GetCatalogUseCase(repository)
    private val toggleFavoriteUseCase = ToggleFavoriteUseCase(favoritesRepository)

    private val _uiState = MutableStateFlow<CatalogListUiState>(CatalogListUiState.Loading)
    val uiState: StateFlow<CatalogListUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val favorites: StateFlow<Set<String>> =
        favoritesRepository.favorites
            .stateIn(
                scope = viewModelScope,
                started = WhileUiSubscribed,
                initialValue = emptySet(),
            )

    val filteredItems: StateFlow<List<CatalogItem>> =
        combine(
            uiState,
            searchQuery.debounce(300), // Debounce search input for 300ms
            favorites,
        ) { state, query, favs ->
            when (state) {
                is CatalogListUiState.Success -> {
                    val items = state.items
                    val filtered =
                        if (query.isBlank()) {
                            items
                        } else {
                            items.filter { item ->
                                item.title.contains(query)
                            }
                        }
                    filtered
                }
                else -> emptyList()
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = emptyList(),
        )

    init {
        loadCatalog()
    }

    fun loadCatalog() {
        viewModelScope.launch {
            _uiState.value = CatalogListUiState.Loading
            val result = getCatalogUseCase()
            result.onSuccess { items ->
                Timber.d("Catalog loaded successfully: ${items.size} items")
                _uiState.value = CatalogListUiState.Success(items)
            }
            result.onFailure { error ->
                Timber.e(error, "Failed to load catalog")
                _uiState.value =
                    CatalogListUiState.Error(
                        error.message ?: "Failed to load catalog",
                    )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavorite(itemId: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(itemId)
        }
    }
}
