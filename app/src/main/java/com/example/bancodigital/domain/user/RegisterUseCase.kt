package com.example.bancodigital.domain.user

import com.example.bancodigital.data.model.User
import com.example.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    val authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
) {
    suspend operator fun invoke(
        name:String,
        email:String,
        telefone: String,
        password: String
    ): User{
        return authFirebaseDataSourceImpl.register(name, email,telefone, password)
    }
}