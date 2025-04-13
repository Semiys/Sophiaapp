package com.example.sophiaapp.presentation.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sophiaapp.data.local.database.AppDatabase
import com.example.sophiaapp.data.local.preferences.ProfileRepository
import com.example.sophiaapp.domain.models.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import com.example.sophiaapp.data.repository.FirebaseAuthRepository
import kotlinx.coroutines.runBlocking

class ProfileViewModel(
    private val repository: ProfileRepository,
    private val firebaseRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _profile = MutableStateFlow(Profile())
    val profile: StateFlow<Profile> = _profile

    private val _tempImageUri = MutableStateFlow<Uri?>(null)
    val tempImageUri: StateFlow<Uri?> = _tempImageUri

    private val _isProfileLoaded = MutableStateFlow(false)
    val isProfileLoaded: StateFlow<Boolean> = _isProfileLoaded

    // Кэширование изображения между перезапусками
    private var cachedPhotoUri: String? = null

    private var bottomNavChangeCount = 0
    private var lastSaveTime = System.currentTimeMillis()

    init {
        // Загружаем профиль моментально
        loadProfileImmediate()

        // И наблюдаем за изменениями
        startObservingProfile()
    }

    // Синхронно загружаем профиль при запуске
    private fun loadProfileImmediate() {
        try {
            runBlocking {
                val entity = repository.getProfileSync()
                if (entity != null) {
                    val profile = entity.toProfile()

                    // Проверяем наличие файла фотографии
                    val photoUri = if (profile.photoUri != null && File(profile.photoUri).exists()) {
                        cachedPhotoUri = profile.photoUri
                        profile.photoUri
                    } else if (cachedPhotoUri != null && File(cachedPhotoUri!!).exists()) {
                        cachedPhotoUri
                    } else {
                        null
                    }

                    _profile.value = profile.copy(photoUri = photoUri)
                    _isProfileLoaded.value = true

                    Log.d("ProfileViewModel", "Immediate load: профиль загружен с фото: $photoUri")
                } else {
                    _profile.value = Profile()
                    _isProfileLoaded.value = true
                    Log.d("ProfileViewModel", "Immediate load: создан пустой профиль")
                }
            }
        } catch (e: Exception) {
            Log.e("ProfileViewModel", "Ошибка при синхронной загрузке профиля", e)
            _isProfileLoaded.value = true // Всё равно считаем, что загрузка завершена
        }
    }

    private fun startObservingProfile() {
        viewModelScope.launch {
            try {
                repository.getProfile().collect { dbProfile ->
                    if (dbProfile != null) {
                        val photoUri = when {
                            dbProfile.photoUri != null && File(dbProfile.photoUri).exists() -> {
                                cachedPhotoUri = dbProfile.photoUri
                                dbProfile.photoUri
                            }
                            cachedPhotoUri != null && File(cachedPhotoUri!!).exists() -> {
                                cachedPhotoUri
                            }
                            _profile.value.photoUri != null && File(_profile.value.photoUri!!).exists() -> {
                                _profile.value.photoUri
                            }
                            else -> null
                        }

                        _profile.value = dbProfile.copy(photoUri = photoUri)
                        _isProfileLoaded.value = true

                        Log.d("ProfileViewModel", "Flow collect: обновлен профиль с фото: $photoUri")
                    }
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Ошибка при наблюдении за профилем", e)
            }
        }
    }

    fun updateName(name: String) {
        _profile.value = _profile.value.copy(name = name)
    }

    fun updateBirthDate(birthDate: String) {
        _profile.value = _profile.value.copy(birthDate = birthDate)
    }

    fun updateEmail(email: String) {
        _profile.value = _profile.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _profile.value = _profile.value.copy(password = password)
    }

    fun createImageUriForCamera(): Uri {
        try {
            val (_, uri) = repository.createImageFile()
            _tempImageUri.value = uri
            return uri
        } catch (e: Exception) {
            Log.e("ProfileViewModel", "Ошибка при создании URI для камеры", e)
            throw e
        }
    }

    fun saveProfileImage(uri: Uri) {
        viewModelScope.launch {
            try {
                val localPath = repository.saveProfileImage(uri)

                // Сразу обновляем кэш
                cachedPhotoUri = localPath

                // И обновляем профиль
                _profile.value = _profile.value.copy(photoUri = localPath)

                // Сохраняем всё в базу данных
                saveCurrentProfile()

                Log.d("ProfileViewModel", "Фото успешно сохранено: $localPath")
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Ошибка при сохранении фото профиля", e)
            }
        }
    }
    fun updatePasswordInFirebase(newPassword: String, currentPassword: String? = null) {
        viewModelScope.launch {
            try {
                // Если currentPassword не предоставлен, показываем диалог для ввода текущего пароля
                // (это можно реализовать через Flow или LiveData и диалог в UI)

                // Для простоты, проверяем доступность текущего пароля
                if (currentPassword != null) {
                    // Сначала выполняем повторную аутентификацию
                    val reauthResult = firebaseRepository.reauthenticateUser(_profile.value.email, currentPassword)

                    reauthResult.onSuccess {
                        // После успешной переаутентификации, обновляем пароль
                        val result = firebaseRepository.updateUserPassword(newPassword)

                        result.onSuccess {
                            Log.d("ProfileViewModel", "Пароль успешно обновлен")
                            _profile.value = _profile.value.copy(password = "")
                            saveCurrentProfile()
                        }.onFailure { error ->
                            Log.e("ProfileViewModel", "Ошибка при обновлении пароля", error)
                        }
                    }.onFailure { error ->
                        Log.e("ProfileViewModel", "Ошибка при повторной аутентификации", error)
                    }
                } else {
                    // Если текущий пароль не предоставлен, показываем сообщение о необходимости
                    // повторной авторизации (это можно отобразить в UI)
                    Log.e("ProfileViewModel", "Для обновления пароля требуется текущий пароль")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Исключение при обновлении пароля", e)
            }
        }
    }

    // Сохраняем профиль при каждом переходе между вкладками
    fun saveCurrentProfile() {
        // Защита от слишком частых сохранений (не чаще раза в 300 мс)
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastSaveTime < 300) {
            Log.d("ProfileViewModel", "Сохранение пропущено: слишком частые вызовы")
            return
        }

        lastSaveTime = currentTime

        viewModelScope.launch {
            try {
                // Создаем копию с кэшированным фото, если текущее отсутствует
                val profileToSave = if (_profile.value.photoUri == null && cachedPhotoUri != null && File(cachedPhotoUri!!).exists()) {
                    _profile.value.copy(photoUri = cachedPhotoUri)
                } else {
                    _profile.value
                }

                repository.saveProfile(profileToSave)

                // Синхронизация с Firebase при необходимости
                if (firebaseRepository.isUserLoggedIn()) {
                    firebaseRepository.saveUserProfile(profileToSave)
                }

                Log.d("ProfileViewModel", "Профиль успешно сохранен с фото: ${profileToSave.photoUri}")
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Ошибка при сохранении профиля", e)
            }
        }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                val database = AppDatabase.getDatabase(context)
                val localRepository = ProfileRepository(context, database.profileDao())
                val firebaseRepository = FirebaseAuthRepository()
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(localRepository, firebaseRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}