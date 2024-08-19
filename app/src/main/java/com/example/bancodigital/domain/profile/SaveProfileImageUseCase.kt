package com.example.bancodigital.domain.profile

import com.example.bancodigital.data.repository.profile.ProfileDataSourceImp
import javax.inject.Inject

class SaveProfileImageUseCase @Inject constructor(
    private val profileDataSourceImp : ProfileDataSourceImp
) {
    suspend operator fun invoke(imageProfile: String) : String {
        return profileDataSourceImp.saveImage(imageProfile)
    }
}