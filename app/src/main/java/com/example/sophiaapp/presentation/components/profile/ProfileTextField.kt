package com.example.sophiaapp.presentation.components.profile

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.sophiaapp.R
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.VisualTransformation.Companion.None

@Composable
fun ProfileTextField(
    value:String,
    onValueChange:(String) -> Unit,
    label:String,
    modifier:Modifier=Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLength: Int = Int.MAX_VALUE,
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation=None,
    placeholder: String?=null
){
    Box(
        modifier=Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical =  8.dp)

    ) {
        Image(
            painter=painterResource(id=R.drawable.profile_name_age),
            contentDescription = null,
            modifier=Modifier
                .matchParentSize(),
            contentScale=ContentScale.FillBounds
        )

        OutlinedTextField(
            value=value,
            onValueChange={newValue ->
                if (newValue.length <=maxLength){
                    onValueChange(newValue)

                }
            },
            label={Text(label)},
            readOnly=readOnly,
            keyboardOptions=KeyboardOptions(keyboardType=keyboardType),
            visualTransformation=visualTransformation,
            placeholder=placeholder?.let{{Text(text=it)}},
            colors=OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent

            ),
            modifier=Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }

}