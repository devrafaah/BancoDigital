package com.example.bancodigital.presenter.features.deposit

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
import com.example.bancodigital.data.enum.TransactionOperation
import com.example.bancodigital.data.enum.TransactionType
import com.example.bancodigital.data.model.Deposit
import com.example.bancodigital.data.model.Transaction
import com.example.bancodigital.databinding.FragmentDepositFormBinding
import com.example.bancodigital.util.BaseFragment
import com.example.bancodigital.util.MoneyTextWatcher
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DepositFormFragment : BaseFragment() {

    private var _binding: FragmentDepositFormBinding? = null
    private val depositViewModel : DepositViewModel by viewModels()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentDepositFormBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, light = true)
        initListener()
    }

    private fun initListener() {
        binding.textAmount.addTextChangedListener(MoneyTextWatcher(binding.textAmount))
        binding.btnDepositConfirm.setOnClickListener {
            validateDeposit()
        }

        with(binding.textAmount) {
            addTextChangedListener {
                if(MoneyTextWatcher.getValueUnMasked(binding.textAmount) > 99999.99F) {
                    binding.textAmount.setText("R$ 0,00")
                }
            }
            doAfterTextChanged {
                this.text?.length?.let { this.setSelection(it)}
            }
        }

    }
    private fun validateDeposit() {
        val amount = MoneyTextWatcher.getValueUnMasked(binding.textAmount)

        if(amount >= 10f) {
            val deposit = Deposit(
                amount = amount
            )
            hideKeyboard()
            saveDeposit(deposit)
        }else {
            Toast.makeText(requireContext(), "Digite um valor maior que R$ 10,00 reais", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveDeposit(deposit: Deposit) {
        depositViewModel.saveDeposit(deposit).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
                is StateView.Sucess -> {
                    Toast.makeText(requireContext(), "Salvado com sucesso", Toast.LENGTH_SHORT).show()
                    binding.progressBar.isVisible = false

                    stateView.data?.let {
                        saveTransaction(it)
                    }
                }
            }
        }
    }

    private fun saveTransaction(deposit : Deposit) {

        val transaction = Transaction(
            id = deposit.id,
            operation = TransactionOperation.DEPOSIT,
            date = deposit.date,
            amount = deposit.amount,
            type = TransactionType.CASH_IN
        )

        depositViewModel.saveTransaction(transaction).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {

                }
                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                }
                is StateView.Sucess -> {
                    val action = MainGraphDirections
                        .actionGlobalDepositReceiptFragment(deposit.id, false)
                    findNavController().navigate(action)
                    Toast.makeText(requireContext(), "Salvado com sucesso", Toast.LENGTH_SHORT).show()
                    binding.progressBar.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}