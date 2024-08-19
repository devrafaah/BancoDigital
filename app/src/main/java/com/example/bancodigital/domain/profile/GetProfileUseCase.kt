package com.example.bancodigital.domain.profile

import com.example.bancodigital.data.model.User
import com.example.bancodigital.data.repository.profile.ProfileDataSourceImp
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val profileDataSourceImp : ProfileDataSourceImp
) {

    suspend operator fun invoke(id : String) : User {
        return profileDataSourceImp.getprofile(id)
    }
}