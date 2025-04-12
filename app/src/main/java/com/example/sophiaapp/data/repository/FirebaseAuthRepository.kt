package com.example.sophiaapp.data.repository

import android.util.Log
import com.example.sophiaapp.domain.models.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Репозиторий для работы с Firebase Authentication и Firestore для данных профиля
 */
class FirebaseAuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser: Flow<FirebaseUser?> = _currentUser

    init {
        // Обновление текущего пользователя при изменении состояния авторизации
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
        }
    }

    /**
     * Регистрация нового пользователя
     */
    suspend fun registerUser(email: String, password: String): Result<FirebaseUser> {
        return try {
            val authResult = withContext(Dispatchers.IO) {
                auth.createUserWithEmailAndPassword(email, password).await()
            }
            val user = authResult.user ?: throw Exception("Ошибка регистрации")
            Result.success(user)
        } catch (e: Exception) {
            Log.e("FirebaseAuthRepository", "Error registering user", e)
            Result.failure(e)
        }
    }

    /**
     * Вход пользователя в систему
     */
    suspend fun loginUser(email: String, password: String): Result<FirebaseUser> {
        return try {
            val authResult = withContext(Dispatchers.IO) {
                auth.signInWithEmailAndPassword(email, password).await()
            }
            val user = authResult.user ?: throw Exception("Ошибка входа")
            Result.success(user)
        } catch (e: Exception) {
            Log.e("FirebaseAuthRepository", "Error logging in user", e)
            Result.failure(e)
        }
    }

    /**
     * Выход пользователя из системы
     */
    fun logoutUser() {
        auth.signOut()
    }

    /**
     * Сохранение профиля пользователя в Firestore
     */
    suspend fun saveUserProfile(profile: Profile): Result<String> {
        val user = auth.currentUser ?: return Result.failure(Exception("Пользователь не авторизован"))

        val profileData = hashMapOf(
            "name" to profile.name,
            "birthDate" to profile.birthDate,
            "email" to profile.email,
            // Не сохраняем пароль в Firestore в открытом виде
            // Не сохраняем локальный путь к фото, так как он может быть доступен только на устройстве
            "updatedAt" to System.currentTimeMillis()
        )

        return try {
            withContext(Dispatchers.IO) {
                usersCollection.document(user.uid).set(profileData).await()
            }
            Result.success(user.uid)
        } catch (e: Exception) {
            Log.e("FirebaseAuthRepository", "Error saving user profile", e)
            Result.failure(e)
        }
    }

    /**
     * Получение профиля пользователя из Firestore
     */
    suspend fun getUserProfile(): Result<Map<String, Any>> {
        val user = auth.currentUser ?: return Result.failure(Exception("Пользователь не авторизован"))

        return try {
            val document = withContext(Dispatchers.IO) {
                usersCollection.document(user.uid).get().await()
            }

            if (document.exists()) {
                Result.success(document.data ?: emptyMap())
            } else {
                Result.failure(Exception("Профиль пользователя не найден"))
            }
        } catch (e: Exception) {
            Log.e("FirebaseAuthRepository", "Error getting user profile", e)
            Result.failure(e)
        }
    }

    /**
     * Проверка, залогинен ли пользователь
     */
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    /**
     * Получение ID текущего пользователя
     */
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}