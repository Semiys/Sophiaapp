package com.example.sophiaapp.presentation.components.profile

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier


@Composable
fun ProfileTextField(
    value:String,
    onValueChange:(String) -> Unit,
    label:String,
    modifier:Modifier=Modifier
){
    OutlinedTextField(
        value=value,
        onValueChange=onValueChange,
        label={Text(label)},
        modifier=modifier
            .fillMaxWidth()
            .padding(vertical=8.dp)
    )

}