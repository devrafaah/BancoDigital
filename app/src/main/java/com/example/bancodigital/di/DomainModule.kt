package com.example.bancodigital.di

import com.example.bancodigital.data.repository.auth.AuthFirebaseDataSource
import com.example.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl
import com.example.bancodigital.data.repository.deposit.DepositDataSource
import com.example.bancodigital.data.repository.deposit.DepositDataSourceImp
import com.example.bancodigital.data.repository.recharge.RechargeDataSource
import com.example.bancodigital.data.repository.recharge.RechargeDataSourceImp
import com.example.bancodigital.data.repository.transaction.TransactionDataSource
import com.example.bancodigital.data.repository.transaction.TransactionDataSourceImp
import com.example.bancodigital.data.repository.transfer.TransferDataSource
import com.example.bancodigital.data.repository.transfer.TransferDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule{
    @Binds
    abstract fun bindsAuthDataSource(
        authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
    ) : AuthFirebaseDataSource


    @Binds
    abstract fun bindsDepositDataSource(
        depositDataSourceImp: DepositDataSourceImp
    ) : DepositDataSource


    @Binds
    abstract fun bindsTransactionDataSource(
        transactionDataSourceImp: TransactionDataSourceImp
    ) : TransactionDataSource

    @Binds
    abstract fun bindsRechargeDataSource(
        rechargeDataSourceImp: RechargeDataSourceImp
    ) : RechargeDataSource

    @Binds
    abstract fun bindsTransferDataSource(
        transferDataSourceImp: TransferDataSourceImp
    ) : TransferDataSource
}