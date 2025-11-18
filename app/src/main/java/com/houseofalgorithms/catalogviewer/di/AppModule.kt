package com.houseofalgorithms.catalogviewer.di

import android.content.Context
import com.houseofalgorithms.catalogviewer.data.datasource.CatalogDataSource
import com.houseofalgorithms.catalogviewer.data.datasource.LocalCatalogDataSource
import com.houseofalgorithms.catalogviewer.data.local.FavoritesDataSource
import com.houseofalgorithms.catalogviewer.data.repository.CatalogRepositoryImpl
import com.houseofalgorithms.catalogviewer.domain.repository.CatalogRepository
import com.houseofalgorithms.catalogviewer.domain.repository.FavoritesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * Provides the catalog data source.
     * Can be easily swapped to RemoteCatalogDataSource, RoomCatalogDataSource, etc.
     */
    @Provides
    @Singleton
    fun provideCatalogDataSource(@ApplicationContext context: Context): CatalogDataSource {
        return LocalCatalogDataSource(context)
    }

    /**
     * Provides the catalog repository.
     * Uses the data source abstraction, allowing easy data source swapping.
     */
    @Provides
    @Singleton
    fun provideCatalogRepository(catalogDataSource: CatalogDataSource): CatalogRepository {
        return CatalogRepositoryImpl(catalogDataSource)
    }

    /**
     * Provides the favorites repository.
     * FavoritesDataSource implements the repository interface directly.
     */
    @Provides
    @Singleton
    fun provideFavoritesRepository(@ApplicationContext context: Context): FavoritesRepository {
        return FavoritesDataSource(context)
    }
}
