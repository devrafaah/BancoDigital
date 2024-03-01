package com.example.bancodigital.presenter.auth.recover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bancodigital.databinding.FragmentRecoverBinding


class RecoverFragment : Fragment() {

    private var _binding: FragmentRecoverBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentRecoverBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listeners()
    }
    private fun listeners() {
        binding.btnRecover.setOnClickListener {
            validarDados()
        }
    }

    private fun validarDados(){
        val email = binding.txtRecoverEmail.text.trim().toString()

        if(email.isNotEmpty()) {
            Toast.makeText(requireContext(), "Enviando Email....", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(requireContext(), "Digite seu Email", Toast.LENGTH_SHORT).show()
        }
    }
}