package com.example.bancodigital.domain.deposit


import com.example.bancodigital.data.model.Deposit
import com.example.bancodigital.data.repository.deposit.DepositDataSourceImp
import javax.inject.Inject

class SaveDepositUseCase @Inject constructor(
    private val depositDataSourceImp: DepositDataSourceImp
) {

    suspend operator fun invoke(deposit: Deposit) : Deposit {
        return depositDataSourceImp.saveDeposit(deposit)
    }
}