package com.example.bancodigital.domain.user

import com.example.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl
import javax.inject.Inject

class RecoverUseCase @Inject constructor(
    private val authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
) {
    suspend operator fun invoke(email:String){
        return authFirebaseDataSourceImpl.recover(email);
    }
}