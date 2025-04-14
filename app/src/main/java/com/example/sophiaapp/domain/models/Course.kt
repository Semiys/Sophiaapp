package com.example.sophiaapp.domain.models

data class Course(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val topics: List<String> = emptyList(),
    val branch: String = ""
)