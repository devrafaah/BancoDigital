package com.example.bancodigital.domain.profile

import com.example.bancodigital.data.model.User
import com.example.bancodigital.data.repository.profile.ProfileDataSourceImp
import javax.inject.Inject

class GetProfileListUseCase @Inject constructor(
    private val profileDataSourceImp : ProfileDataSourceImp
) {

    suspend operator fun invoke() : List<User> {
        return profileDataSourceImp.getProfileList()
    }
}