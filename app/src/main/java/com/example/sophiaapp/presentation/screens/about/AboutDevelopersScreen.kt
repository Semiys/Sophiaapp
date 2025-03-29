package com.example.sophiaapp.presentation.screens.about

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.example.sophiaapp.R
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import android.content.Intent
import android.net.Uri
import com.example.sophiaapp.presentation.common.components.BackButton
import com.example.sophiaapp.utils.localization.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutDevelopersScreen(
    navController:NavHostController,
    paddingValues: PaddingValues = PaddingValues()
){
    val context=LocalContext.current
    Scaffold(
        topBar={
            TopAppBar(
                title={Text(AppStrings.About.SCREEN_TITLE)},
                navigationIcon={
                    BackButton(onClick={navController.popBackStack()})
                }
            )
        }
    ){
        paddingValues->
        Column(
            modifier=Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment=Alignment.CenterHorizontally
        ) {
            Box(
                modifier=Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight=48.dp)

            ){
                Image(
                    painter=painterResource(id=R.drawable.card_background_not),
                    contentDescription=null,
                    modifier=Modifier.matchParentSize(),
                    contentScale=ContentScale.FillBounds

                )
                Column(
                    modifier=Modifier.padding(16.dp),
                    horizontalAlignment=Alignment.CenterHorizontally

                ) {
                    Text(
                        text = AppStrings.About.APP_DESCRIPTION,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 16.dp)

                    )
                }
            }

            DeveloperCard(
                name=AppStrings.About.DEVELOPER_NAME_ONE,
                group=AppStrings.About.DEVELOPER_GROUP,
                faculty=AppStrings.About.DEVELOPER_FACULTY,
                university=AppStrings.About.DEVELOPER_UNIVERSITY,
                role=AppStrings.About.DEVELOPER_ROLE_ONE,
                githubUrl = AppStrings.About.GITHUB_URL_ONE,
                telegramUrl=AppStrings.About.TELEGRAM_URL_ONE,
                email=AppStrings.About.DEVELOPER_EMAIL_ONE,

            )
            Spacer(modifier=Modifier.height(16.dp))
            DeveloperCard(
                name=AppStrings.About.DEVELOPER_NAME_TWO,
                group=AppStrings.About.DEVELOPER_GROUP_TWO,
                faculty=AppStrings.About.DEVELOPER_FACULTY_TWO,
                university=AppStrings.About.DEVELOPER_UNIVERSITY_TWO,
                role=AppStrings.About.DEVELOPER_ROLE_TWO,
                githubUrl = AppStrings.About.GITHUB_URL_TWO,
                telegramUrl=AppStrings.About.TELEGRAM_URL_TWO,
                email=AppStrings.About.DEVELOPER_EMAIL_TWO,

                )


        }

    }
}

@Composable
fun DeveloperCard(
    name: String,
    group: String,
    faculty: String,
    university:String,
    role: String,
    githubUrl:String="",
    telegramUrl:String="",
    email:String
){
    val context=LocalContext.current
    Box(
        modifier=Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight=48.dp)
    ){
        Image(
            painter=painterResource(id=R.drawable.card_background_not),
            contentDescription=null,
            modifier=Modifier.matchParentSize(),
            contentScale=ContentScale.FillBounds
        )
        Column(
            modifier=Modifier.padding(16.dp),
            horizontalAlignment=Alignment.CenterHorizontally

        ){
            Surface(
                modifier=Modifier
                    .size(100.dp),
                shape=CircleShape,
                color=MaterialTheme.colorScheme.primaryContainer
            ){
                Icon(
                    imageVector=Icons.Default.Person,
                    contentDescription="Аватар Разработчика",
                    modifier=Modifier
                        .padding(16.dp)
                        .size(68.dp),
                    tint=MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier=Modifier.height(8.dp))
            Text(
                text=name,
                style=MaterialTheme.typography.titleMedium,
                textAlign=TextAlign.Center

            )
            Spacer(modifier=Modifier.height(4.dp))

            Text(
                text=group,
                style=MaterialTheme.typography.bodyMedium
            )
            Text(
                text=faculty,
                style=MaterialTheme.typography.bodyMedium,
                textAlign=TextAlign.Center
            )
            Text(
                text=university,
                style=MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier=Modifier.height(4.dp))

            Text(
                text=role,
                style=MaterialTheme.typography.bodySmall,
                color=MaterialTheme.colorScheme.primary
            )
            Spacer(modifier=Modifier.height(4.dp))
            Text(
                text=email,
                style=MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier=Modifier.height(8.dp))

            Row(
                modifier=Modifier.fillMaxWidth(),
                horizontalArrangement=Arrangement.SpaceEvenly
            ){
                if (githubUrl.isNotEmpty()){
                    FloatingActionButton(
                        onClick={
                            val intent=Intent(Intent.ACTION_VIEW,Uri.parse(githubUrl))
                            context.startActivity(intent)
                        },
                        modifier=Modifier.size(48.dp),
                        containerColor=MaterialTheme.colorScheme.secondaryContainer
                    ){
                        Icon(
                            painter=painterResource(id=R.drawable.github),
                            contentDescription="Git Hub Профиль",
                            modifier=Modifier.size(36.dp)


                        )
                    }
                }
                if (telegramUrl.isNotEmpty()){
                    FloatingActionButton(
                        onClick={
                            val intent=Intent(Intent.ACTION_VIEW, Uri.parse(telegramUrl))
                            context.startActivity(intent)
                        },
                        modifier=Modifier.size(48.dp),
                        containerColor=MaterialTheme.colorScheme.secondaryContainer
                    ){
                        Icon(
                            painter=painterResource(id=R.drawable.telegramlogo),
                            contentDescription="Telegram",
                            modifier=Modifier.size(48.dp)
                        )

                    }
                }


            }

        }
    }

}