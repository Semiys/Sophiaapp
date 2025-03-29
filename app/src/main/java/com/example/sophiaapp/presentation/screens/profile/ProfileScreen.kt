package com.example.sophiaapp.presentation.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Info

import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

import com.example.sophiaapp.presentation.components.profile.ProfileTextField
import com.example.sophiaapp.presentation.components.profile.ProfilePictureSelector
import com.example.sophiaapp.presentation.components.profile.InfoButton
import com.example.sophiaapp.utils.localization.AppStrings
import com.example.sophiaapp.domain.models.Profile
import com.example.sophiaapp.navigation.Screen


import androidx.navigation.NavHostController


@Composable
fun ProfileScreen(
    navController:NavHostController,
    paddingValues: PaddingValues = PaddingValues()
){
    var profile by remember {
        mutableStateOf(
            Profile(
                name = "",
                birthDate = "",
                email = "",
                password="",
                photoUri = null
            )
        )
    }
    val scrollState= rememberScrollState()

    Surface(
        modifier=Modifier.fillMaxSize()
    ){
        Column(
            modifier=Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment=Alignment.CenterHorizontally
        ){
            Text(
                text=AppStrings.Profile.COMPLETE_PROFILE,
                style=MaterialTheme.typography.headlineMedium,
                modifier=Modifier.padding(bottom=24.dp)
            )

            ProfilePictureSelector(
                imageUrl = profile.photoUri,
                onImageSelected =  { /* Здесь добавьте логику выбора фото */ }
            )

            Spacer(modifier=Modifier.height(24.dp))

            ProfileTextField(
                value = profile.name,
                onValueChange = { newName ->
                    profile = profile.copy(name = newName)
                },
                label = AppStrings.Profile.NAME_LABEL
            )

            Spacer(modifier=Modifier.height(8.dp))

            ProfileTextField(
                value = profile.birthDate,
                onValueChange = { newBirthDate ->
                    profile = profile.copy(birthDate = newBirthDate)
                },
                label = AppStrings.Auth.BIRTHDATE,
                placeholder="ДД.ММ.ГГГГ"
            )

            Spacer(modifier=Modifier.height(8.dp))

            ProfileTextField(
                value = profile.email,
                onValueChange = { newEmail ->
                    profile = profile.copy(email = newEmail)
                },
                label = AppStrings.Auth.EMAIL,
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier=Modifier.height(8.dp))

            ProfileTextField(
                value=profile.password,
                onValueChange = {newPassword ->
                    profile=profile.copy(password=newPassword)
                },
                label=AppStrings.Auth.PASSWORD,
                keyboardType = KeyboardType.Password,
                visualTransformation=PasswordVisualTransformation()
            )

            Spacer(modifier=Modifier.height(24.dp))

            InfoButton(
                onClick = { navController.navigate(Screen.AboutDevelopers.route) },
                text = AppStrings.Profile.ABOUT_DEVELOPERS,
                iconRes = Icons.Default.Person

            )
            Spacer(modifier=Modifier.height(8.dp))

            InfoButton(
                onClick={navController.navigate(Screen.AboutProject.route)},
                text= AppStrings.About.PROJECT_TITLE,
                iconRes=Icons.Default.Info
            )
            Spacer(modifier=Modifier.height(24.dp))
        }
    }


}


