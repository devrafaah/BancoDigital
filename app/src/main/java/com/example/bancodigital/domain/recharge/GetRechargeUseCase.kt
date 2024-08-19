package com.example.bancodigital.domain.recharge


import com.example.bancodigital.data.model.Recharge
import com.example.bancodigital.data.repository.recharge.RechargeDataSourceImp
import javax.inject.Inject

class GetRechargeUseCase @Inject constructor(
    private val rechargeDataSourceImp: RechargeDataSourceImp
) {

    suspend operator fun invoke(id: String) : Recharge {
        return rechargeDataSourceImp.getRecharge(id)
    }
}