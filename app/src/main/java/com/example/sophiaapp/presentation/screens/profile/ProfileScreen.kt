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



@Composable
fun ProfileScreen(){
    var name by remember {mutableStateOf("")}
    var age by remember{mutableStateOf("")}
    var phonenumber by remember{mutableStateOf("")}
    var profileImageUrl by remember{mutableStateOf<String?>(null)}

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
                text="Complete your profile",
                style=MaterialTheme.typography.headlineMedium,
                modifier=Modifier.padding(bottom=24.dp)
            )

            ProfilePictureSelector(
                imageUrl=profileImageUrl,
                onImageSelected={/* логика выбора фото*/}
            )

            Spacer(modifier=Modifier.height(24.dp))

            ProfileTextField(
                value=name,
                onValueChange={name=it},
                label = "Full Name"
            )

            Spacer(modifier=Modifier.height(8.dp))

            ProfileTextField(
                value=age,
                onValueChange={age=it},
                label="Age"
            )

            Spacer(modifier=Modifier.height(8.dp))

            ProfileTextField(
                value=phonenumber,
                onValueChange={phonenumber=it},
                label="Phone Number"
            )

            Spacer(modifier=Modifier.height(24.dp))

            InfoButton(
                onClick={/* Здесь будет навигация на экран информации */}
            )


        }

    }


}


