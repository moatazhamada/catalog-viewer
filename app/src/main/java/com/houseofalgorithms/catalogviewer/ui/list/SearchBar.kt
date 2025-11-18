package com.houseofalgorithms.catalogviewer.ui.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier =
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = androidx.compose.ui.res.stringResource(
                    com.houseofalgorithms.catalogviewer.R.string.search,
                ),
            )
        },
        placeholder = {
            androidx.compose.material3.Text(
                androidx.compose.ui.res.stringResource(com.houseofalgorithms.catalogviewer.R.string.search_by_title),
            )
        },
        singleLine = true,
        colors =
        OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchBarPreview() {
    androidx.compose.material3.MaterialTheme {
        SearchBar(
            query = "Sample search",
            onQueryChange = { },
        )
    }
}
