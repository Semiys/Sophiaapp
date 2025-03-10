package com.example.sophiaapp.presentation.screens.welcome

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.example.sophiaapp.utils.localization.AppStrings
import com.example.sophiaapp.R

@Composable
fun WelcomeScreen(
    onContinueClick:()->Unit
){
    Column(
        modifier=Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment=Alignment.CenterHorizontally,
        verticalArrangement=Arrangement.SpaceBetween
    ){
        Column(
            horizontalAlignment=Alignment.CenterHorizontally,
            modifier=Modifier.padding(top=48.dp)
        ){
            Text(
                text=AppStrings.Welcome.SCREEN_TITLE,
                style=MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier=Modifier.height(16.dp))
            Text(
                text=AppStrings.Welcome.SUBTITLE,
                style=MaterialTheme.typography.bodyLarge
            )

        }
        Image(
            painter=painterResource(id=R.drawable.app_logo),
            contentDescription=null,
            modifier=Modifier
                .size(300.dp)
                .padding(vertical=32.dp)

        )

        Button(
            onClick=onContinueClick,
            modifier=Modifier
                .fillMaxWidth()
                .padding(bottom=32.dp)
        ){
            Text(text=AppStrings.Welcome.CONTINUE_BUTTON)
        }


    }

}