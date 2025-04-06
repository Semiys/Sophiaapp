package com.example.sophiaapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Модель для вопроса с выбором вариантов ответа
 * 
 * @param id Уникальный идентификатор вопроса
 * @param text Текст вопроса с маркером [BLANK] для пропущенного слова
 * @param options Варианты ответов (A, Б, В и т.д.)
 * @param correctAnswerIndex Индекс правильного ответа в списке options
 */
@Parcelize
data class MultipleChoiceQuestion(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int
) : Parcelable

/**
 * Модель для игры "Вставлялка" - игра с выбором вариантов ответа
 * 
 * @param id Уникальный идентификатор игры
 * @param title Название игры
 * @param description Описание игры и инструкции
 * @param questions Список вопросов с вариантами ответов
 */
@Parcelize
data class MultipleChoiceGame(
    val id: String,
    val title: String,
    val description: String,
    val questions: List<MultipleChoiceQuestion>
) : Parcelable 