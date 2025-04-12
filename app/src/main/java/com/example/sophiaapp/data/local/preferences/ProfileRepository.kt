package com.example.sophiaapp.data.local.preferences

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.example.sophiaapp.data.local.preferences.ProfileDao
import com.example.sophiaapp.data.local.preferences.ProfileEntity
import com.example.sophiaapp.domain.models.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.text.SimpleDateFormat
import java.util.*




class ProfileRepository(
    private val context: Context,
    private val profileDao:ProfileDao

){
    fun getProfile(): Flow<Profile?> {
        return profileDao.getProfile().map { entity ->
            entity?.toProfile()
        }
    }
    suspend fun getProfileSync(): ProfileEntity? {
        return profileDao.getProfileSync()
    }

    suspend fun saveProfile(profile: Profile) {
        val existingProfile = profileDao.getProfileSync()

        if (existingProfile == null) {
            profileDao.insertProfile(ProfileEntity.fromProfile(profile))
        } else {
            profileDao.updateProfile(ProfileEntity.fromProfile(profile))
        }
    }

    suspend fun updateProfile(profile: Profile) {
        profileDao.updateProfile(ProfileEntity.fromProfile(profile))
    }

    fun createImageFile(): Pair<File, Uri> {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"

        // ИЗМЕНЕНО: Используем внутреннюю директорию вместо внешней
        val storageDir = File(context.filesDir, "ProfileImages")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        val image = File.createTempFile(imageFileName, ".jpg", storageDir)

        val imageUri = FileProvider.getUriForFile(
            context,
            "${context.applicationContext.packageName}.fileprovider",
            image
        )
        return Pair(image, imageUri)
    }

    suspend fun saveProfileImage(sourceUri: Uri): String {
        try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val destinationFileName = "PROFILE_${timeStamp}.jpg"

            // ИЗМЕНЕНО: Используем внутреннюю директорию вместо внешней
            val storageDir = File(context.filesDir, "ProfileImages")
            if (!storageDir.exists()) {
                val success = storageDir.mkdirs()
                Log.d("ProfileRepository", "Directory creation result: $success for path ${storageDir.absolutePath}")
            }

            val destinationFile = File(storageDir, destinationFileName)
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                destinationFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            val localPath = destinationFile.absolutePath
            Log.d("ProfileRepository", "Photo saved to: $localPath")

            val existingProfile = profileDao.getProfileSync()
            if (existingProfile != null) {
                val updatedProfile = existingProfile.copy(photoPath = localPath)
                profileDao.updateProfile(updatedProfile)
            } else {
                val newProfile = ProfileEntity(
                    id = 1,
                    name = "",
                    birthDate = "",
                    email = "",
                    password = "",
                    photoPath = localPath
                )
                profileDao.insertProfile(newProfile)
            }

            // Удаляем старый файл, если он существует
            existingProfile?.photoPath?.let { oldPath ->
                if (oldPath != localPath) {
                    val oldFile = File(oldPath)
                    if (oldFile.exists() && oldFile.isFile) {
                        val deleted = oldFile.delete()
                        Log.d("ProfileRepository", "Old file deleted: $deleted")
                    }
                }
            }

            return localPath

        } catch (e: Exception) {
            Log.e("ProfileRepository", "Error saving profile image", e)
            throw e
        }
    }
}


