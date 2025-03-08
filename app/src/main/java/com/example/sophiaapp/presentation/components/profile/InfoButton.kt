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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.dp
import com.example.sophiaapp.utils.localization.AppStrings


@Composable
fun InfoButton(
    onClick:() -> Unit,
    modifier: Modifier=Modifier
){
    OutlinedButton(
        onClick=onClick,
        modifier=modifier.fillMaxWidth()
    ){
        Row(
            verticalAlignment=Alignment.CenterVertically
        ){
            Icon(
                imageVector=Icons.Default.Info,
                contentDescription=AppStrings.Profile.INFORMATION
            )
            Spacer(modifier=Modifier.width(8.dp))
            Text(text=AppStrings.Profile.ABOUT_DEVELOPERS)

        }
    }

}