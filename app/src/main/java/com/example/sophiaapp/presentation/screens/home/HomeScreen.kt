package com.example.sophiaapp.presentation.screens.home

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
import com.example.sophiaapp.utils.localization.AppStrings


/*
import androidx.compose.ui.tooling.preview.Preview
*/


@Composable
fun HomeScreen(){
    LazyColumn(
        modifier=Modifier
            .fillMaxSize()
            .padding(16.dp)





    ){
        item {
            Text(
                text = AppStrings.HomeScreen.WELCOME_PHILOSOPHER,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)

            )
            Text(
                text = AppStrings.HomeScreen.EXPLORE_PHILOSOPHY,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                elevation=CardDefaults.cardElevation(
                    defaultElevation=4.dp
                ),
                shape=RoundedCornerShape(16.dp)
            ) {
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
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(Color.LightGray)
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                elevation=CardDefaults.cardElevation(
                    defaultElevation=4.dp
                ),
                shape=RoundedCornerShape(16.dp)

            ) {
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
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(Color.LightGray)
                                .padding(start=16.dp)
                        )
                    }
                    //Кнопка Begin now
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .wrapContentWidth(),
                        shape=RoundedCornerShape(12.dp),
                        elevation=ButtonDefaults.buttonElevation(
                            defaultElevation=4.dp
                        )
                    ) {
                        Text(
                            text=AppStrings.HomeScreen.BEGIN_NOW,
                            style=MaterialTheme.typography.bodyMedium,
                            modifier=Modifier.padding(horizontal=4.dp,vertical=4.dp)
                        )
                    }
                }

            }
        }
        item{
            Text(
                text=AppStrings.HomeScreen.DISCOVER_THOUGHT,
                style =MaterialTheme.typography.titleMedium,
                modifier=Modifier.padding(vertical=16.dp)

            )
        }
        item{
            LazyVerticalGrid(
                columns=GridCells.Fixed(2),
                horizontalArrangement=Arrangement.spacedBy(16.dp),
                verticalArrangement=Arrangement.spacedBy(16.dp),
                modifier=Modifier
                    .fillMaxWidth()
                    .height(500.dp),
                userScrollEnabled = false


            ){
                items(4){index ->
                    Card(
                        modifier=Modifier.fillMaxWidth(),
                        elevation=CardDefaults.cardElevation(
                            defaultElevation=4.dp
                        ),
                        shape=RoundedCornerShape(16.dp)
                    ){
                        Column(
                            modifier=Modifier.padding(16.dp),
                            verticalArrangement=Arrangement.spacedBy(8.dp)

                        ){
                            Box(
                                modifier=Modifier
                                    .fillMaxWidth()
                                    .size(120.dp)
                                    .background(Color.LightGray)
                            )
                            Text(
                                text=when(index){
                                    0 -> AppStrings.HomeScreen.EXPLORE_EXISTENTIAL
                                    1 -> AppStrings.HomeScreen.PHILOSOPHY_ONLIFE
                                    2 -> AppStrings.HomeScreen.STUDY_PHILOCOPHICAL
                                    3 -> AppStrings.HomeScreen.EXPLORE
                                    else -> ""
                                },
                                style=MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

            }
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
