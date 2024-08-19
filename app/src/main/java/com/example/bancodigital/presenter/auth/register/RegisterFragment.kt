package com.example.bancodigital.presenter.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.bancodigital.R
import com.example.bancodigital.data.model.User
import com.example.bancodigital.databinding.FragmentRegisterBinding
import com.example.bancodigital.presenter.profile.ProfileViewModel
import com.example.bancodigital.util.BaseFragment
import com.example.bancodigital.util.FirebaseHelper
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment : BaseFragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val registerViewModel: RegisterViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        initListeners()
    }

    private fun initListeners(){
        binding.btnRegister.setOnClickListener { validateData() }
    }

    private fun validateData() {
        val nome = binding.registerUserName.text.toString().trim()
        val email = binding.registerUserEmail.text.toString().trim()
        val telefone = binding.registerUserTelefone.unMaskedText
        val password = binding.registerUserPassword.text.toString().trim()


       if(nome.isNotEmpty()){
           if(email.isNotEmpty()){
               if(telefone?.isNotEmpty() == true){
                   if(telefone.length == 11){
                       if(password.isNotEmpty()){
                           hideKeyboard()
                           registerUser(nome,email,telefone, password)
                       }else{
                           showBottomSheet(message = getString(R.string.text_senha_empty))
                       }
                   }else{
                       showBottomSheet(message = getString(R.string.text_phone_invalid))
                   }

               }else{
                   showBottomSheet(message = getString(R.string.text_telefone_empty))
               }
           }else{
               showBottomSheet(message = getString(R.string.text_email_empty))
           }
       }else{
           showBottomSheet(message = getString(R.string.text_name_empty))
       }
    }



    private fun registerUser(name:String, email:String, telefone: String, password: String) {
        registerViewModel.register(name, email, telefone, password).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(
                        message = getString(FirebaseHelper.validError(stateView.message ?: ""))
                    )
                }
                is StateView.Sucess -> {
                    stateView.data?.let{
                        saveProfile(it)
                    }
                }
            }
        }
    }

    private fun saveProfile(user: User){
        profileViewModel.saveProfile(user).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(
                        message = getString(FirebaseHelper.validError(stateView.message ?: ""))
                    )
                }
                is StateView.Sucess -> {
                    val navOptions = NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()
                    findNavController().navigate(R.id.action_global_homeFragment, null, navOptions)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}