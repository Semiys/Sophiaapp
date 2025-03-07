package com.example.sophiaapp.presentation.components.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.material3.DropdownMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhilosopherDropDown(
    selected:String,
    onSelectedChange:(String)->Unit,
    modifier:Modifier=Modifier
){
    var expanded by remember{mutableStateOf(false)}
    val philosophers=listOf(
        "Plato",
        "Aristotle",
        "Kant",
        "Nietzsche",
        "Sartre"
    )
    ExposedDropdownMenuBox(
        expanded=expanded,
        onExpandedChange={expanded=it},
        modifier=modifier.fillMaxWidth()
    ){
        OutlinedTextField(
            value=selected,
            onValueChange={},
            readOnly=true,
            label={Text("Select your favorite philosopher")},
            modifier=Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon={ExposedDropdownMenuDefaults.TrailingIcon(expanded=expanded)}

        )
        DropdownMenu(
            expanded=expanded,
            onDismissRequest={expanded=false},
            modifier=Modifier.exposedDropdownSize()
        )
        {
            philosophers.forEach{philosopher->
                DropdownMenuItem(
                    text={Text(philosopher)},
                    onClick={
                        onSelectedChange(philosopher)
                        expanded=false
                    }
                )
            }
        }


    }




}