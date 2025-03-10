package com.example.sophiaapp.presentation.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

import com.example.sophiaapp.utils.localization.AppStrings

@Composable
fun SignInScreen(
    onSignInClick:()->Unit,
    onSignUpClick:()-> Unit
){
    var phone by remember {mutableStateOf("")}

    Column(
        modifier=Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment=Alignment.CenterHorizontally
    ){
        Text(
            text=AppStrings.Auth.SIGNIN_TITLE,
            style=MaterialTheme.typography.headlineMedium,
            modifier=Modifier.padding(vertical=32.dp)
        )

        OutlinedTextField(
            value=phone,
            onValueChange={phone=it},
            label={Text(AppStrings.Auth.PHONE_LABEL)},
            modifier=Modifier
                .fillMaxWidth()
                .padding(vertical=8.dp)
        )

        Spacer(modifier=Modifier.weight(1f))

        Button(
            onClick=onSignInClick,
            modifier=Modifier
                .fillMaxWidth()
                .padding(vertical=8.dp)
        ){
            Text(text=AppStrings.Auth.SIGNIN_BUTTON)
        }
        TextButton(
            onClick=onSignUpClick
        ){
            Text(text=AppStrings.Auth.NO_ACCOUNT)
        }

    }

}