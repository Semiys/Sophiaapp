package com.example.sophiaapp.presentation.components.profile


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.sophiaapp.utils.localization.AppStrings
import com.example.sophiaapp.R
import java.io.File


@Composable
fun ProfilePictureSelector(
    imageUrl: String?,
    onImageSelected: (Uri) -> Unit,
    onCameraSelected:() -> Uri,
    modifier: Modifier = Modifier
) {
    val context= LocalContext.current
    val interactionSource=remember{ MutableInteractionSource()}
    var showDialog by remember{ mutableStateOf(false) }

    val galleryLauncher= rememberLauncherForActivityResult(
        contract=ActivityResultContracts.GetContent()
    ){
        uri: Uri? ->
        uri?.let{onImageSelected(it)}
    }

    val cameraLauncher=rememberLauncherForActivityResult(
        contract=ActivityResultContracts.TakePicture()
    ){success ->
        if (success){
            // Фото успешно сделано, оно уже сохранено по uri
        }
    }

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource=interactionSource,
                indication = rememberRipple(bounded = true,color=MaterialTheme.colorScheme.primary),
                onClick = {showDialog=true}
            )
    ) {

        if (imageUrl != null && File(imageUrl).exists()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = AppStrings.Profile.PHOTO_PROFILE,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center)
            )
        }
        Image(
            painter=painterResource(
                id=if(imageUrl==null || !File(imageUrl).exists())
                    R.drawable.photo_not
            else R.drawable.photo),
            contentDescription = null,
            modifier=Modifier.matchParentSize(),
            contentScale=ContentScale.FillBounds
        )

    }
    if (showDialog){
        Dialog(onDismissRequest={showDialog=false}){
            Surface(
                shape=MaterialTheme.shapes.medium,
                color=MaterialTheme.colorScheme.surface,
                modifier=Modifier.padding(16.dp)
            ){
                Column(
                    modifier=Modifier.padding(16.dp),
                    horizontalAlignment=Alignment.CenterHorizontally
                ){
                    Text(
                        text=AppStrings.Profile.CHANGE_PHOTO,
                        style=MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier=Modifier.height(16.dp))

                    Button(
                        onClick={
                            galleryLauncher.launch("image/*")
                            showDialog=false
                        },
                        modifier=Modifier.fillMaxWidth()
                    ){
                        Text("Выбрать из галереи")
                    }
                    Spacer(modifier=Modifier.height(8.dp))

                    Button(
                        onClick={
                            val uri=onCameraSelected()
                            cameraLauncher.launch(uri)
                            showDialog=false
                        },
                        modifier=Modifier.fillMaxWidth()
                    ){
                        Text("Сделать фото")
                    }
                    Spacer(modifier=Modifier.height(8.dp))

                    TextButton(
                        onClick={showDialog=false},
                        modifier=Modifier.fillMaxWidth()
                    ){
                        Text("Отмена")
                    }
                }
            }

        }
    }


}