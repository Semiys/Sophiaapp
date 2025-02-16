package com.example.sophiaapp.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.example.sophiaapp.presentation.screens.home.HomeScreen
import com.example.sophiaapp.presentation.screens.library.LibraryScreen
import com.example.sophiaapp.presentation.screens.progress.ProgressScreen
import com.example.sophiaapp.presentation.screens.profile.ProfileScreen


@Composable
fun SetupNavGraph(navController:NavHostController){
    NavHost(
        navController=navController,
        startDestination=Screen.Home.route
    ){
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