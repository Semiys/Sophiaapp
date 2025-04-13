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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.sophiaapp.presentation.common.components.CustomButton
import com.example.sophiaapp.presentation.components.profile.ProfileTextField
import com.example.sophiaapp.R

import com.example.sophiaapp.utils.localization.AppStrings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.sophiaapp.presentation.viewmodels.AuthViewModel
import com.example.sophiaapp.utils.ValidationUtils

@Composable
fun SignInScreen(
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory(LocalContext.current))
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Состояния ошибок валидации
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Проверка полей
    fun validateEmail() {
        emailError = ValidationUtils.getEmailError(email)
    }

    fun validatePassword() {
        // При входе проверяем только на пустое поле
        passwordError = when {
            password.isEmpty() -> "Пароль не может быть пустым"
            else -> null
        }
    }

    // Проверка всей формы
    fun validateForm(): Boolean {
        validateEmail()
        validatePassword()

        return emailError == null && passwordError == null
    }

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    // Если пользователь успешно авторизовался, переходим на главный экран
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onSignInClick()
        }
    }

    LaunchedEffect(errorMessage) {
        // Создаем локальную переменную с явным приведением типа
        val error = errorMessage ?: return@LaunchedEffect

        // Теперь используем эту локальную переменную для проверок
        when {
            error.contains("password is invalid", ignoreCase = true) ||
                    error.contains("wrong password", ignoreCase = true) ||
                    error.contains("The password is invalid", ignoreCase = true) -> {
                viewModel.setErrorMessage("Неверный пароль")
            }
            error.contains("user not found", ignoreCase = true) ||
                    error.contains("email not found", ignoreCase = true) ||
                    error.contains("invalid email", ignoreCase = true) ||
                    error.contains("There is no user record", ignoreCase = true) -> {
                viewModel.setErrorMessage("Пользователь с такой почтой не найден")
            }
            error.contains("network", ignoreCase = true) ||
                    error.contains("connect", ignoreCase = true) -> {
                viewModel.setErrorMessage("Ошибка сети. Проверьте подключение к интернету")
            }
            error.contains("INVALID_LOGIN_CREDENTIALS", ignoreCase = true) -> {
                viewModel.setErrorMessage("Неверный email или пароль")
            }
            else -> {
                // Если ошибка не опознана, показываем общее сообщение
                viewModel.setErrorMessage("Ошибка авторизации. Проверьте введенные данные")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = AppStrings.Auth.SIGNIN_TITLE,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 32.dp)
        )

        ProfileTextField(
            value = email,
            onValueChange = {
                email = it
                validateEmail()
            },
            label = AppStrings.Auth.EMAIL,
            keyboardType = KeyboardType.Email,
            maxLength = 50,
            isError = emailError != null,
            errorMessage = emailError,
            modifier = Modifier.onFocusChanged {
                if (!it.isFocused) validateEmail()
            }
        )

        ProfileTextField(
            value = password,
            onValueChange = {
                password = it
                validatePassword()
            },
            label = AppStrings.Auth.PASSWORD,
            keyboardType = KeyboardType.Password,
            maxLength = 30,
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            errorMessage = passwordError,
            modifier = Modifier.onFocusChanged {
                if (!it.isFocused) validatePassword()
            }
        )

        // Отображение общей ошибки
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CustomButton(
            text = AppStrings.Auth.SIGNIN_BUTTON,
            onClick = {
                if (validateForm()) {
                    viewModel.login(email, password)
                }
            },
            modifier = Modifier.padding(vertical = 8.dp),
            backgroundRes = R.drawable.auth_sign,
            textColor = MaterialTheme.colorScheme.primary,
            enabled = !isLoading
        )

        // Индикатор загрузки
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(8.dp)
            )
        }

        CustomButton(
            text = AppStrings.Auth.NO_ACCOUNT,
            onClick = onSignUpClick,
            modifier = Modifier.padding(top = 8.dp),
            backgroundRes = R.drawable.continue_button,
            textColor = MaterialTheme.colorScheme.primary
        )
    }
}