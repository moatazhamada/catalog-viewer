package com.houseofalgorithms.catalogviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.houseofalgorithms.catalogviewer.ui.navigation.NavGraph
import com.houseofalgorithms.catalogviewer.ui.theme.CatalogViewerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatalogViewerTheme {
                NavGraph()
            }
        }
    }
}
