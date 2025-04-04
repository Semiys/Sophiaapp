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

class ProfileViewModel(private val repository: ProfileRepository):ViewModel(){

    private val _profile=MutableStateFlow(Profile())
    val profile:StateFlow<Profile> = _profile

    private val _tempImageUri= MutableStateFlow<Uri?>(null)
    val tempImageUri: StateFlow<Uri?> = _tempImageUri

    private val _isProfileLoaded = MutableStateFlow(false)
    val isProfileLoaded: StateFlow<Boolean> = _isProfileLoaded

    init{
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                repository.getProfile().collect { dbProfile ->
                    if (dbProfile != null) {
                        _profile.value = dbProfile
                        _isProfileLoaded.value = true
                    } else {
                        _profile.value = Profile()
                        saveCurrentProfile()
                        _isProfileLoaded.value = true
                    }
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error loading profile", e)
            }
        }
    }

    fun updateProfile(profile: Profile) {
        viewModelScope.launch {
            try {
                repository.updateProfile(profile)
                _profile.value = profile
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error updating profile", e)
            }
        }
    }

    fun updateName(name:String){
        _profile.value=  _profile.value.copy(name=name)

    }

    fun updateBirthDate(birthDate:String){
        _profile.value= _profile.value.copy(birthDate=birthDate)
    }

    fun updateEmail(email:String){
        _profile.value = _profile.value.copy(email=email)
    }

    fun updatePassword(password:String){
        _profile.value= _profile.value.copy(password=password)
    }

    fun createImageUriForCamera(): Uri {
        try {
            val (_, uri) = repository.createImageFile()
            _tempImageUri.value = uri
            return uri
        } catch (e: Exception) {
            Log.e("ProfileViewModel", "Error creating image URI", e)
            throw e
        }
    }

    fun saveProfileImage(uri: Uri) {
        viewModelScope.launch {
            try {
                val localPath = repository.saveProfileImage(uri)
                _profile.value = _profile.value.copy(photoUri = localPath)
                saveCurrentProfile()
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error saving profile image", e)
            }
        }
    }

    fun saveCurrentProfile() {
        viewModelScope.launch {
            try {
                repository.saveProfile(_profile.value)
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error saving current profile", e)
            }
        }
    }

    class Factory(private val context: Context): ViewModelProvider.Factory {
        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                val database = AppDatabase.getDatabase(context)
                val repository = ProfileRepository(context, database.profileDao())
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}