package com.example.bancodigital.presenter.auth.register


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.bancodigital.domain.user.RegisterUseCase
import com.example.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUsecase: RegisterUseCase
) : ViewModel()  {

    fun register(
        name:String,
        email:String,
        telefone: String,
        password: String
    ) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val user = registerUsecase.invoke(name, email, telefone, password)

            emit(StateView.Sucess(user))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }
}