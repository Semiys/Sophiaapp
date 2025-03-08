package com.example.sophiaapp.navigation

import com.example.sophiaapp.utils.localization.AppStrings

sealed class Screen(val route: String){
    object Home:Screen(route="home_screen")
    object Library: Screen(route="library_screen")
    object Progress: Screen(route="progress_screen")
    object Profile: Screen(route="profile_screen")

    val title:String
        get()=when(this){
            is Home ->AppStrings.NavigationBar.HOME
            is Library -> AppStrings.NavigationBar.LIBRARY
            is Progress -> AppStrings.NavigationBar.PROGRESS
            is Profile -> AppStrings.NavigationBar.PROFILE
        }

}