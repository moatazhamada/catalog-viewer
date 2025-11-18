package com.houseofalgorithms.catalogviewer.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.houseofalgorithms.catalogviewer.R
import com.houseofalgorithms.catalogviewer.ui.common.ErrorState
import com.houseofalgorithms.catalogviewer.viewmodel.CatalogDetailUiState
import com.houseofalgorithms.catalogviewer.viewmodel.CatalogDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogDetailScreen(
    onBackClick: () -> Unit,
    itemId: String? = null,
    viewModel: CatalogDetailViewModel? = null,
    showTopBar: Boolean = true,
) {
    // Hilt automatically provides ViewModel, but we can also accept it as parameter for testing
    val detailViewModel =
        viewModel
            ?: androidx.hilt.navigation.compose.hiltViewModel<CatalogDetailViewModel>(
                key = itemId,
            )

    val uiState by detailViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text(stringResource(R.string.item_details)) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                            )
                        }
                    },
                )
            }
        },
        floatingActionButton = {
            when (val state = uiState) {
                is CatalogDetailUiState.Success -> {
                    FloatingActionButton(
                        onClick = { detailViewModel.toggleFavorite() },
                    ) {
                        Icon(
                            imageVector =
                            if (state.isFavorite) {
                                Icons.Default.Favorite
                            } else {
                                Icons.Default.FavoriteBorder
                            },
                            contentDescription =
                            if (state.isFavorite) {
                                stringResource(R.string.unfavorite_item)
                            } else {
                                stringResource(R.string.favorite_item)
                            },
                        )
                    }
                }
                else -> { /* No FAB for other states */ }
            }
        },
    ) { paddingValues ->
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when (val state = uiState) {
                is CatalogDetailUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                is CatalogDetailUiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { /* Could reload if needed */ },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is CatalogDetailUiState.Success -> {
                    val item = state.item
                    Column(
                        modifier =
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                    ) {
                        // Hero section with title
                        Box(
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    text = item.category,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }

                        // Main content card
                        Card(
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = MaterialTheme.shapes.large,
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                verticalArrangement = Arrangement.spacedBy(20.dp),
                            ) {
                                // Price section
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    Text(
                                        text = stringResource(R.string.price),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                    Text(
                                        text = "$${String.format("%.2f", item.price)}",
                                        style = MaterialTheme.typography.displaySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                }

                                HorizontalDivider()

                                // Rating section
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(4.dp),
                                    ) {
                                        Text(
                                            text = stringResource(R.string.rating),
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        ) {
                                            Text(
                                                text = "⭐",
                                                style = MaterialTheme.typography.headlineMedium,
                                            )
                                            Text(
                                                text = String.format("%.1f", item.rating),
                                                style = MaterialTheme.typography.headlineMedium,
                                                fontWeight = FontWeight.SemiBold,
                                            )
                                        }
                                    }
                                }

                                HorizontalDivider()

                                // ID section
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    Text(
                                        text = "Item ID",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                    Text(
                                        text = item.id,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                }
                            }
                        }

                        // Spacer at bottom
                        Box(modifier = Modifier.padding(24.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CatalogDetailScreenPreview() {
    androidx.compose.material3.MaterialTheme {
        CatalogDetailScreen(
            onBackClick = { },
            itemId = "preview_001",
            showTopBar = true,
        )
    }
}
