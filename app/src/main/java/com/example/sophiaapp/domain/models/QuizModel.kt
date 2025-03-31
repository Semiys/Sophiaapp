package com.example.sophiaapp.domain.models

data class QuizQuestion(
    val question:String,
    val options: List<String>,
    val correctAnswerIndex:Int
)

data class Quiz(
    val title: String,
    val questions: List<QuizQuestion>
)