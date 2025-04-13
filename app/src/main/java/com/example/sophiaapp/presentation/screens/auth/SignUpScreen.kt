package com.example.sophiaapp.presentation.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.sophiaapp.R
import com.example.sophiaapp.presentation.common.components.CustomButton
import com.example.sophiaapp.presentation.components.profile.ProfileTextField
import com.example.sophiaapp.presentation.viewmodels.AuthViewModel
import com.example.sophiaapp.utils.ValidationUtils
import com.example.sophiaapp.utils.localization.AppStrings
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
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

    var showDatePicker by remember { mutableStateOf(false) }

    // Состояния ошибок валидации
    var nameError by remember { mutableStateOf<String?>(null) }
    var birthDateError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    // Проверка полей
    fun validateName() {
        nameError = ValidationUtils.getNameError(name)
    }

    fun validateBirthDate() {
        birthDateError = ValidationUtils.getBirthDateError(birthDate)
    }

    fun validateEmail() {
        emailError = ValidationUtils.getEmailError(email)
    }

    fun validatePassword() {
        passwordError = ValidationUtils.getPasswordError(password)
    }

    fun validateConfirmPassword() {
        confirmPasswordError = when {
            confirmPassword.isEmpty() -> "Подтверждение пароля не может быть пустым"
            password != confirmPassword -> "Пароли не совпадают"
            else -> null
        }
    }

    // Проверка всей формы
    fun validateForm(): Boolean {
        validateName()
        validateBirthDate()
        validateEmail()
        validatePassword()
        validateConfirmPassword()

        return nameError == null &&
                birthDateError == null &&
                emailError == null &&
                passwordError == null &&
                confirmPasswordError == null
    }

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val scrollState = rememberScrollState()

    // Если пользователь успешно зарегистрировался, переходим на главный экран
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onRegisterClick()
        }
    }

    // DatePicker для выбора даты рождения
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        // Устанавливаем текущую дату, если поле пустое
        LaunchedEffect(showDatePicker) {
            if (birthDate.isEmpty()) {
                // Установка текущей даты как выбранной для лучшего UX
                datePickerState.selectedDateMillis = System.currentTimeMillis()
            }
        }

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Date(millis)
                        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        birthDate = formatter.format(date)
                        validateBirthDate()
                    }
                    showDatePicker = false
                }) {
                    Text("Подтвердить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = AppStrings.Auth.SIGNUP_TITLE,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 32.dp)
        )

        ProfileTextField(
            value = name,
            onValueChange = {
                name = it
                validateName()
            },
            label = AppStrings.Auth.NAME_LABEL,
            maxLength = 50,
            isError = nameError != null,
            errorMessage = nameError,
            modifier = Modifier.onFocusChanged {
                if (!it.isFocused) validateName()
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileTextField(
                value = birthDate,
                onValueChange = {
                    birthDate = it
                    validateBirthDate()
                },
                label = AppStrings.Auth.BIRTHDATE,
                keyboardType = KeyboardType.Text,
                maxLength = 10,
                placeholder = "ДД.ММ.ГГГГ",
                isError = birthDateError != null,
                errorMessage = birthDateError,
                readOnly = true,
                modifier = Modifier
                    .weight(0.8f)
            )

            IconButton(
                onClick = {
                    // Явно показываем DatePicker
                    showDatePicker = true
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.calendarik),
                    contentDescription = "Выбрать дату"
                )
            }
        }

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
                if (confirmPassword.isNotEmpty()) {
                    validateConfirmPassword()
                }
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

        ProfileTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                validateConfirmPassword()
            },
            label = AppStrings.Auth.CONFIRMPASSWORD,
            keyboardType = KeyboardType.Password,
            maxLength = 30,
            visualTransformation = PasswordVisualTransformation(),
            isError = confirmPasswordError != null,
            errorMessage = confirmPasswordError,
            modifier = Modifier.onFocusChanged {
                if (!it.isFocused) validateConfirmPassword()
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

        // Кнопка регистрации
        CustomButton(
            text = AppStrings.Auth.SIGNUP_BUTTON,
            onClick = {
                if (validateForm()) {
                    viewModel.register(email, password, name, birthDate)
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
            text = AppStrings.Auth.ALREADY_HAVE_ACCOUNT,
            onClick = onSignInClick,
            modifier = Modifier.padding(top = 8.dp),
            backgroundRes = R.drawable.continue_button,
            textColor = MaterialTheme.colorScheme.primary
        )
    }
}