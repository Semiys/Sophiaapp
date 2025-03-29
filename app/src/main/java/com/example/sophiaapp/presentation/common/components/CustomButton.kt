package com.example.sophiaapp.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sophiaapp.R

@Composable
fun CustomButton(
    text:String,
    onClick:()-> Unit,
    modifier:Modifier=Modifier,
    backgroundRes:Int=R.drawable.continue_button,
    textColor:Color=Color.White
){
    Box(
        modifier=modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight=48.dp)
            .clickable(onClick=onClick)
    ){
        Image(
            painter=painterResource(id=backgroundRes),
            contentDescription=null,
            modifier=Modifier.matchParentSize(),
            contentScale=ContentScale.FillBounds
        )
        Text(
            text=text,
            style=MaterialTheme.typography.bodyLarge,
            color=textColor,
            textAlign=TextAlign.Center,
            modifier=Modifier
                .align(Alignment.Center)
                .padding(vertical=12.dp)
        )
    }

}