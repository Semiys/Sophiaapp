package com.example.sophiaapp.data.local.preferences

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sophiaapp.domain.models.Profile

@Entity(tableName="profile")
data class ProfileEntity(
    @PrimaryKey val id: Int=1,
    val name: String,
    val birthDate: String,
    val email: String,
    val password: String,
    val photoPath: String?
){
    fun toProfile(): Profile{
        return Profile(
            name=name,
            birthDate=birthDate,
            email=email,
            password=password,
            photoUri=photoPath
        )
    }

    companion object{
        fun fromProfile(profile:Profile):ProfileEntity{
            return ProfileEntity(
                name=profile.name,
                birthDate=profile.birthDate,
                email=profile.email,
                password=profile.password,
                photoPath=profile.photoUri
            )
        }
    }

}