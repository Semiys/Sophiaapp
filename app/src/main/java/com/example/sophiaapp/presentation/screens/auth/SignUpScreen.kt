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
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton

import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.sophiaapp.utils.localization.AppStrings


@Composable
fun SignUpScreen(
    onRegisterClick:()->Unit,
    onSignInClick:()->Unit

){
    var name by remember {mutableStateOf("")}
    var age by remember{mutableStateOf("")}
    var phone by remember{mutableStateOf("")}

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

        OutlinedTextField(
            value=name,
            onValueChange={name=it},
            label={Text(AppStrings.Auth.NAME_LABEL)},
            modifier=Modifier
                .fillMaxWidth()
                .padding(vertical=8.dp)
        )

        OutlinedTextField(
            value=age,
            onValueChange={age=it},
            label={Text(AppStrings.Auth.AGE_LABEL)},
            modifier=Modifier
                .fillMaxWidth()
                .padding(vertical=8.dp)
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
            onClick=onRegisterClick,
            modifier=Modifier
                .fillMaxWidth()
                .padding(vertical=8.dp)
        ){
            Text(text=AppStrings.Auth.SIGNUP_BUTTON)
        }
        TextButton(
            onClick=onSignInClick
        ){
            Text(text=AppStrings.Auth.ALREADY_HAVE_ACCOUNT)
        }






    }


}