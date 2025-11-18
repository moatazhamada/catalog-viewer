package com.houseofalgorithms.catalogviewer.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Domain repository interface for favorites.
 * Domain layer defines the contract, data layer implements it.
 */
interface FavoritesRepository {
    val favorites: Flow<Set<String>>

    suspend fun toggleFavorite(id: String): Boolean

    suspend fun isFavorite(id: String): Boolean
}
