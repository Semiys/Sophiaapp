package com.example.sophiaapp.domain.models

data class Topic(
    val name: String,
    var isSelected: Boolean =false
)

data class Branch(
    val name: String,
    var isSelected: Boolean=false
)