package com.example.sophiaapp.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer


import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.sophiaapp.utils.localization.AppStrings
import com.example.sophiaapp.R
import com.example.sophiaapp.presentation.common.components.CustomButton


/*
import androidx.compose.ui.tooling.preview.Preview
*/


@Composable
fun HomeScreen(paddingValues: PaddingValues = PaddingValues()){
    LazyColumn(
        modifier=Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal=16.dp)





    ){
        item {
            Text(
                text = AppStrings.HomeScreen.WELCOME_PHILOSOPHER,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp,top=16.dp)

            )
            Text(
                text = AppStrings.HomeScreen.EXPLORE_PHILOSOPHY,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Image(
                    painter= painterResource(id=R.drawable.card_background),
                    contentDescription=null,
                    modifier=Modifier.matchParentSize(),
                    contentScale=ContentScale.FillBounds
                )
                Column(
                    modifier = Modifier.padding(16.dp)

                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Text(
                            text = AppStrings.HomeScreen.DIVE_INTO_THE_DEPTHS,
                            style = MaterialTheme.typography.titleMedium
                        )
                        //Место для картинки
                        Image(
                            painter=painterResource(id=R.drawable.book_new),
                            contentDescription=null,
                            modifier=Modifier.size(80.dp)
                        )


                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Checkbox(
                            checked = false,
                            onCheckedChange = {}
                        )
                        Text(text=AppStrings.HomeScreen.UNLOCK_NEW)
                    }


                    //Прогресс бар
                    LinearProgressIndicator(
                        progress = {
                            0.7f
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

            }
        }
        item {
            Text(
                text = AppStrings.HomeScreen.PONDER_THE_MYSTERIES,
                style = MaterialTheme.typography.titleMedium

            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)

            ) {
                Image(
                    painter=painterResource(id=R.drawable.card_background),
                    contentDescription=null,
                    modifier=Modifier.matchParentSize(),
                    contentScale=ContentScale.FillBounds
                )
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier=Modifier.weight(1f)
                        ) {
                            Text(
                                text = AppStrings.HomeScreen.PHILOSOPHICAL_TOOLS,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = AppStrings.HomeScreen.JOIN_GLOBAL,
                                style = MaterialTheme.typography.bodySmall
                            )

                        }
                        Image(
                            painter=painterResource(id=R.drawable.book_new),
                            contentDescription=null,
                            modifier=Modifier.size(80.dp)
                        )
                    }
                    //Кнопка Begin now
                    CustomButton(
                        text=AppStrings.HomeScreen.BEGIN_NOW,
                        onClick={/* действие */},
                        modifier=Modifier.padding(top=16.dp),
                        backgroundRes = R.drawable.continue_button,
                        textColor = MaterialTheme.colorScheme.primary
                    )

                }

            }
        }
        item{
            Spacer(modifier=Modifier.height(60.dp))
        }


    }

}


/*
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}
*/
