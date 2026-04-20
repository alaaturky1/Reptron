package com.supplementstore.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.supplementstore.R // 👈 تأكد إن ده مسار الـ Resources بتاعك صح
import com.supplementstore.navigation.AppRoute
import com.supplementstore.viewmodels.AuthState
import com.supplementstore.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterView(
    userViewModel: UserViewModel,
    onNavigate: (AppRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var localErrorMessage by remember { mutableStateOf<String?>(null) }

    val authState by userViewModel.authState.collectAsStateWithLifecycle()
    val isLoading = authState is AuthState.Loading
    val vmErrorMessage = (authState as? AuthState.Error)?.message
    val isSuccess = authState is AuthState.Success

    val passwordMatch = password.isEmpty() || confirmPassword.isEmpty() || password == confirmPassword
    val isFormValid = name.isNotEmpty() &&
            email.isNotEmpty() &&
            password.isNotEmpty() &&
            confirmPassword.isNotEmpty() &&
            phone.isNotEmpty() &&
            passwordMatch &&
            password.length >= 6

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            userViewModel.resetAuthState()
            onNavigate(AppRoute.Home)
        }
    }

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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Create Account",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Cyan
                )

                Text(
                    text = "Sign up to get started",
                    fontSize = 18.sp,
                    color = Color(0xFFCBD5E1).copy(alpha = 0.9f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF0F172A).copy(alpha = 0.3f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Full Name", color = Color.White) },
                    placeholder = { Text("Enter your name", color = Color(0xFFCBD5E1).copy(alpha = 0.7f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.8f),
                        unfocusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.8f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.Cyan.copy(alpha = 0.3f),
                        unfocusedBorderColor = Color.Cyan.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email", color = Color.White) },
                    placeholder = { Text("Enter your email", color = Color(0xFFCBD5E1).copy(alpha = 0.7f)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.8f),
                        unfocusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.8f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.Cyan.copy(alpha = 0.3f),
                        unfocusedBorderColor = Color.Cyan.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Phone Number", color = Color.White) },
                    placeholder = { Text("Enter your phone", color = Color(0xFFCBD5E1).copy(alpha = 0.7f)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.8f),
                        unfocusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.8f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.Cyan.copy(alpha = 0.3f),
                        unfocusedBorderColor = Color.Cyan.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Password", color = Color.White) },
                        placeholder = { Text("Enter your password", color = Color(0xFFCBD5E1).copy(alpha = 0.7f)) },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = Color(0xFFCBD5E1)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.8f),
                            unfocusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.8f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.Cyan.copy(alpha = 0.3f),
                            unfocusedBorderColor = Color.Cyan.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    if (password.isNotEmpty() && password.length < 6) {
                        Text(
                            text = "Password must be at least 6 characters",
                            fontSize = 12.sp,
                            color = Color.Yellow,
                            modifier = Modifier.padding(start = 12.dp, top = 4.dp)
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Confirm Password", color = Color.White) },
                        placeholder = { Text("Confirm your password", color = Color(0xFFCBD5E1).copy(alpha = 0.7f)) },
                        visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                Icon(
                                    imageVector = if (showConfirmPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = Color(0xFFCBD5E1)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.8f),
                            unfocusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.8f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = if (passwordMatch) Color.Cyan.copy(alpha = 0.3f) else Color.Red.copy(alpha = 0.5f),
                            unfocusedBorderColor = if (passwordMatch) Color.Cyan.copy(alpha = 0.3f) else Color.Red.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    if (!confirmPassword.isEmpty() && !passwordMatch) {
                        Text(
                            text = "Passwords do not match",
                            fontSize = 12.sp,
                            color = Color.Red,
                            modifier = Modifier.padding(start = 12.dp, top = 4.dp)
                        )
                    }
                }

                (localErrorMessage ?: vmErrorMessage)?.let { error ->
                    Text(
                        text = error,
                        fontSize = 14.sp,
                        color = Color.Red,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Register Button
                Button(
                    onClick = {
                        localErrorMessage = null
                        if (!passwordMatch) {
                            localErrorMessage = "Passwords do not match"
                            return@Button
                        }
                        if (password.length < 6) {
                            localErrorMessage = "Password must be at least 6 characters"
                            return@Button
                        }
                        userViewModel.register(name, email, password, phone)
                    },
                    enabled = isFormValid && !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color.Cyan, Color(0xFF00BCD4))
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color(0xFF0F172A),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Sign Up",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(modifier = Modifier.weight(1f), color = Color(0xFFCBD5E1).copy(alpha = 0.3f))
                    Text(
                        text = "Or",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFCBD5E1).copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    Divider(modifier = Modifier.weight(1f), color = Color(0xFFCBD5E1).copy(alpha = 0.3f))
                }

                OutlinedButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFCBD5E1).copy(alpha = 0.3f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    )
                ) {
                    // 👈 هتحتاج تحط صورة أيقونة جوجل في ملف الـ drawable باسم ic_google
                    // لو مش عندك، ممكن تستخدم أيقونة عادية مؤقتاً لحد ما تحملها.
                    // Image(
                    //     painter = painterResource(id = R.drawable.ic_google),
                    //     contentDescription = "Google Logo",
                    //     modifier = Modifier.size(24.dp)
                    // )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Sign up with Google",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Login Link
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Already have an account? ",
                        fontSize = 16.sp,
                        color = Color(0xFFCBD5E1)
                    )
                    TextButton(onClick = { onNavigate(AppRoute.Login) }) {
                        Text(
                            text = "Sign In",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Cyan
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}