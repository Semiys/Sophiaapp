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
import com.example.sophiaapp.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.max

@Composable
fun BottomBar(navController:NavHostController){
    val screens=listOf(
        Screen.Home,
        Screen.Library,
        Screen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute=navBackStackEntry?.destination?.route

    Box(
        modifier=Modifier
            .fillMaxWidth()
            .background(Color.White)
            .height(80.dp)

    ){
        Image(
            painter=painterResource(id=R.drawable.navigationbar),
            contentDescription=null,
            modifier=Modifier.fillMaxSize(),
            contentScale=ContentScale.FillBounds
        )
        Row(
            modifier=Modifier
                .fillMaxWidth()
                .padding(top=8.dp,bottom=8.dp),
            horizontalArrangement=Arrangement.SpaceEvenly
        ){
            screens.forEach {screen ->
                val selected = currentRoute==screen.route
                Column(
                    horizontalAlignment= Alignment.CenterHorizontally,
                    modifier=Modifier
                        .weight(1f)
                        .clickable(onClick={
                            navController.navigate(screen.route){
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop=true
                            }
                        })
                        .padding(0.dp)
                ){
                    Box(
                        modifier=Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if(selected)Color(0xFFE6F0FF)else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ){
                        Image(
                            painter=painterResource(
                                id=when(screen){
                                    Screen.Home -> R.drawable.home_icon
                                    Screen.Library  -> R.drawable.library_icon
                                    Screen.Profile -> R.drawable.people_icon
                                    else -> R.drawable.home_icon
                                }),
                            contentDescription = screen.title,
                            modifier=Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text=screen.title,
                        color=if(selected) Color(0xFF3E80FF) else Color.Gray,
                        textAlign=TextAlign.Center,
                        fontSize=10.sp,
                        maxLines=1,
                        modifier=Modifier
                            .padding(top=2.dp)
                            .width(80.dp)
                    )

                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFE0E0E0))
                .align(Alignment.TopCenter)
        )


    }
}
