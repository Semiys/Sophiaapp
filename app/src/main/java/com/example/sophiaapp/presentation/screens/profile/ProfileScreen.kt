package com.example.sophiaapp.presentation.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height

import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

import com.example.sophiaapp.presentation.components.profile.ProfileTextField
import com.example.sophiaapp.presentation.components.profile.ProfilePictureSelector
import com.example.sophiaapp.presentation.components.profile.InfoButton
import com.example.sophiaapp.utils.localization.AppStrings
import com.example.sophiaapp.domain.models.Profile


@Composable
fun ProfileScreen(){
    var profile by remember {
        mutableStateOf(
            Profile(
                name = "",
                age = "",
                phoneNumber = "",
                photoUri = null
            )
        )
    }

    Surface(
        modifier=Modifier.fillMaxSize()
    ){
        Column(
            modifier=Modifier
                .fillMaxSize()
                .padding(16.dp),
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
                value = profile.age,
                onValueChange = { newAge ->
                    profile = profile.copy(age = newAge)
                },
                label = AppStrings.Profile.AGE
            )

            Spacer(modifier=Modifier.height(8.dp))

            ProfileTextField(
                value = profile.phoneNumber,
                onValueChange = { newPhone ->
                    profile = profile.copy(phoneNumber = newPhone)
                },
                label = AppStrings.Profile.PHONE_NUMBER
            )

            Spacer(modifier=Modifier.height(24.dp))

            InfoButton(
                onClick = {/* Здесь будет навигация на экран информации */}
            )
        }
    }


}


