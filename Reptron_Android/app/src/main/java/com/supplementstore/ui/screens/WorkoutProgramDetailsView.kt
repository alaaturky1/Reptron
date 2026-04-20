package com.supplementstore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supplementstore.ui.components.FooterView

// WorkoutProgram - defined here since it's used across multiple views
data class WorkoutProgram(
    val id: Int,
    val image: String,
    val title: String,
    val description: String
) {
    companion object {
        val samples = listOf(
            WorkoutProgram(1, "Strength Training", "Strength Training", "Build maximum muscle and increase explosive power."),
            WorkoutProgram(2, "Fat Loss", "Fat Loss", "Burn calories fast with structured HIIT and cardio workouts."),
            WorkoutProgram(3, "Endurance", "Endurance", "Is the ability of an organism to exert itself and remain active for a long period of time.")
        )
        
        fun findById(id: Int): WorkoutProgram? {
            return samples.firstOrNull { it.id == id }
        }
    }
}

@Composable
fun WorkoutProgramDetailsView(
    program: WorkoutProgram,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E293B),
                        Color(0xFF0F172A)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Program Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E293B)),
                contentAlignment = Alignment.Center
            ) {
                Text(program.title, color = Color.White, fontSize = 18.sp)
            }
            
            Text(
                text = program.title,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Cyan
            )
            
            Text(
                text = program.description,
                fontSize = 16.sp,
                color = Color(0xFFCBD5E1)
            )
            
            FooterView(onNavigate = { }, modifier = Modifier.padding(vertical = 64.dp))
        }
    }
}
