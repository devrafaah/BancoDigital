package com.example.bancodigital.presenter.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bancodigital.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listeners()
    }
    private fun listeners() {
        binding.btnLoginEntrar.setOnClickListener {
            validarDados()
        }
    }

    private fun validarDados(){
        val email = binding.txtLoginEmail.text.trim().toString()
        val senha = binding.txtLoginSenha.text.trim().toString()

        if(email.isNotEmpty()) {
            if(senha.isNotEmpty()) {
                Toast.makeText(requireContext(), "Entrando na sua conta....", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(requireContext(), "Digite sua senha", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            Toast.makeText(requireContext(), "Digite seu Email", Toast.LENGTH_SHORT).show()
        }
    }
}