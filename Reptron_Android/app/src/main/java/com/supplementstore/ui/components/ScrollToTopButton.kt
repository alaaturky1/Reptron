package com.supplementstore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ScrollToTopButton(
    scrollOffset: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Floating button to scroll to top
    // Shows when scrollOffset > 300 (matching Swift implementation)
    if (scrollOffset > 300) {
        FloatingActionButton(
            onClick = onClick,
            modifier = modifier.size(56.dp),
            containerColor = Color.Cyan,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.ArrowUpward,
                contentDescription = "Scroll to top",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
