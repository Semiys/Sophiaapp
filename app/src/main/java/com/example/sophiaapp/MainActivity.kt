package com.example.sophiaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sophiaapp.navigation.Screen
import com.example.sophiaapp.navigation.SetupNavGraph
import com.example.sophiaapp.presentation.components.BottomBar
import com.example.sophiaapp.presentation.viewmodels.AuthViewModel
import com.example.sophiaapp.ui.theme.SophiaappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SophiaappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory(LocalContext.current))
                    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

                    // Проверяем аутентификацию при запуске
                    LaunchedEffect(isLoggedIn) {
                        if (isLoggedIn) {
                            // Пользователь авторизован - перейти на главный экран
                            navController.navigate(Screen.Home.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        } else if (navController.currentBackStackEntry?.destination?.route !in listOf(
                                Screen.Welcome.route,
                                Screen.SignIn.route,
                                Screen.SignUp.route
                            )) {
                            // Не авторизован и не на экране входа - перейти на экран приветствия
                            navController.navigate(Screen.Welcome.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    val isCourseRelated = currentRoute?.startsWith(Screen.CourseDetail.route.substringBefore("{")) == true ||
                            currentRoute?.startsWith(Screen.CourseTheory.route.substringBefore("{")) == true ||
                            currentRoute?.startsWith(Screen.CoursePractice.route.substringBefore("{")) == true


                    val showBottomBar = when {
                        currentRoute == Screen.Welcome.route ||
                                currentRoute == Screen.SignIn.route ||
                                currentRoute == Screen.SignUp.route ||
                                currentRoute == Screen.AboutDevelopers.route ||
                                currentRoute == Screen.AboutProject.route ||
                                currentRoute == Screen.PrivacyPolicy.route ||
                                currentRoute == Screen.TermsOfUse.route ||
                                isCourseRelated -> false
                        else -> true

                    }
                    Scaffold(
                        bottomBar = {
                            if (showBottomBar) {
                                BottomBar(navController = navController)
                            }

                        }
                    ) { paddingValues ->
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            SetupNavGraph(
                                navController = navController,
                                paddingValues = paddingValues
                            )
                        }
                    }
                }
            }
        }
    }
}