package com.supplementstore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supplementstore.viewmodels.AiCoachViewModel

val BackgroundDark = Color(0xFF0B0E14)
val CyanNeon = Color(0xFF00E5FF)
val CardBackground = Color(0xFF161E2E)
val ButtonDarkBackground = Color(0xFF0B1924)
val ChipUnselectedBackground = Color(0xFF2A2F3A)

@Composable
fun AiCoachDashboardScreen(
    viewModel: AiCoachViewModel,
    onStartWorkoutClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val selectedLevel by viewModel.selectedLevel.collectAsState()
    val selectedExercise by viewModel.selectedExercise.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "AI Coach",
            color = CyanNeon,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp, bottom = 32.dp)
        )

        Text(
            text = "Welcome back, Athlete",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "AI Fitness Coach",
            color = Color.Gray,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
        )

        Button(
            onClick = onStartWorkoutClick,
            colors = ButtonDefaults.buttonColors(containerColor = CyanNeon),
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Start Workout",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onHistoryClick,
            colors = ButtonDefaults.buttonColors(containerColor = ButtonDarkBackground),
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = CyanNeon,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Workout history",
                color = CyanNeon,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        SectionTitle("Choose language")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SelectableChip(text = "EN", isSelected = selectedLanguage == "EN") { viewModel.updateLanguage("EN") }
            }
            Box(modifier = Modifier.weight(1f)) {
                SelectableChip(text = "AR", isSelected = selectedLanguage == "AR") { viewModel.updateLanguage("AR") }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Choose level")
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()), // شيلنا fillMaxWidth من هنا
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SelectableChip(text = "Beginner", isSelected = selectedLevel == "Beginner") { viewModel.updateLevel("Beginner") }
            SelectableChip(text = "Intermediate", isSelected = selectedLevel == "Intermediate") { viewModel.updateLevel("Intermediate") }
            SelectableChip(text = "Professional", isSelected = selectedLevel == "Professional") { viewModel.updateLevel("Professional") }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Choose exercise")
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()), // وشيلنا fillMaxWidth من هنا
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SelectableChip(text = "Squat", isSelected = selectedExercise == "Squat") { viewModel.updateExercise("Squat") }
            SelectableChip(text = "Pushup", isSelected = selectedExercise == "Pushup") { viewModel.updateExercise("Pushup") }
            SelectableChip(text = "Plank", isSelected = selectedExercise == "Plank") { viewModel.updateExercise("Plank") }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Last workout",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        WorkoutHistoryCard(date = "18 Apr 2026 at 12:48 AM", reps = 0, score = 100)

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun SelectableChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) CyanNeon else ChipUnselectedBackground
        ),
        shape = RoundedCornerShape(32.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        modifier = Modifier.height(48.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Black else Color.LightGray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun AiCoachHistoryScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 32.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .background(Color.White, shape = CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "History",
                color = CyanNeon,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 40.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { WorkoutHistoryCard(date = "18 Apr 2026 at 12:48 AM", reps = 0, score = 100) }
            item { WorkoutHistoryCard(date = "14 Apr 2026 at 12:39 AM", reps = 0, score = 100) }
        }
    }
}

@Composable
fun WorkoutHistoryCard(date: String, reps: Int, score: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$reps reps",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Score",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Score $score",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = date,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}