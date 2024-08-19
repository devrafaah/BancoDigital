package com.example.bancodigital.presenter.auth.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.bancodigital.R
import com.example.bancodigital.databinding.FragmentLoginBinding
import com.example.bancodigital.util.BaseFragment
import com.example.bancodigital.util.FirebaseHelper
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val loginViewModel: LoginViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners(){
        binding.txtNewAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.txtRecoverAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverFragment)
        }
        binding.btnValidate.setOnClickListener { validateData() }
    }

    private fun validateData() {
        val email = binding.userEmail.text.toString().trim()
        val password = binding.userPassword.text.toString().trim()


        if(email.isNotEmpty()){
            if(password.isNotEmpty()){
                hideKeyboard()
                loginUser(email, password)
            }else{
                showBottomSheet(message = getString(R.string.text_senha_empty))
            }
        }else {
            showBottomSheet(message = getString(R.string.text_email_empty))
        }
    }

    private fun loginUser(email: String, senha: String) {
        loginViewModel.login(email,senha).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    Log.i("INFOTESTE", "loginUser: ${stateView.message}")
                    showBottomSheet(
                        message = getString(FirebaseHelper.validError(stateView.message ?: ""))
                    )
                }
                is StateView.Sucess -> {
                    binding.progressBar.isVisible = false
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