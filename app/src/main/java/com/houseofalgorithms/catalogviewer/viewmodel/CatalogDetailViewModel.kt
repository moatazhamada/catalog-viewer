package com.houseofalgorithms.catalogviewer.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.houseofalgorithms.catalogviewer.domain.repository.CatalogRepository
import com.houseofalgorithms.catalogviewer.domain.repository.FavoritesRepository
import com.houseofalgorithms.catalogviewer.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val STOP_TIMEOUT_MILLIS = 5_000L
private val WhileUiSubscribed = SharingStarted.WhileSubscribed(stopTimeoutMillis = STOP_TIMEOUT_MILLIS)

@HiltViewModel
class CatalogDetailViewModel @Inject constructor(
    private val repository: CatalogRepository,
    private val favoritesRepository: FavoritesRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    // Get itemId from SavedStateHandle (from navigation) or use empty string as fallback
    // For two-pane mode, we need to set it manually via SavedStateHandle
    // Use MutableStateFlow to make itemId reactive for combine operations
    private val itemIdFlow = MutableStateFlow(savedStateHandle.get<String>("itemId") ?: "")

    fun setItemId(id: String) {
        if (itemIdFlow.value != id) {
            itemIdFlow.value = id
            savedStateHandle.set("itemId", id)
            loadItem()
        }
    }
    private val toggleFavoriteUseCase = ToggleFavoriteUseCase(favoritesRepository)

    private val _uiState = MutableStateFlow<CatalogDetailUiState>(CatalogDetailUiState.Loading)
    val uiState: StateFlow<CatalogDetailUiState> = _uiState.asStateFlow()

    val favorites: StateFlow<Set<String>> =
        favoritesRepository.favorites
            .stateIn(
                scope = viewModelScope,
                started = WhileUiSubscribed,
                initialValue = emptySet(),
            )

    init {
        loadItem()
        // Observe favorites and itemId changes reactively using combine
        // This ensures UI updates when either favorites or itemId changes
        // Automatically cancelled when ViewModel is cleared
        viewModelScope.launch {
            combine(
                itemIdFlow,
                favorites,
            ) { currentItemId, favs ->
                Pair(currentItemId, favs)
            }.collect { (currentItemId, favs) ->
                val currentState = _uiState.value
                if (currentState is CatalogDetailUiState.Success && currentItemId.isNotEmpty()) {
                    _uiState.value = currentState.copy(isFavorite = favs.contains(currentItemId))
                }
            }
        }
    }

    private fun loadItem() {
        viewModelScope.launch {
            val currentItemId = itemIdFlow.value // Capture current itemId value
            if (currentItemId.isEmpty()) {
                return@launch
            }
            _uiState.value = CatalogDetailUiState.Loading
            val result = repository.getCatalog()
            result.onSuccess { items ->
                val item = items.find { it.id == currentItemId }
                if (item != null) {
                    val isFavorite = favoritesRepository.isFavorite(currentItemId)
                    Timber.d("Item loaded successfully: ${item.title}")
                    _uiState.value = CatalogDetailUiState.Success(item, isFavorite)
                } else {
                    Timber.w("Item not found: $currentItemId")
                    _uiState.value = CatalogDetailUiState.Error("Item not found")
                }
            }
            result.onFailure { error ->
                Timber.e(error, "Failed to load item: $currentItemId")
                _uiState.value =
                    CatalogDetailUiState.Error(
                        error.message ?: "Failed to load item",
                    )
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentItemId = itemIdFlow.value // Capture current itemId value
            if (currentItemId.isNotEmpty()) {
                toggleFavoriteUseCase(currentItemId)
                // State will be updated automatically via favorites Flow observation
            }
        }
    }
}
