package com.example.sophiaapp.presentation.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme

import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.sophiaapp.presentation.common.components.CustomButton
import com.example.sophiaapp.presentation.components.profile.ProfileTextField
import com.example.sophiaapp.utils.localization.AppStrings
import com.example.sophiaapp.R
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.sophiaapp.presentation.viewmodels.AuthViewModel


@Composable
fun SignUpScreen(
    onRegisterClick: () -> Unit,
    onSignInClick: () -> Unit,
    viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory(LocalContext.current))
) {
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    // Если пользователь успешно зарегистрировался, переходим на главный экран
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onRegisterClick()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = AppStrings.Auth.SIGNUP_TITLE,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 32.dp)
        )

        ProfileTextField(
            value = name,
            onValueChange = { name = it },
            label = AppStrings.Auth.NAME_LABEL,
            maxLength = 50
        )

        ProfileTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = AppStrings.Auth.BIRTHDATE,
            keyboardType = KeyboardType.Text,
            maxLength = 10,
            placeholder = "ДД.ММ.ГГГГ"
        )

        ProfileTextField(
            value = email,
            onValueChange = { email = it },
            label = AppStrings.Auth.EMAIL,
            keyboardType = KeyboardType.Email,
            maxLength = 50
        )

        ProfileTextField(
            value = password,
            onValueChange = { password = it },
            label = AppStrings.Auth.PASSWORD,
            keyboardType = KeyboardType.Password,
            maxLength = 30,
            visualTransformation = PasswordVisualTransformation()
        )

        ProfileTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = AppStrings.Auth.CONFIRMPASSWORD,
            keyboardType = KeyboardType.Password,
            maxLength = 30,
            visualTransformation = PasswordVisualTransformation()
        )

        // Отображение ошибки
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Кнопка регистрации
        CustomButton(
            text = AppStrings.Auth.SIGNUP_BUTTON,
            onClick = {
                if (password == confirmPassword) {
                    viewModel.register(email, password, name, birthDate)
                } else {
                    // Показать ошибку несовпадения паролей
                }
            },
            modifier = Modifier.padding(vertical = 8.dp),
            backgroundRes = R.drawable.auth_sign,
            textColor = MaterialTheme.colorScheme.primary,
            enabled = !isLoading && password == confirmPassword
        )

        // Индикатор загрузки
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(8.dp)
            )
        }

        CustomButton(
            text = AppStrings.Auth.ALREADY_HAVE_ACCOUNT,
            onClick = onSignInClick,
            modifier = Modifier.padding(top = 8.dp),
            backgroundRes = R.drawable.continue_button,
            textColor = MaterialTheme.colorScheme.primary
        )
    }
}