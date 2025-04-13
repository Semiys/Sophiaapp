package com.example.sophiaapp.presentation.screens.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.DisposableEffect

import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

import com.example.sophiaapp.presentation.components.profile.ProfileTextField
import com.example.sophiaapp.presentation.components.profile.ProfilePictureSelector
import com.example.sophiaapp.presentation.components.profile.InfoButton
import com.example.sophiaapp.utils.localization.AppStrings
import com.example.sophiaapp.navigation.Screen
import com.example.sophiaapp.presentation.viewmodels.ProfileViewModel

import androidx.navigation.NavHostController
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.example.sophiaapp.presentation.components.PermissionHandler
import com.example.sophiaapp.presentation.viewmodels.AuthViewModel
import com.example.sophiaapp.utils.ValidationUtils

// Импорты для DatePicker
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.TextButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Импорты для диалога подтверждения пароля
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.text.KeyboardOptions

// Импорты для строки с иконкой календаря
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import com.example.sophiaapp.R
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    paddingValues: PaddingValues = PaddingValues()
) {
    val context = LocalContext.current
    val viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModel.Factory(context)
    )

    val profile by viewModel.profile.collectAsState()
    val isProfileLoaded by viewModel.isProfileLoaded.collectAsState()
    val scrollState = rememberScrollState()

    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory(LocalContext.current))
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    var nameError by remember { mutableStateOf<String?>(null) }
    var birthDateError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }
    var currentPasswordError by remember { mutableStateOf<String?>(null) }




    // Диалог для ввода текущего пароля
    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = {
                showPasswordDialog = false
                currentPassword = ""
                currentPasswordError = null
            },
            title = { Text("Введите текущий пароль") },
            text = {
                Column {
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = {
                            currentPassword = it
                            currentPasswordError = null
                        },
                        label = { Text("Текущий пароль") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = currentPasswordError != null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    if (currentPasswordError != null) {
                        Text(
                            text = currentPasswordError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (currentPassword.isEmpty()) {
                            currentPasswordError = "Введите текущий пароль"
                        } else {
                            // Сохраняем новый пароль с переаутентификацией
                            viewModel.updatePasswordInFirebase(profile.password, currentPassword)
                            showPasswordDialog = false
                            currentPassword = ""
                            currentPasswordError = null
                        }
                    }
                ) {
                    Text("Подтвердить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPasswordDialog = false
                        currentPassword = ""
                        currentPasswordError = null
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }

    // Функции для валидации полей
    fun validateName() {
        nameError = ValidationUtils.getNameError(profile.name)
    }

    fun validateBirthDate() {
        birthDateError = ValidationUtils.getBirthDateError(profile.birthDate)
    }

    fun validateEmail() {
        emailError = ValidationUtils.getEmailError(profile.email)
    }

    fun validatePassword() {
        // Валидируем пароль только если он не пустой (для изменения пароля)
        passwordError = if (profile.password.isNotEmpty()) {
            ValidationUtils.getPasswordError(profile.password)
        } else {
            null // Если пароль пустой, не показываем ошибку (пользователь мог не менять пароль)
        }
    }

    // Функция для проверки всех полей перед сохранением
    fun validateForm(): Boolean {
        validateName()
        validateBirthDate()
        validateEmail()
        validatePassword()

        return nameError == null &&
                birthDateError == null &&
                emailError == null &&
                (passwordError == null || profile.password.isEmpty())
    }
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        // Устанавливаем текущую дату, если поле пустое
        LaunchedEffect(showDatePicker) {
            if (profile.birthDate.isEmpty()) {
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
                        viewModel.updateBirthDate(formatter.format(date))
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

    // Загружаем/сохраняем профиль при каждом появлении экрана
    LaunchedEffect(key1 = Unit) {
        viewModel.saveCurrentProfile()
    }

    // Наблюдаем за жизненным циклом для сохранения профиля
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> viewModel.saveCurrentProfile()
                Lifecycle.Event.ON_RESUME -> viewModel.saveCurrentProfile()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.saveCurrentProfile()
        }
    }

    // Отслеживаем изменения навигации, чтобы сохранять при переключении
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, _, _ ->
            viewModel.saveCurrentProfile()
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    var permissionsReady by remember { mutableStateOf(false) }
    PermissionHandler {
        permissionsReady = true
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        if (!isProfileLoaded) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = AppStrings.Profile.COMPLETE_PROFILE,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                ProfilePictureSelector(
                    imageUrl = profile.photoUri,
                    onImageSelected = { uri ->
                        viewModel.saveProfileImage(uri)
                        Toast.makeText(context, "Фото выбрано и сохранено", Toast.LENGTH_SHORT).show()
                    },
                    onCameraSelected = {
                        viewModel.createImageUriForCamera()
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                ProfileTextField(
                    value = profile.name,
                    onValueChange = {
                        viewModel.updateName(it)
                        nameError = null // Сбрасываем ошибку при вводе
                    },
                    label = AppStrings.Profile.NAME_LABEL,
                    isError = nameError != null,
                    errorMessage = nameError,
                    modifier = Modifier.onFocusChanged {
                        if (!it.isFocused) validateName()
                    }
                )

                // Аналогично модифицируйте остальные поля
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileTextField(
                        value = profile.birthDate,
                        onValueChange = {
                            viewModel.updateBirthDate(it)
                            birthDateError = null
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
                            .onFocusChanged {
                                if (!it.isFocused) validateBirthDate()
                            }
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
                    value = profile.email,
                    onValueChange = {
                        viewModel.updateEmail(it)
                        emailError = null
                    },
                    label = AppStrings.Auth.EMAIL,
                    keyboardType = KeyboardType.Email,
                    isError = emailError != null,
                    errorMessage = emailError,
                    modifier = Modifier.onFocusChanged {
                        if (!it.isFocused) validateEmail()
                    }
                )

                ProfileTextField(
                    value = profile.password,
                    onValueChange = {
                        viewModel.updatePassword(it)
                        passwordError = null
                    },
                    label = AppStrings.Auth.PASSWORD,
                    keyboardType = KeyboardType.Password,
                    visualTransformation = PasswordVisualTransformation(),
                    isError = passwordError != null,
                    errorMessage = passwordError,
                    modifier = Modifier.onFocusChanged {
                        if (!it.isFocused && profile.password.isNotEmpty()) validatePassword()
                    }
                )

                // Модифицируйте кнопку сохранения для валидации перед сохранением
                Button(
                    onClick = {
                        if (validateForm()) {
                            viewModel.saveCurrentProfile()

                            // Если пароль был изменен, показываем диалог для ввода текущего пароля
                            if (profile.password.isNotEmpty()) {
                                showPasswordDialog = true
                            } else {
                                Toast.makeText(context, "Профиль успешно сохранен", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Пожалуйста, исправьте ошибки в форме", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(AppStrings.Profile.SAVE_BUTTON)
                }

                Spacer(modifier = Modifier.height(16.dp))

                InfoButton(
                    onClick = { navController.navigate(Screen.AboutDevelopers.route) },
                    text = AppStrings.Profile.ABOUT_DEVELOPERS,
                    iconRes = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(8.dp))

                InfoButton(
                    onClick = { navController.navigate(Screen.AboutProject.route) },
                    text = AppStrings.About.PROJECT_TITLE,
                    iconRes = Icons.Default.Info
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}