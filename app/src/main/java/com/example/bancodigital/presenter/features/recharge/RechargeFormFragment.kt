package com.example.bancodigital.presenter.features.recharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bancodigital.MainGraphDirections
import com.example.bancodigital.R
import com.example.bancodigital.data.enum.TransactionOperation
import com.example.bancodigital.data.enum.TransactionType
import com.example.bancodigital.data.model.Recharge
import com.example.bancodigital.data.model.Transaction
import com.example.bancodigital.databinding.FragmentRechargeFormBinding
import com.example.bancodigital.util.BaseFragment
import com.example.bancodigital.util.MoneyTextWatcher
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RechargeFormFragment : BaseFragment() {

    private var _binding: FragmentRechargeFormBinding? = null

    private val viewModel : RechargeViewModel by viewModels()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentRechargeFormBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, light = true)
        initListeners()
    }
    private fun initListeners() {
        with(binding.editRecharge) {
            addTextChangedListener {
                if(MoneyTextWatcher.getValueUnMasked(this) > 100) {
                    setText("R$ 0,00")
                }
            }
            doAfterTextChanged {
                this.text?.length?.let { setSelection(it)}
            }
        }
        binding.editRecharge.addTextChangedListener(MoneyTextWatcher(binding.editRecharge))
        binding.btnConfirm.setOnClickListener { validateData() }
    }

    private fun validateData() {
        val telefone = binding.textNumber.unMaskedText
        val rechargeValue = MoneyTextWatcher.getValueUnMasked(binding.editRecharge)

        if(telefone?.isNotEmpty() == true){
            if(telefone.length == 11) {
                if(rechargeValue >= 10){
                    hideKeyboard()
                    Toast.makeText(requireContext(), "Tudo certo!!", Toast.LENGTH_LONG).show()
                    val recharge = Recharge(
                        number = telefone,
                        amount = rechargeValue.toFloat()
                    )
                    saveRecharge(recharge)
                }else {
                    Toast.makeText(requireContext(), "Digite um valor da recarga maior ou igual à R$ 10,00 reais!!", Toast.LENGTH_SHORT).show()
                }
            }else{
                showBottomSheet(message = getString(R.string.text_phone_invalid))
            }

        }else{
            showBottomSheet(message = getString(R.string.text_telefone_empty))
        }
    }


    private fun saveRecharge(recharge: Recharge) {
        viewModel.saveRecharge(recharge).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
                is StateView.Sucess -> {
                    Toast.makeText(requireContext(), "Salvado Recarga com sucesso", Toast.LENGTH_SHORT).show()
                    binding.progressBar.isVisible = false

                    stateView.data?.let {
                        saveTransaction(it)
                    }
                }
            }
        }
    }

    private fun saveTransaction(recharge: Recharge) {

        val transaction = Transaction(
            id = recharge.id,
            operation = TransactionOperation.RECHARGE,
            date = recharge.date,
            amount = recharge.amount,
            type = TransactionType.CASH_OUT
        )

        viewModel.saveTransaction(transaction).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {

                }
                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
                is StateView.Sucess -> {
                    val action = MainGraphDirections
                        .actionGlobalRechargeReceiptFragment(recharge.id, false)
                    findNavController().navigate(action)
                    binding.progressBar.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}