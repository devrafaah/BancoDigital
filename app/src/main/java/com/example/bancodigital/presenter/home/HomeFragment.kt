package com.example.bancodigital.presenter.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.bancodigital.MainGraphDirections
import com.example.bancodigital.R
import com.example.bancodigital.data.enum.TransactionOperation
import com.example.bancodigital.data.enum.TransactionType
import com.example.bancodigital.data.model.Transaction
import com.example.bancodigital.data.model.User
import com.example.bancodigital.databinding.FragmentHomeBinding
import com.example.bancodigital.util.FirebaseHelper
import com.example.bancodigital.util.GetMask
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.showBottomSheet
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel : HomeViewModel by viewModels()
    private val tagPicasso = "tagPicasso"
    private lateinit var adapterTransaction: TransactionAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerViewTransactions()


        getTransactions()

        initListener()
    }

    private fun initListener() {
        getProfile()
        binding.btnLogout.setOnClickListener {
            FirebaseHelper.getAuth().signOut()

            val navOptions = NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
            findNavController().navigate(R.id.action_global_authentication, null, navOptions)
        }
        binding.btnDeposit.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_depositFormFragment)
        }

        binding.btnShowAll.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_extractFragment)
        }
        binding.btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.btnExtract.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_extractFragment)
        }
        binding.btnRecharge.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_rechargeFormFragment)
        }
        binding.btnTransfer.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_transferUserListFragment)
        }
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

    private fun getProfile() {
        homeViewModel.getProfile().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> null
                is StateView.Error -> {
                    showBottomSheet(
                        message = getString(FirebaseHelper.validError(stateView.message ?: ""))
                    )
                }
                is StateView.Sucess -> {
                    stateView.data?.let {
                        configData(it)
                    }
                }
            }
        }
    }

    private fun configData(user: User?) {
        if(user?.image?.isNotEmpty() == true) {
            Picasso.get()
                .load(user?.image)
                .tag(tagPicasso)
                .fit()
                .centerCrop()
                .into(binding.userImage, object : Callback {
                    override fun onSuccess() {
                        binding.progressbar.isVisible = false
                        binding.userImage.isVisible = true
                    }

                    override fun onError(e: Exception?) {
                        binding.progressbar.isVisible = false
                        binding.userImage.isVisible = true
                    }

                })
        } else {
            binding.progressbar.isVisible = false
            binding.userImage.isVisible = true
            binding.userImage.setImageResource(R.drawable.image_user)
        }
    }

    private fun getTransactions() {
        homeViewModel.getTransactions().observe(viewLifecycleOwner) { stateView ->
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
                    adapterTransaction.submitList(listreverse?.take(6))


                    binding.emptyText.isVisible = stateView.data?.isEmpty() == true
                    showBalance(stateView.data ?: emptyList())
                }
            }
        }
    }

    private fun showBalance(transactions: List<Transaction>) {
        var cashIn = 0f
        var cashOut = 0f

        transactions.forEach { transaction ->
            if(transaction.type == TransactionType.CASH_IN) {
                cashIn += transaction.amount
            }else {
                cashOut += transaction.amount
            }
        }
        binding.textBalance.text = getString(R.string.text_formated_value, GetMask.getFormatedValue(cashIn - cashOut))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Picasso.get()?.cancelTag(tagPicasso)
        _binding = null
    }
}