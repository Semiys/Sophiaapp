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

    fun createImageFile():Pair<File, Uri>{
        val timeStamp=SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(Date())
        val imageFileName="JPEG_"+"_" + timeStamp + "_"
        val storageDir=context.getExternalFilesDir("ProfileImages")
        val image=File.createTempFile(imageFileName,".jpg",storageDir)

        val imageUri=FileProvider.getUriForFile(
            context,
            "${context.applicationContext.packageName}.fileprovider",
            image
        )
        return Pair(image,imageUri)
    }

    suspend fun saveProfileImage(sourceUri: Uri): String {
        try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val destinationFileName = "PROFILE_${timeStamp}.jpg"
            val storageDir = context.getExternalFilesDir("ProfileImages")
            storageDir?.mkdirs()
            val destinationFile = File(storageDir, destinationFileName)
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                destinationFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            val localPath = destinationFile.absolutePath
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
            existingProfile?.photoPath?.let { oldPath ->
                if (oldPath != localPath) {
                    val oldFile = File(oldPath)
                    if (oldFile.exists() && oldFile.isFile) {
                        oldFile.delete()
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


