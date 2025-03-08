package com.example.sophiaapp.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues


import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Button
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.sophiaapp.domain.models.Topic
import com.example.sophiaapp.domain.models.Branch
import com.example.sophiaapp.utils.localization.AppStrings


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    showBottomSheet:Boolean,
    onDismiss:() -> Unit
){
    var topics by remember{
        mutableStateOf(
            listOf(
                Topic(AppStrings.FilterBottomSheet.EPISTEMOLOGY),
                Topic(AppStrings.FilterBottomSheet.ETHICS),
                Topic(AppStrings.FilterBottomSheet.LOGIC),
                Topic(AppStrings.FilterBottomSheet.METAPHYSICS),
                Topic(AppStrings.FilterBottomSheet.POLITICAL_PHILOSOPHY)
            )
        )
    }
    var branches by remember{
        mutableStateOf(
            listOf(
                Branch(AppStrings.FilterBottomSheet.PHILOSOPHY_LANGUAGE),
                Branch(AppStrings.FilterBottomSheet.PHILOSOPHY_SCIENCE),
                Branch(AppStrings.FilterBottomSheet.ETHICAL_THEORIES),
                Branch(AppStrings.FilterBottomSheet.PHILOSOPHY_TECHNOLOGY)
            )
        )
    }

    if (showBottomSheet){
        ModalBottomSheet(
            onDismissRequest=onDismiss,
            sheetState=rememberModalBottomSheetState()
        ){
            Column(
                modifier=Modifier
                    .fillMaxWidth()
                    .padding(horizontal=16.dp)
                    .padding(bottom=32.dp)
            ){
                Text(
                    text=AppStrings.FilterBottomSheet.FILTER,
                    style=MaterialTheme.typography.titleLarge,
                    modifier=Modifier.padding(bottom=16.dp)
                )
                Text(
                    text=AppStrings.FilterBottomSheet.POPULAR_TOPICS,
                    style=MaterialTheme.typography.titleMedium,
                    modifier=Modifier.padding(bottom=8.dp)
                )
                LazyVerticalGrid(
                    columns=GridCells.Fixed(3),
                    modifier=Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    horizontalArrangement=Arrangement.spacedBy(4.dp),
                    verticalArrangement=Arrangement.spacedBy(4.dp),
                    contentPadding=PaddingValues(end=0.dp)
                ){
                    items(topics.size){
                        index ->
                        FilterChip(
                            selected=topics[index].isSelected,
                            onClick={
                                val updatedTopics=topics.toMutableList()
                                updatedTopics[index]=topics[index].copy(isSelected=!topics[index].isSelected)
                                topics=updatedTopics},
                            label={Text(topics[index].name)}

                        )
                    }
                }
                Spacer(modifier=Modifier.height(24.dp))

                Text(
                    text=AppStrings.FilterBottomSheet.BRANCH,
                    style=MaterialTheme.typography.titleMedium,
                    modifier=Modifier.padding(bottom=8.dp)
                )

                branches.forEachIndexed{index, branch ->
                    Row(
                        modifier=Modifier
                            .fillMaxWidth()
                            .padding(vertical=4.dp),
                        verticalAlignment=Alignment.CenterVertically
                    ){
                        Checkbox(
                            checked=branch.isSelected,
                            onCheckedChange={
                                val updatedBranches=branches.toMutableList()
                                updatedBranches[index]=branch.copy(isSelected=it)
                                branches=updatedBranches
                            }
                        )
                        Text(
                            text=branch.name,
                            modifier=Modifier.padding(start=8.dp)
                        )
                    }
                }
                Button(
                    onClick=onDismiss,
                    modifier=Modifier
                        .fillMaxWidth()
                        .padding(top=24.dp)
                ){
                    Text(text=AppStrings.FilterBottomSheet.APPLY_FILTERS)
                }

            }

        }
    }
}