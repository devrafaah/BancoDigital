package com.example.bancodigital.domain.transaction

import com.example.bancodigital.data.enum.TransactionType
import com.example.bancodigital.data.repository.transaction.TransactionDataSourceImp
import javax.inject.Inject

class GetBalanceUseCase @Inject constructor(
    private val transactionDataSourceImp: TransactionDataSourceImp
) {
    suspend operator fun invoke() : Float{
        var cashIn = 0f
        var cashOut = 0f

        transactionDataSourceImp.getTransactions().forEach { transaction ->
            if(transaction.type == TransactionType.CASH_IN) {
                cashIn += transaction.amount
            }else {
                cashOut += transaction.amount
            }
        }



        return cashIn - cashOut
    }
}