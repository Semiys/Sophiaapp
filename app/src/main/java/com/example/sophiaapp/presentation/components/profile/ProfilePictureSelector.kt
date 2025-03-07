package com.example.sophiaapp.presentation.components.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme

import coil.compose.AsyncImage






@Composable
fun ProfilePictureSelector(
    imageUrl:String?,
    onImageSelected:()->Unit,
    modifier:Modifier=Modifier
){
    Surface(
        shape=CircleShape,
        shadowElevation=4.dp,
        modifier=modifier.size(100.dp)
    ){
        Box(
            modifier=Modifier
                .fillMaxSize()
                .clickable(onClick=onImageSelected)
        ){
            if(imageUrl==null)
            {
                Icon(
                    imageVector=Icons.Default.Add,
                    contentDescription="Add photo",
                    modifier=Modifier.align(Alignment.Center)
                )
            }
            else{
                AsyncImage(
                    model=imageUrl,
                    contentDescription="Profile picture",
                    contentScale=ContentScale.Crop,
                    modifier=Modifier.fillMaxSize()
                )
            }
        }

    }
}