package com.example.bancodigital.presenter.features.transfer.transfer_confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bancodigital.MainGraphDirections
import com.example.bancodigital.R
import com.example.bancodigital.data.model.Transfer
import com.example.bancodigital.databinding.FragmentTransferConfirmBinding
import com.example.bancodigital.util.FirebaseHelper
import com.example.bancodigital.util.GetMask
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheet
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TransferConfirmFragment : Fragment() {
    private var _binding: FragmentTransferConfirmBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ConfirmTransferViewModel by viewModels()

    private val args: TransferConfirmFragmentArgs by navArgs()

    private val tagPicasso = "tagPicasso"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransferConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, light = false)
        
        initListener()
    }

    private fun initListener() {

        configData()
        binding.btnContinue.setOnClickListener {
            binding.btnContinue.isEnabled = false
            getBalance()
        }
    }

    private fun configData() {
        val imageUrl = args.user.image
        if (!imageUrl.isNullOrEmpty()) {
            binding.progressbar.isVisible = true
            Picasso.get()
                .load(imageUrl)
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
            binding.userImage.setImageResource(R.drawable.image_user)
            binding.progressbar.isVisible = false
            binding.userImage.isVisible = true
        }

        binding.textUserName.text = args.user.name
        binding.textUserAmount.text = getString(R.string.text_formated_value, GetMask.getFormatedValue(args.amount))
    }

    private fun getBalance() {
        viewModel.getBalance().observe(viewLifecycleOwner) { stateView ->
            
            when(stateView) {
                is StateView.Loading -> {
                    binding.progressBar2.isVisible = true
                }
                is StateView.Error -> {
                    binding.btnContinue.isEnabled = true
                    binding.progressBar2.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
                is StateView.Sucess -> {
                    if((stateView.data ?: 0f) >= args.amount){
                        val transfer = Transfer(
                            id = args.user.id,
                            idUserSend = FirebaseHelper.getUserId(),
                            idUserReceived = args.user.id,
                            amount = args.amount,
                        )
                        saveTransfer(transfer)
                    }else {
                        binding.btnContinue.isEnabled = true
                        binding.progressBar2.isVisible = false
                        showBottomSheet(message = getString(R.string.text_TransferConfirmFragment_insufficientBalance))
                    }
                }
            }
        }
    }

    private fun saveTransfer(transfer: Transfer) {
        viewModel.saveTransfer(transfer).observe(viewLifecycleOwner) { stateView ->

            when(stateView) {
                is StateView.Loading -> {
                }
                is StateView.Error -> {
                    binding.progressBar2.isVisible = false
                    binding.btnContinue.isEnabled = true
                    showBottomSheet(message = stateView.message)
                }
                is StateView.Sucess -> {
                    updateTransfer(transfer)
                }
            }
        }
    }

    private fun updateTransfer(transfer: Transfer) {
        viewModel.updateTransfer(transfer).observe(viewLifecycleOwner) { stateView ->

            when(stateView) {
                is StateView.Loading -> {
                }
                is StateView.Error -> {
                    binding.progressBar2.isVisible = false
                    binding.btnContinue.isEnabled = true
                    showBottomSheet(message = stateView.message)
                }
                is StateView.Sucess -> {
                    saveTransaction(transfer)
                }
            }
        }
    }

    private fun saveTransaction(transfer: Transfer) {
        viewModel.saveTransaction(transfer).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {

                }
                is StateView.Error -> {
                    binding.progressBar2.isVisible = false
                    binding.btnContinue.isEnabled = true
                }
                is StateView.Sucess -> {
                    val action = MainGraphDirections.actionGlobalReceiptTransferFragment(transfer.id, false)
                    binding.progressBar2.isVisible = false
                    Toast.makeText(requireContext(), "Trensferência realizada com sucesso", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(action)

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Picasso.get()?.cancelTag(tagPicasso)
    }

}