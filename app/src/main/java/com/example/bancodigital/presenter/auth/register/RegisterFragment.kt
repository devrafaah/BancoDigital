package com.example.bancodigital.presenter.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bancodigital.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
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
    }

    private fun listeners() {
        binding.btnCadastrarUser.setOnClickListener {
            // cadastrar novo usuario
            validationNewUser()
        }
    }

    private fun validationNewUser() {
        val nome = binding.txtNewuserNome.text.trim().toString()
        val email = binding.txtNewuserNome.text.trim().toString()
        val telefone = binding.txtNewuserNome.text.trim().toString()
        val senha = binding.txtNewuserNome.text.trim().toString()
        val confirmarSenha = binding.txtNewuserConfirmarSenha.text.trim().toString()

        if(nome.isNotEmpty()) {
            if(email.isNotEmpty()) {
                if(telefone.isNotEmpty()) {
                    if(senha.isNotEmpty()) {
                        if(senha == confirmarSenha) {
                            Toast.makeText(requireContext(), "Registrando Conta....", Toast.LENGTH_SHORT).show()
                        }else {
                            Toast.makeText(requireContext(), "Digite a mesma senha ambos os campos", Toast.LENGTH_SHORT).show()
                        }
                    }else {
                        Toast.makeText(requireContext(), "Digite sua senha", Toast.LENGTH_SHORT).show()
                    }
                }else {
                    Toast.makeText(requireContext(), "Digite seu Telefone", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(), "Digite seu Email", Toast.LENGTH_SHORT).show()
            }
        }else {
            Toast.makeText(requireContext(), "Digite seu nome", Toast.LENGTH_SHORT).show()
        }

    }
}