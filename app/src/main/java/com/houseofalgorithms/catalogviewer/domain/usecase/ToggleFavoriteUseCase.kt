package com.houseofalgorithms.catalogviewer.domain.usecase

import com.houseofalgorithms.catalogviewer.domain.repository.FavoritesRepository

class ToggleFavoriteUseCase(
    private val favoritesRepository: FavoritesRepository,
) {
    suspend operator fun invoke(itemId: String): Boolean {
        return favoritesRepository.toggleFavorite(itemId)
    }
}
