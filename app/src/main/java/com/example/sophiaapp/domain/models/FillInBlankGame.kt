package com.example.sophiaapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Модель для вопроса с пропущенным словом
 * 
 * @param id Уникальный идентификатор вопроса
 * @param text Текст вопроса с маркером [BLANK] для пропущенного слова
 * @param correctAnswer Правильный ответ, который должен быть подставлен вместо пропуска
 */
@Parcelize
data class FillInBlankQuestion(
    val id: String,
    val text: String,
    val correctAnswer: String
) : Parcelable

/**
 * Модель для игры "Вставь пропущенное слово"
 * 
 * @param id Уникальный идентификатор игры
 * @param title Название игры
 * @param description Описание игры и инструкции
 * @param questions Список вопросов с пропущенными словами
 */
@Parcelize
data class FillInBlankGame(
    val id: String,
    val title: String,
    val description: String,
    val questions: List<FillInBlankQuestion>
) : Parcelable 