package com.example.sophiaapp.navigation

sealed class Screen(val route: String){
    object Home:Screen(route="home_screen")
    object Library: Screen(route="library_screen")
    object Progress: Screen(route="progress_screen")
    object Profile: Screen(route="profile_screen")

}