package com.example.sophiaapp.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.example.sophiaapp.presentation.screens.home.HomeScreen
import com.example.sophiaapp.presentation.screens.library.LibraryScreen
import com.example.sophiaapp.presentation.screens.progress.ProgressScreen
import com.example.sophiaapp.presentation.screens.profile.ProfileScreen
import com.example.sophiaapp.presentation.screens.welcome.WelcomeScreen
import com.example.sophiaapp.presentation.screens.auth.SignUpScreen
import com.example.sophiaapp.presentation.screens.auth.SignInScreen


@Composable
fun SetupNavGraph(navController:NavHostController){
    NavHost(
        navController=navController,
        startDestination=Screen.Welcome.route
    ){
        composable(route=Screen.Welcome.route){
            WelcomeScreen(
                onContinueClick={
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }
        composable(route=Screen.SignUp.route){
            SignUpScreen(
                onRegisterClick={
                    navController.navigate(Screen.Home.route){
                        popUpTo(Screen.Welcome.route){
                            inclusive=true
                        }
                    }
                },
                onSignInClick={
                    navController.navigate(Screen.SignIn.route)
                }
            )
        }

        composable(route=Screen.SignIn.route){
            SignInScreen(
                onSignInClick={
                    navController.navigate(Screen.Home.route){
                        popUpTo(Screen.Welcome.route){
                            inclusive=true
                        }
                    }
                },
                onSignUpClick={
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }


        composable(route=Screen.Home.route){
            HomeScreen()
        }
        composable(route=Screen.Library.route){
            LibraryScreen()
        }
        composable(route=Screen.Progress.route){
            ProgressScreen()
        }
        composable(route=Screen.Profile.route){
            ProfileScreen()
        }

    }
}