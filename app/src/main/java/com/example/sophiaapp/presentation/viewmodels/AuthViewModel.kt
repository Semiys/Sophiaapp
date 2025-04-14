package com.example.sophiaapp.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sophiaapp.data.local.database.AppDatabase
import com.example.sophiaapp.data.local.preferences.ProfileRepository
import com.example.sophiaapp.data.repository.FirebaseAuthRepository
import com.example.sophiaapp.domain.models.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val firebaseRepository: FirebaseAuthRepository,
    private val localRepository: ProfileRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoggedIn = MutableStateFlow(firebaseRepository.isUserLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        // Если пользователь уже залогинен, загружаем данные из Firebase в локальную БД
        if (isLoggedIn.value) {
            syncProfileFromFirebase()
        }
    }

    fun register(email: String, password: String, name: String, birthDate: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val registerResult = firebaseRepository.registerUser(email, password)
                registerResult.onSuccess { user ->
                    // Создаем профиль в локальной БД
                    val profile = Profile(
                        name = name,
                        email = email,
                        birthDate = birthDate,
                        password = "" // Не храним пароль в локальной БД
                    )
                    localRepository.saveProfile(profile)

                    // Сохраняем профиль в Firebase
                    firebaseRepository.saveUserProfile(profile)

                    _isLoggedIn.value = true
                }.onFailure { error ->
                    _errorMessage.value = "Ошибка регистрации: ${error.message}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка регистрации: ${e.message}"
                Log.e("AuthViewModel", "Error during registration", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        _errorMessage.value = null

        // Упрощенная проверка только на пустые поля
        if (email.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Заполните все поля"
            _isLoading.value = false
            return
        }

        viewModelScope.launch {
            try {
                val loginResult = firebaseRepository.loginUser(email, password)
                loginResult.onSuccess { user ->
                    // Успешно вошли, синхронизируем данные с Firebase
                    syncProfileFromFirebase()
                    _isLoggedIn.value = true
                }.onFailure { error ->
                    // Просто сохраняем исходную ошибку, перевод делаем в UI
                    _errorMessage.value = error.message
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("AuthViewModel", "Error during login", e)
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun logout() {
        firebaseRepository.logoutUser()
        _isLoggedIn.value = false
    }

    private fun syncProfileFromFirebase() {
        viewModelScope.launch {
            try {
                val profileResult = firebaseRepository.getUserProfile()
                profileResult.onSuccess { data ->
                    // Получаем текущий локальный профиль (для сохранения пути к фото)
                    val currentLocalProfile = localRepository.getProfileSync()
                    val updatedProfile = Profile(
                        name = data["name"] as? String ?: "",
                        birthDate = data["birthDate"] as? String ?: "",
                        email = data["email"] as? String ?: "",
                        password = "", // Не сохраняем пароль
                        photoUri = currentLocalProfile?.photoPath // Используем photoPath вместо photoUri
                    )
                    localRepository.saveProfile(updatedProfile)
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error syncing profile from Firebase", e)
            }
        }
    }

    // Синхронизация профиля с Firebase (при обновлении данных локально)
    fun syncProfileToFirebase() {
        viewModelScope.launch {
            try {
                val currentProfile = localRepository.getProfileSync()
                if (currentProfile != null) {
                    firebaseRepository.saveUserProfile(currentProfile.toProfile())
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error syncing profile to Firebase", e)
            }
        }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                val database = AppDatabase.getInstance(context)
                val localRepository = ProfileRepository(context, database.profileDao())
                val firebaseRepository = FirebaseAuthRepository()
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(firebaseRepository, localRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}