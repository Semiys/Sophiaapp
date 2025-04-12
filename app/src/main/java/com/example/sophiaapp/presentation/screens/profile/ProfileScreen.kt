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
                    onValueChange = { viewModel.updateName(it) },
                    label = AppStrings.Profile.NAME_LABEL
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProfileTextField(
                    value = profile.birthDate,
                    onValueChange = { viewModel.updateBirthDate(it) },
                    label = AppStrings.Auth.BIRTHDATE,
                    placeholder = "ДД.ММ.ГГГГ"
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProfileTextField(
                    value = profile.email,
                    onValueChange = { viewModel.updateEmail(it) },
                    label = AppStrings.Auth.EMAIL,
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProfileTextField(
                    value = profile.password,
                    onValueChange = { viewModel.updatePassword(it) },
                    label = AppStrings.Auth.PASSWORD,
                    keyboardType = KeyboardType.Password,
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.saveCurrentProfile()
                        Toast.makeText(context, "Профиль успешно сохранен", Toast.LENGTH_SHORT).show()
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