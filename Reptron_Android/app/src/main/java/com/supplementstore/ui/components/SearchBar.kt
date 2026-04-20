package com.supplementstore.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    searchTerm: String,
    onSearchTermChange: (String) -> Unit,
    placeholder: String = "Search...",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchTerm,
        onValueChange = onSearchTermChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFFCBD5E1).copy(alpha = 0.7f)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Cyan
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.5f),
            unfocusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.3f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color.Cyan.copy(alpha = 0.5f),
            unfocusedBorderColor = Color.Cyan.copy(alpha = 0.3f),
            cursorColor = Color.Cyan
        ),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { /* Handle search action */ }
        )
    )
}
