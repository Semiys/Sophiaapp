package com.example.sophiaapp.domain.models

data class Profile(
    val name: String = "",
    val birthDate: String = "",
    val email: String = "",
    val password: String="",
    val photoUri: String? = null
)