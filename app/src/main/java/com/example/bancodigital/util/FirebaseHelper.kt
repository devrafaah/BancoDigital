package com.example.bancodigital.util

import com.example.bancodigital.R
import com.google.firebase.auth.FirebaseAuth

class FirebaseHelper {

    companion object {
        fun getAuth() = FirebaseAuth.getInstance()
        fun isAuthenticator() = getAuth().currentUser != null

        fun getUserId() = getAuth().currentUser?.uid ?: ""

        fun validError(error: String) : Int {
            return when{
                error.contains("The supplied auth credential is incorrect") -> R.string.firebaseError_password_or_email_wrong_register_fragment
                error.contains("The email address is badly formatted.") -> R.string.firebaseError_invalid_email_register_fragment
                error.contains("The email address is already in use by another account.") -> R.string.firebaseError_email_in_use_register_fragment
                error.contains("Password should be at least 6 characters") -> R.string.firebaseError_strong_password_register_fragment
                else -> {
                    R.string.error_generic
                }
            }

        }


    }


}