package com.houseofalgorithms.catalogviewer.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.houseofalgorithms.catalogviewer.R
import com.houseofalgorithms.catalogviewer.ui.common.EmptyState
import com.houseofalgorithms.catalogviewer.ui.common.ErrorState
import com.houseofalgorithms.catalogviewer.viewmodel.CatalogListUiState
import com.houseofalgorithms.catalogviewer.viewmodel.CatalogListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogListScreen(
    onItemClick: (String) -> Unit,
    viewModel: CatalogListViewModel = hiltViewModel(),
    showTopBar: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredItems by viewModel.filteredItems.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text(stringResource(R.string.catalog_viewer)) },
                )
            }
        },
    ) { paddingValues ->
        Box(
            modifier =
            modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when (val state = uiState) {
                is CatalogListUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                is CatalogListUiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { viewModel.loadCatalog() },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is CatalogListUiState.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        SearchBar(
                            query = searchQuery,
                            onQueryChange = { viewModel.updateSearchQuery(it) },
                        )
                        if (filteredItems.isEmpty()) {
                            EmptyState(
                                message =
                                if (searchQuery.isBlank()) {
                                    stringResource(R.string.no_items_available)
                                } else {
                                    stringResource(R.string.no_items_found, searchQuery)
                                },
                                modifier = Modifier.fillMaxSize(),
                            )
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(bottom = 0.dp),
                            ) {
                                items(
                                    items = filteredItems,
                                    key = { it.id },
                                ) { item ->
                                    CatalogListItem(
                                        item = item,
                                        isFavorite = favorites.contains(item.id),
                                        onItemClick = { onItemClick(item.id) },
                                        onFavoriteClick = { viewModel.toggleFavorite(item.id) },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CatalogListScreenPreview() {
    androidx.compose.material3.MaterialTheme {
        CatalogListScreen(
            onItemClick = { },
            showTopBar = true,
        )
    }
}
