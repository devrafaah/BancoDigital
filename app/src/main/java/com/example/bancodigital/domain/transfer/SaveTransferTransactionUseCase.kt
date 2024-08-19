package com.example.bancodigital.domain.transfer

import com.example.bancodigital.data.model.Transfer
import com.example.bancodigital.data.repository.transfer.TransferDataSourceImp
import javax.inject.Inject

class SaveTransferTransactionUseCase @Inject constructor(
    private val transferDataSourceImp: TransferDataSourceImp
) {
    suspend operator fun invoke(transfer: Transfer) {
        transferDataSourceImp.saveTransferTransaction(transfer)
    }

}