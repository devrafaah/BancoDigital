package com.example.bancodigital.domain.user

import com.example.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl
import javax.inject.Inject



class LoginUseCase @Inject constructor(
    val authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
) {
    suspend operator fun invoke(email: String, senha: String) {
        return authFirebaseDataSourceImpl.login(email,senha)
    }
}