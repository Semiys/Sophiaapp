package com.example.sophiaapp.presentation.components.profile

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.sophiaapp.R
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLength: Int = Int.MAX_VALUE,
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    onClick: (() -> Unit)? = null,
    focusManager: FocusManager = LocalFocusManager.current
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .then(
                    if (onClick != null) Modifier.clickable { onClick() }
                    else Modifier
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_name_age),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillBounds
            )

            OutlinedTextField(
                value = value,
                onValueChange = { newValue ->
                    // Удаляем все пробелы из ввода
                    val filteredValue = newValue.replace(" ", "")
                    if (filteredValue.length <= maxLength) {
                        onValueChange(filteredValue)
                    }
                },
                label = { Text(label) },
                readOnly = readOnly,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.clearFocus()
                    }
                ),
                singleLine = true, // Запрещаем многострочный ввод
                visualTransformation = visualTransformation,
                placeholder = placeholder?.let { { Text(text = it) } },
                isError = isError,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error.copy(alpha = 0.5f) else Color.Transparent,
                    focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorContainerColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }

        // Отображение ошибки под полем ввода
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}