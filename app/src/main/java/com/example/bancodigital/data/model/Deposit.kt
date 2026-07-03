package com.example.bancodigital.data.model

import android.os.Parcelable
import com.google.firebase.database.FirebaseDatabase
import kotlinx.parcelize.Parcelize

@Parcelize
data class Deposit (
    var id : String = "",
    var date: Long = 0,
    var amount: Float = 0f,
) : Parcelable {
    companion object {
        fun getIdFromFirebase() : String {
            val id = FirebaseDatabase.getInstance().reference.push().key ?: ""
            return id
        }
    }
}
