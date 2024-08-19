package com.example.bancodigital.presenter.features.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bancodigital.R
import com.example.bancodigital.data.model.Deposit
import com.example.bancodigital.databinding.FragmentDepositReceiptBinding
import com.example.bancodigital.util.GetMask
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DepositReceiptFragment : Fragment() {

    private var _binding: FragmentDepositReceiptBinding? = null
    private val binding get() = _binding!!

    private val args: DepositReceiptFragmentArgs by navArgs()
    private val viewmodel : DepositReceiptFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentDepositReceiptBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, args.homeAsUpEnable, false)
        initListener()
    }
    private fun getDeposit() {
        viewmodel.getDeposit(args.iddeposit).observe(viewLifecycleOwner) { stateview ->
            when(stateview) {
                is StateView.Loading -> {

                }
                is StateView.Error -> {
                    Toast.makeText(requireContext(), "Ocorreu um erro", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is StateView.Sucess -> {
                    stateview.data?.let { configData(it)}
                }
            }

        }
    }

    private fun initListener() {
        binding.btnContinue.setOnClickListener {
            if(args.homeAsUpEnable) {
                findNavController().popBackStack()
            }else {
                val navOptions = NavOptions.Builder().setPopUpTo(R.id.depositFormFragment, true).build()
                findNavController().navigate(R.id.action_global_homeFragment, null, navOptions)
            }
        }
        getDeposit()
    }
    private fun configData(deposit : Deposit) {
        binding.textDepositcode.text = deposit.id
        binding.textDateDeposit.text = GetMask.getFormatedDate(deposit.date,GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
        binding.textAmountDeposit.text = getString(R.string.text_formated_value, GetMask.getFormatedValue(deposit.amount))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}