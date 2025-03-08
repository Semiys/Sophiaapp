package com.example.sophiaapp.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sophiaapp.navigation.Screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Check
@Composable
fun BottomBar(navController:NavHostController){
    val screens=listOf(
        Screen.Home,
        Screen.Library,
        Screen.Progress,
        Screen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute=navBackStackEntry?.destination?.route

    NavigationBar{

        screens.forEach{screen->
            NavigationBarItem(

                label = { Text(text = screen.title) },

                selected=currentRoute==screen.route,

                onClick={
                    navController.navigate(screen.route){
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop=true
                    }
                },
                icon={
                    val icon = when(screen){
                        Screen.Home ->Icons.Default.Home
                        Screen.Library -> Icons.Default.List
                        Screen.Progress -> Icons.Default.Check
                        Screen.Profile -> Icons.Default.Person
                    }
                    Icon(
                        imageVector=icon,
                        contentDescription=screen.route
                    )
                }
            )
        }
    }
}
