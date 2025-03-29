package com.example.sophiaapp.presentation.screens.legal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sophiaapp.presentation.common.components.LongTextCard
import com.example.sophiaapp.utils.localization.AppStrings
import com.example.sophiaapp.R
import com.example.sophiaapp.presentation.common.components.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    navController:NavHostController
){
    Scaffold(
        topBar={
            TopAppBar(
                title={Text(AppStrings.Project.PRIVACY_POLICY)},
                navigationIcon={
                    BackButton(onClick={navController.popBackStack()})
                }
            )
        }
    ){innerPadding->
        Column(
            modifier=Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal=16.dp)
                .verticalScroll(rememberScrollState())
        ){
            LongTextCard(
                text=AppStrings.Legal.PRIVACY_POLICY_TEXT,
                backgroundRes=R.drawable.card_background_not,
                verticalPadding=28
            )
        }
    }

}