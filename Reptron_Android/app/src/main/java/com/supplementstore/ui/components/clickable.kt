package com.supplementstore.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

// Extension function for clickable modifier compatibility
fun Modifier.clickable(onClick: () -> Unit): Modifier = this.clickable(onClick = onClick)
