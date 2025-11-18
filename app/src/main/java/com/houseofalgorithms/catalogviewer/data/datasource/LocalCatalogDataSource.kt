package com.houseofalgorithms.catalogviewer.data.datasource

import android.content.Context
import com.houseofalgorithms.catalogviewer.data.model.CatalogResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.IOException

/**
 * Local data source implementation that reads from raw resources.
 * Can be easily replaced with RemoteCatalogDataSource, RoomCatalogDataSource, etc.
 */
class LocalCatalogDataSource(
    private val context: Context,
) : CatalogDataSource {
    private val json =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

    override suspend fun getCatalog(): Result<CatalogResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val jsonString =
                    context.resources
                        .openRawResource(com.houseofalgorithms.catalogviewer.R.raw.catalog)
                        .bufferedReader()
                        .use { it.readText() }

                val response = json.decodeFromString<CatalogResponse>(jsonString)
                Timber.d("Successfully loaded catalog with ${response.items.size} items")
                Result.success(response)
            } catch (e: IOException) {
                Timber.e(e, "IO error while reading catalog file")
                Result.failure(e)
            } catch (e: Exception) {
                Timber.e(e, "Unexpected error while loading catalog")
                Result.failure(e)
            }
        }
    }
}
