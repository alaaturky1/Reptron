package com.supplementstore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryButton(
    id: String,
    name: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .background(
                brush = if (isSelected) {
                    Brush.horizontalGradient(
                        colors = listOf(Color.Cyan, Color(0xFF00BCD4))
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Cyan.copy(alpha = 0.1f),
                            Color.Cyan.copy(alpha = 0.05f)
                        )
                    )
                },
                shape = RoundedCornerShape(12.dp)
            )
            .then(
                if (!isSelected) {
                    Modifier.border(
                        1.dp,
                        Color.Cyan.copy(alpha = 0.3f),
                        RoundedCornerShape(12.dp)
                    )
                } else Modifier
            )
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color(0xFF0F172A) else Color.Cyan,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) Color(0xFF0F172A) else Color.Cyan
            )
        }
    }
}

// Helper function to get icon from string (simplified version)
fun getCategoryIcon(iconName: String): ImageVector {
    return when (iconName) {
        "bolt.fill" -> Icons.Default.Bolt
        "pills.fill" -> Icons.Default.Medication
        "dumbbell.fill" -> Icons.Default.FitnessCenter
        else -> Icons.Default.Category
    }
}
