package com.example.bancodigital.domain.deposit


import com.example.bancodigital.data.model.Deposit
import com.example.bancodigital.data.repository.deposit.DepositDataSourceImp
import javax.inject.Inject

class GetDepositUseCase @Inject constructor(
    private val depositDataSourceImp: DepositDataSourceImp
) {

    suspend operator fun invoke(id: String) : Deposit {
        return depositDataSourceImp.getDeposit(id)
    }
}