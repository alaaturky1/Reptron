package com.supplementstore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Testimonial(
    val id: Int,
    val name: String,
    val role: String,
    val content: String,
    val image: String
)

@Composable
fun TestimonialCard(
    testimonial: Testimonial,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E293B).copy(alpha = 0.8f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Profile Image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E293B))
                    .border(
                        3.dp,
                        Color.Cyan.copy(alpha = 0.3f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Note: Load image from resources
                Text(
                    text = testimonial.name.take(1),
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Quote
            Text(
                text = "\"${testimonial.content}\"",
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                color = Color(0xFFCBD5E1),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            // Name
            Text(
                text = testimonial.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Cyan // Gradient would need custom text composable
            )
            
            // Role
            Text(
                text = testimonial.role,
                fontSize = 16.sp,
                color = Color(0xFF94A3B8)
            )
        }
        
        // Top border gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
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
