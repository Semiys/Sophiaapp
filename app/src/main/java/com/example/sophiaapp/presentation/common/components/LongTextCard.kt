package com.example.sophiaapp.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sophiaapp.R

@Composable
fun LongTextCard(
    text:String,
    backgroundRes: Int=R.drawable.card_background_not,
    modifier:Modifier=Modifier,
    textAlign:TextAlign=TextAlign.Start,
    horizontalPadding:Int=24,
    verticalPadding:Int=16
){
    Box(
        modifier=modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight=48.dp)
    ){
        Image(
            painter=painterResource(id=backgroundRes),
            contentDescription=null,
            modifier=Modifier.matchParentSize(),
            contentScale=ContentScale.FillBounds
        )
        Column(
            modifier=Modifier
                .fillMaxWidth()
                .padding(horizontal=horizontalPadding.dp,vertical=verticalPadding.dp)
        ){
            Text(
                text=text,
                style=MaterialTheme.typography.bodyMedium,
                textAlign=textAlign
            )
        }
    }
}