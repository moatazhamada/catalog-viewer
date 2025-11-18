package com.houseofalgorithms.catalogviewer.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.houseofalgorithms.catalogviewer.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Data layer implementation of domain favorites repository interface.
 */
class FavoritesDataSource(private val context: Context) : FavoritesRepository {
    // Initialize DataStore as instance property to avoid issues during Hilt initialization
    // This prevents the top-level extension property from being initialized too early
    private val Context.dataStoreInstance: DataStore<Preferences> by preferencesDataStore(name = "favorites")
    private val dataStore: DataStore<Preferences> get() = context.dataStoreInstance

    private val favoritesKey = stringSetPreferencesKey("favorite_ids")

    override val favorites: Flow<Set<String>> =
        dataStore.data
            .catch { exception ->
                // Handle corrupted DataStore file
                if (exception is IOException) {
                    emit(androidx.datastore.preferences.core.emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[favoritesKey] ?: emptySet()
            }

    override suspend fun toggleFavorite(id: String): Boolean {
        return try {
            var result = false
            dataStore.edit { preferences ->
                val currentFavorites = preferences[favoritesKey] ?: emptySet()
                val newFavorites =
                    if (currentFavorites.contains(id)) {
                        currentFavorites - id
                    } else {
                        currentFavorites + id
                    }
                preferences[favoritesKey] = newFavorites
                result = newFavorites.contains(id)
            }
            result
        } catch (e: IOException) {
            // If DataStore is corrupted, clear it and retry
            try {
                context.deleteSharedPreferences("favorites.preferences_pb")
                // Retry once after clearing
                var result = false
                dataStore.edit { preferences ->
                    val newFavorites = setOf(id)
                    preferences[favoritesKey] = newFavorites
                    result = true
                }
                result
            } catch (retryException: Exception) {
                false
            }
        }
    }

    override suspend fun isFavorite(id: String): Boolean {
        return try {
            dataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(androidx.datastore.preferences.core.emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { preferences ->
                    preferences[favoritesKey]?.contains(id) ?: false
                }
                .first()
        } catch (e: Exception) {
            false
        }
    }
}
