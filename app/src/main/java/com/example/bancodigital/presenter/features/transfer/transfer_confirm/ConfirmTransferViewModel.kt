package com.example.bancodigital.presenter.features.transfer.transfer_confirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.bancodigital.data.model.Transfer
import com.example.bancodigital.domain.transaction.GetBalanceUseCase
import com.example.bancodigital.domain.transfer.SaveTransferTransactionUseCase
import com.example.bancodigital.domain.transfer.SaveTransferUseCase
import com.example.bancodigital.domain.transfer.UpdateTransferUseCase
import com.example.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ConfirmTransferViewModel @Inject constructor(
    private val getBalanceUseCase : GetBalanceUseCase,
    private val saveTransferUseCase: SaveTransferUseCase,
    private val updateTransferUseCase: UpdateTransferUseCase,
    private val saveTransferTransactionUseCase: SaveTransferTransactionUseCase
) : ViewModel(){

    fun getBalance() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val balance = getBalanceUseCase.invoke()

            emit(StateView.Sucess(balance))
        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun saveTransfer(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            saveTransferUseCase.invoke(transfer)

            emit(StateView.Sucess(Unit))
        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun updateTransfer(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            updateTransferUseCase.invoke(transfer)

            emit(StateView.Sucess(Unit))
        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun saveTransaction(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            saveTransferTransactionUseCase.invoke(transfer)

            emit(StateView.Sucess(transfer))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }
}