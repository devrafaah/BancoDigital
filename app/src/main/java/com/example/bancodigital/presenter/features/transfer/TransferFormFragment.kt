package com.example.bancodigital.presenter.features.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bancodigital.R
import com.example.bancodigital.databinding.FragmentTransferFormBinding
import com.example.bancodigital.util.BaseFragment
import com.example.bancodigital.util.MoneyTextWatcher
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TransferFormFragment : BaseFragment() {
    private var _binding: FragmentTransferFormBinding? = null
    private val binding get() = _binding!!
    private val args: TransferFormFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentTransferFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, light = true)
        initListener()
    }
    private fun initListener() {
        binding.textAmount.addTextChangedListener(MoneyTextWatcher(binding.textAmount))
        binding.btnTransferConfirm.setOnClickListener {
            validateTransfer()
        }

        with(binding.textAmount) {
            addTextChangedListener {
                if(MoneyTextWatcher.getValueUnMasked(binding.textAmount) > 99999.99F) {
                    binding.textAmount.setText(getString(R.string.text_transferFormFragment_DefaultValue))
                }
            }
            doAfterTextChanged {
                this.text?.length?.let { this.setSelection(it)}
            }
        }
    }


    private fun validateTransfer() {
        val amount = MoneyTextWatcher.getValueUnMasked(binding.textAmount)

        if(amount > 0f) {
            val action = TransferFormFragmentDirections.actionTransferFormFragmentToTransferReceiptFragment(
                args.user,
                amount
            )
            findNavController().navigate(action)
            hideKeyboard()
        }else {
            showBottomSheet(message = getString(R.string.text_transferFormFragment_warning_empty_value))
        }
    }
}