package com.example.sophiaapp.presentation.components

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat


@Composable
fun PermissionHandler(
    onPermissionsGranted:()-> Unit
){
    val context= LocalContext.current
    var permissionsGranted by remember {mutableStateOf(false)}

    val permissions=if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.CAMERA
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
    }
    val permissionLauncher=rememberLauncherForActivityResult(
        contract=ActivityResultContracts.RequestMultiplePermissions()
    ){permissionsMap ->
        permissionsGranted=permissionsMap.values.all{it}
        if (permissionsGranted){
            onPermissionsGranted()
        }
    }
    LaunchedEffect(key1=Unit){
        permissionsGranted=permissions.all{
            ContextCompat.checkSelfPermission(context,it)==PackageManager.PERMISSION_GRANTED
        }
        if (permissionsGranted){
            onPermissionsGranted()
        }else{
            permissionLauncher.launch(permissions)
        }
    }
}