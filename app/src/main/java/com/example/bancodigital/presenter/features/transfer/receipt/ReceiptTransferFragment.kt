package com.example.bancodigital.presenter.features.transfer.receipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bancodigital.R
import com.example.bancodigital.data.model.Transfer
import com.example.bancodigital.data.model.User
import com.example.bancodigital.databinding.FragmentReceiptTransferBinding
import com.example.bancodigital.util.FirebaseHelper
import com.example.bancodigital.util.GetMask
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReceiptTransferFragment : Fragment() {

    private var _binding: FragmentReceiptTransferBinding? = null
    private val binding  get() = _binding!!
    private val tagPicasso = "tagPicasso"



    private val receiptTransferViewModel: ReceiptTransferViewModel by viewModels()
    private val args: ReceiptTransferFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReceiptTransferBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar, homeAsUpEnabled = args.homeAsUpEnabled)
        getTransfer()
        initListener()

    }

    private fun initListener() {
        binding.btnReceipt.setOnClickListener {
            if(args.homeAsUpEnabled) {
                findNavController().popBackStack()
            }else {
                val navOptions = NavOptions.Builder().setPopUpTo(R.id.transferUserListFragment, true).build()
                findNavController().navigate(R.id.action_global_homeFragment, null, navOptions)
            }
        }
    }

    private fun getTransfer() {
        receiptTransferViewModel.getTransfer(args.transferId).observe(viewLifecycleOwner) { stateview ->
            when(stateview) {
                is StateView.Loading -> {

                }
                is StateView.Error -> {
                    Toast.makeText(requireContext(), "Ocorreu um erro", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is StateView.Sucess -> {
                    stateview.data?.let {transfer ->
                        val userID = if(transfer.idUserSend == FirebaseHelper.getUserId()) {
                            transfer.idUserReceived

                        }else {
                            transfer.idUserSend
                        }

                        getProfile(userID)
                        configTransfer(transfer)
                    }
                }
            }
        }
    }


    private fun configTransfer(transfer: Transfer) {
        if(transfer.idUserSend == FirebaseHelper.getUserId()) {
            binding.textView6.text = getString(R.string.text_TransferReceiptFragment_title_send)
            binding.textView8.text = getString(R.string.text_TransferReceiptFragment_subtitle_send)

        }else{
            binding.textView6.text = getString(R.string.text_TransferReceiptFragment_title_received)
            binding.textView8.text = getString(R.string.text_TransferReceiptFragment_subtitle_received)
        }

        binding.textCodeReceipt.text = transfer.id
        binding.textDateReceipt.text = GetMask.getFormatedDate(transfer.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
        binding.textAmountReceipt.text = getString(R.string.text_formated_value, GetMask.getFormatedValue(transfer.amount))
    }
    private fun configProfile(user: User) {
        if (user.image.isNotEmpty()) {
            binding.progressbar.isVisible = true
            Picasso.get()
                .load(user.image)
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

        binding.textNameReceipt.text = user.name
    }


    private fun getProfile(id : String) {
        receiptTransferViewModel.getProfile(id).observe(viewLifecycleOwner) { stateview ->
            when(stateview) {
                is StateView.Loading -> {

                }
                is StateView.Error -> {
                    Toast.makeText(requireContext(), "Ocorreu um erro", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is StateView.Sucess -> {
                    stateview.data?.let { configProfile(it)}
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Picasso.get()?.cancelTag(tagPicasso)
        _binding = null
    }


}