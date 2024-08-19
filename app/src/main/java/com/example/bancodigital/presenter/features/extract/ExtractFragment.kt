package com.example.bancodigital.presenter.features.extract

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bancodigital.MainGraphDirections
import com.example.bancodigital.data.enum.TransactionOperation
import com.example.bancodigital.databinding.FragmentExtractBinding
import com.example.bancodigital.presenter.home.TransactionAdapter
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ExtractFragment : Fragment() {

    private var _binding: FragmentExtractBinding? = null
    private val extractViewModel : ExtractViewModel by viewModels()
    private lateinit var adapterTransaction: TransactionAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExtractBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, light = false)

        initRecyclerViewTransactions()

        getTransactions()
    }


    private fun initRecyclerViewTransactions(){
        adapterTransaction = TransactionAdapter(requireContext()){ transaction ->
            when(transaction.operation) {
                TransactionOperation.DEPOSIT -> {
                    val action = MainGraphDirections
                        .actionGlobalDepositReceiptFragment(transaction.id, true)
                    findNavController().navigate(action)
                }
                TransactionOperation.RECHARGE -> {
                    val action = MainGraphDirections
                        .actionGlobalRechargeReceiptFragment(transaction.id, true)
                    findNavController().navigate(action)
                }
                TransactionOperation.TRANSFER -> {
                    val action = MainGraphDirections.actionGlobalReceiptTransferFragment(transaction.id, true)
                    findNavController().navigate(action)
                }

                else -> {}
            }
        }
        with(binding.rvTransactions) {
            setHasFixedSize(true)
            adapter = adapterTransaction
        }
    }

    private fun getTransactions() {
        extractViewModel.getTransactions().observe(viewLifecycleOwner) {stateView ->
            val listreverse = (stateView.data)?.reversed()
            when(stateView) {
                is StateView.Loading -> {
                    binding.loadRvTransactions.isVisible = true
                }
                is StateView.Error -> {
                    showBottomSheet(message = stateView.message)
                    binding.loadRvTransactions.isVisible = false
                }
                is StateView.Sucess -> {
                    binding.loadRvTransactions.isVisible = false

                    binding.emptyText.isVisible = stateView.data?.isEmpty() == true
                    adapterTransaction.submitList(listreverse)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}