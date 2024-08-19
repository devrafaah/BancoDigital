package com.example.bancodigital.data.repository.auth

import com.example.bancodigital.data.model.User
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine


class AuthFirebaseDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthFirebaseDataSource{
    override suspend fun login(email: String, senha: String) {
        return suspendCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        continuation.resumeWith(Result.success(Unit))
                    } else {
                        task.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

    override suspend fun register(name: String, email: String, telefone: String, password: String) : User {
        return suspendCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //registrar no firebase com success
                        val userId = task.result.user?.uid ?: ""
                        val user = User(
                            id = userId,
                            name = name,
                            email = email,
                            phone = telefone,
                            senha = password
                        )
                        continuation.resumeWith(Result.success(user))
                    } else {
                        //error
                        task.exception?.let{
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

    override suspend fun recover(email: String) {
        return suspendCoroutine { continuation ->
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        continuation.resumeWith(Result.success(Unit))
                    } else {
                        task.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }
}