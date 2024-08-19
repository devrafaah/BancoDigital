package com.example.bancodigital.data.model

import android.os.Parcelable
import com.example.bancodigital.data.enum.TransactionOperation
import com.example.bancodigital.data.enum.TransactionType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(
    var id : String = "",
    val operation: TransactionOperation? = null,
    val date : Long = 0,
    val amount : Float = 0f,
    var type: TransactionType? = null
) : Parcelable
