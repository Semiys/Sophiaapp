package com.example.sophiaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.sophiaapp.navigation.SetupNavGraph
import com.example.sophiaapp.ui.theme.SophiaappTheme
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import com.example.sophiaapp.presentation.components.BottomBar

class MainActivity:ComponentActivity(){
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        //Включаем поддержку Splash Screen
        installSplashScreen()

        setContent{
            SophiaappTheme{
                Surface(
                    modifier=Modifier.fillMaxSize(),
                    color=MaterialTheme.colorScheme.background
                ){
                    val navController=rememberNavController()
                    Scaffold(
                        bottomBar={
                            BottomBar(navController=navController)
                        }
                    ){
                        paddingValues ->
                        Box(
                            modifier=Modifier.padding(paddingValues)
                        ){
                            SetupNavGraph(navController=navController)
                        }
                    }

                }
            }
        }
    }
}