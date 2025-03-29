package com.example.sophiaapp.presentation.components.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.sophiaapp.utils.localization.AppStrings
import com.example.sophiaapp.R
import androidx.compose.ui.graphics.vector.ImageVector



@Composable
fun InfoButton(
    onClick:() -> Unit,
    text:String,
    iconRes:ImageVector,
    modifier: Modifier=Modifier

){
    Box(
        modifier=Modifier
            .fillMaxWidth()
            .padding(horizontal=16.dp,vertical=8.dp)
            .clickable(onClick=onClick)
    ){
        Image(
            painter=painterResource(id=R.drawable.continue_button),
            contentDescription=null,
            modifier=Modifier.matchParentSize(),
            contentScale=ContentScale.FillBounds
        )
        Row(
            modifier=Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment=Alignment.CenterVertically
        ){
            Icon(
                imageVector=iconRes,
                contentDescription=null,
                modifier=Modifier.size(24.dp)
            )
            Spacer(modifier=Modifier.width(12.dp))

            Text(
                text=text,
                style=MaterialTheme.typography.bodyLarge
            )

        }
    }

}