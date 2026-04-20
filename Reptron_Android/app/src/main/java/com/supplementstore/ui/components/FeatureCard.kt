package com.supplementstore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FeatureCard(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Color(0xFF1E293B).copy(alpha = 0.8f),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                1.dp,
                Color.Cyan.copy(alpha = 0.2f),
                RoundedCornerShape(16.dp)
            )
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Unspecified, // Gradient handled by tinting
                modifier = Modifier.size(40.dp)
            )
            
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = description,
                fontSize = 15.sp,
                color = Color(0xFF94A3B8),
                textAlign = TextAlign.Center
            )
        }
    }
}

// Helper function to get icon from string
fun getFeatureIcon(iconName: String): ImageVector {
    return when (iconName) {
        "star.fill" -> Icons.Default.Star
        "trophy.fill" -> Icons.Default.EmojiEvents
        "shippingbox.fill" -> Icons.Default.LocalShipping
        "shield.fill" -> Icons.Default.Shield
        else -> Icons.Default.Info
    }
}
