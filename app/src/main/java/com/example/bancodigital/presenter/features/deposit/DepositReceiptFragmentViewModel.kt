package com.example.bancodigital.presenter.features.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.bancodigital.domain.deposit.GetDepositUseCase
import com.example.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DepositReceiptFragmentViewModel  @Inject constructor(
    private val getDepositUseCase: GetDepositUseCase
) : ViewModel(){

    fun getDeposit(id : String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val resultdeposit = getDepositUseCase.invoke(id)

            emit(StateView.Sucess(resultdeposit))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

}