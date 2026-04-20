package com.supplementstore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun WorkoutCard(
    image: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(300.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E293B).copy(alpha = 0.8f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFF1E293B))
            ) {
                // Note: Load image from resources
                // AsyncImage(model = image, contentDescription = title)
                Text(
                    text = title,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            // Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color(0xFF94A3B8),
                    maxLines = 3
                )
            }
        }
        
        // Top border gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Cyan,
                            Color(0xFF00BCD4),
                            Color(0xFF0097A7)
                        )
                    )
                )
        )
    }
}
