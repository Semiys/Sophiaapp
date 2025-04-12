package com.example.sophiaapp.presentation.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.sophiaapp.navigation.Screen

@Composable
fun AuthScreen(navController: NavHostController) {
    // Просто перенаправляем на экран SignIn
    navController.navigate(Screen.SignIn.route) {
        popUpTo(0) { inclusive = true }
    }
}