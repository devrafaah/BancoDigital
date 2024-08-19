package com.example.bancodigital.domain.transaction

import com.example.bancodigital.data.model.Transaction
import com.example.bancodigital.data.repository.transaction.TransactionDataSourceImp
import javax.inject.Inject

class GetTransactionUseCase @Inject constructor(
    private val transactionDataSourceImp: TransactionDataSourceImp
) {
    suspend operator fun invoke() : List<Transaction>{
        return transactionDataSourceImp.getTransactions()
    }
}