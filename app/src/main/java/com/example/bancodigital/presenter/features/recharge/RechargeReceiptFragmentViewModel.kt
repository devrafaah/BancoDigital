package com.example.bancodigital.presenter.features.recharge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.bancodigital.domain.recharge.GetRechargeUseCase
import com.example.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RechargeReceiptFragmentViewModel  @Inject constructor(
    private val getRechargeUseCase: GetRechargeUseCase
) : ViewModel(){

    fun getRecharge(id : String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val resultRecharge = getRechargeUseCase.invoke(id)

            emit(StateView.Sucess(resultRecharge))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

}