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


@Composable
fun SignUpScreen(
    onRegisterClick:()->Unit,
    onSignInClick:()->Unit

){
    var name by remember {mutableStateOf("")}
    var birthDate by remember{mutableStateOf("")}
    var email by remember{mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    var confirmPassword by remember{mutableStateOf("")}


    Column(
        modifier=Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment=Alignment.CenterHorizontally

    ){
        Text(
            text=AppStrings.Auth.SIGNUP_TITLE,
            style=MaterialTheme.typography.headlineMedium,
            modifier=Modifier.padding(vertical=32.dp)
        )

        ProfileTextField(
            value=name,
            onValueChange={name=it},
            label=AppStrings.Auth.NAME_LABEL,
            maxLength = 50
        )

        ProfileTextField(
            value=birthDate,
            onValueChange={birthDate=it},
            label=AppStrings.Auth.BIRTHDATE,
            keyboardType=KeyboardType.Text,
            maxLength = 10,
            placeholder="ДД.ММ.ГГГГ"
        )

        ProfileTextField(
            value=email,
            onValueChange={email=it},
            label=AppStrings.Auth.EMAIL,
            keyboardType = KeyboardType.Email,
            maxLength = 50
        )
        ProfileTextField(
            value=password,
            onValueChange = {password=it},
            label=AppStrings.Auth.PASSWORD,
            keyboardType = KeyboardType.Password,
            maxLength = 30,
            visualTransformation=PasswordVisualTransformation()
        )
        ProfileTextField(
            value=confirmPassword,
            onValueChange = {confirmPassword=it},
            label=AppStrings.Auth.CONFIRMPASSWORD,
            keyboardType=KeyboardType.Password,
            maxLength = 30,
            visualTransformation=PasswordVisualTransformation()
        )

        Spacer(modifier=Modifier.weight(1f))

        CustomButton(
            text=AppStrings.Auth.SIGNUP_BUTTON,
            onClick=onRegisterClick,
            modifier=Modifier.padding(vertical=8.dp),
            backgroundRes = R.drawable.auth_sign,
            textColor = MaterialTheme.colorScheme.primary

        )
        CustomButton(
            text=AppStrings.Auth.ALREADY_HAVE_ACCOUNT,
            onClick=onSignInClick,
            modifier=Modifier.padding(top=8.dp),
            backgroundRes = R.drawable.continue_button,
            textColor=MaterialTheme.colorScheme.primary
        )






    }


}