package com.houseofalgorithms.catalogviewer.ui.catalog

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.houseofalgorithms.catalogviewer.ui.detail.CatalogDetailScreen
import com.houseofalgorithms.catalogviewer.ui.list.CatalogListScreen
import com.houseofalgorithms.catalogviewer.viewmodel.CatalogListViewModel

/**
 * Main catalog screen that adapts to screen size:
 * - Phone: Shows list or detail separately (navigation)
 * - Tablet/Foldable: Shows list and detail side-by-side
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun CatalogScreen(
    listViewModel: CatalogListViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit = {},
    selectedItemId: String? = null,
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val windowSizeClass =
        if (activity != null) {
            calculateWindowSizeClass(activity)
        } else {
            // Fallback for non-Activity contexts
            androidx.compose.material3.windowsizeclass.WindowSizeClass
                .calculateFromSize(DpSize(360.dp, 640.dp))
        }
    val isExpandedScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded

    var selectedItem by remember { mutableStateOf(selectedItemId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Catalog Viewer",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
            )
        },
    ) { paddingValues ->
        if (isExpandedScreen) {
            // Tablet/Foldable: Two-pane layout
            Row(
                modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                // List pane (40% width)
                Column(
                    modifier =
                    Modifier
                        .weight(0.4f)
                        .fillMaxSize(),
                ) {
                    CatalogListScreen(
                        onItemClick = { itemId ->
                            selectedItem = itemId
                        },
                        viewModel = listViewModel,
                        showTopBar = false,
                    )
                }

                // Divider
                HorizontalDivider(
                    modifier =
                    Modifier
                        .width(1.dp)
                        .fillMaxHeight(),
                )

                // Detail pane (60% width)
                Column(
                    modifier =
                    Modifier
                        .weight(0.6f)
                        .fillMaxSize(),
                ) {
                    if (selectedItem != null) {
                        CatalogDetailContent(
                            itemId = selectedItem!!,
                            showTopBar = false,
                        )
                    } else {
                        CatalogDetailPlaceholder()
                    }
                }
            }
        } else {
            // Phone: Single pane with navigation
            CatalogListScreen(
                onItemClick = { itemId ->
                    selectedItem = itemId
                    onNavigateToDetail(itemId)
                },
                viewModel = listViewModel,
                showTopBar = false,
                modifier = Modifier.padding(paddingValues),
            )
        }
    }
}

@Composable
private fun CatalogDetailContent(itemId: String, showTopBar: Boolean = true) {
    // Use key to force recomposition when itemId changes
    // This ensures the ViewModel is recreated with the new itemId
    androidx.compose.runtime.key(itemId) {
        // Hilt automatically handles ViewModel creation with SavedStateHandle
        val detailViewModel: com.houseofalgorithms.catalogviewer.viewmodel.CatalogDetailViewModel =
            hiltViewModel(
                key = itemId,
            )
        // Set itemId for two-pane mode (when not using Navigation)
        // LaunchedEffect with itemId key ensures it only runs when itemId actually changes
        LaunchedEffect(itemId) {
            if (itemId.isNotEmpty()) {
                detailViewModel.setItemId(itemId)
            }
        }
        CatalogDetailScreen(
            onBackClick = { /* No-op in two-pane mode */ },
            itemId = itemId,
            viewModel = detailViewModel,
            showTopBar = showTopBar,
        )
    }
}

@Composable
private fun CatalogDetailPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = androidx.compose.ui.res.stringResource(
                com.houseofalgorithms.catalogviewer.R.string.select_item_to_view_details,
            ),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
