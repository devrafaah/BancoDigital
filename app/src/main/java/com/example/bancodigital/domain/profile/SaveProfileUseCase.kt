package com.example.bancodigital.domain.profile

import com.example.bancodigital.data.model.User
import com.example.bancodigital.data.repository.profile.ProfileDataSourceImp
import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
    private val profileDataSourceImp : ProfileDataSourceImp
) {

    suspend operator fun invoke(user: User) {
        return profileDataSourceImp.saveprofile(user)
    }
}