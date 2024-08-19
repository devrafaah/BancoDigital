package com.example.bancodigital.data.repository.auth

import com.example.bancodigital.data.model.User

interface AuthFirebaseDataSource {

    suspend fun login(email: String,senha:String)
    suspend fun register(
        name:String,
        email:String,
        telefone: String,
        password: String
    ) : User
    suspend fun recover(email: String)
}