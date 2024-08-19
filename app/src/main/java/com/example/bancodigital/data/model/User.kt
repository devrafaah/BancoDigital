package com.example.bancodigital.data.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "",
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var image: String = "",
    @get:Exclude
    val senha: String = ""
) : Parcelable
