package com.example.bancodigital.presenter.features.transfer.transfer_use_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.bancodigital.domain.profile.GetProfileListUseCase
import com.example.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class TransferUserListViewModel @Inject constructor(
    private val getProfileListUseCase: GetProfileListUseCase
) : ViewModel(){

    fun getProfileList() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val usersList = getProfileListUseCase.invoke()
            emit(StateView.Sucess(usersList))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

}