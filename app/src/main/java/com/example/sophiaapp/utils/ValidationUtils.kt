package com.example.sophiaapp.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


object ValidationUtils {
    // Улучшенная проверка email с более строгим регулярным выражением
    fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

        // Дополнительная проверка на распространенные домены
        val validDomains = listOf(
            "gmail.com", "yahoo.com", "outlook.com", "hotmail.com",
            "mail.ru", "yandex.ru", "icloud.com", "protonmail.com",
            "aol.com", "zoho.com", "gmx.com"
        )

        val isValidFormat = email.isNotEmpty() && emailPattern.matcher(email).matches()

        // Если домен не входит в список известных доменов, делаем дополнительную проверку
        if (isValidFormat && !email.contains('@')) {
            return false
        }

        return isValidFormat
    }

    // Проверка минимальной длины пароля и наличия цифры и буквы
    fun isValidPassword(password: String): Boolean {
        if (password.length < 6) return false
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        return hasLetter && hasDigit
    }

    // Проверка формата даты рождения (DD.MM.YYYY)
    fun isValidBirthDate(birthDate: String): Boolean {
        return try {
            if (birthDate.isEmpty()) return true // Опционально

            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            dateFormat.isLenient = false
            val parsedDate = dateFormat.parse(birthDate) ?: return false

            // Проверка разумного диапазона дат
            val calendar = Calendar.getInstance()
            calendar.time = parsedDate

            val year = calendar.get(Calendar.YEAR)
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)

            year in 1900..currentYear
        } catch (e: Exception) {
            false
        }
    }

    // Проверка имени на минимальную длину
    fun isValidName(name: String): Boolean {
        return name.trim().length >= 2
    }

    // Получаем понятные сообщения об ошибках для каждого поля
    fun getEmailError(email: String): String? {
        return when {
            email.isEmpty() -> "Email не может быть пустым"
            !isValidEmail(email) -> "Введите корректный email адрес"
            else -> null
        }
    }

    fun getPasswordError(password: String): String? {
        return when {
            password.isEmpty() -> "Пароль не может быть пустым"
            password.length < 6 -> "Пароль должен содержать минимум 6 символов"
            !password.any { it.isLetter() } -> "Пароль должен содержать хотя бы одну букву"
            !password.any { it.isDigit() } -> "Пароль должен содержать хотя бы одну цифру"
            else -> null
        }
    }

    fun getNameError(name: String): String? {
        return when {
            name.trim().isEmpty() -> "Имя не может быть пустым"
            name.trim().length < 2 -> "Имя должно содержать минимум 2 символа"
            else -> null
        }
    }

    fun getBirthDateError(birthDate: String): String? {
        if (birthDate.isEmpty()) return null // Опционально

        return when {
            !birthDate.matches(Regex("\\d{2}\\.\\d{2}\\.\\d{4}")) ->
                "Формат даты должен быть ДД.ММ.ГГГГ"
            !isValidBirthDate(birthDate) ->
                "Введите корректную дату"
            else -> null
        }
    }
}