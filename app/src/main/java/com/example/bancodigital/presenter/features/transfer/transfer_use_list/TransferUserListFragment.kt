package com.example.bancodigital.presenter.features.transfer.transfer_use_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bancodigital.R
import com.example.bancodigital.data.model.User
import com.example.bancodigital.databinding.FragmentTransferUserListBinding
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheet
import com.ferfalk.simplesearchview.SimpleSearchView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TransferUserListFragment : Fragment() {
    private var _binding: FragmentTransferUserListBinding? = null
    private val binding get() = _binding!!
    private val transferUserListViewModel : TransferUserListViewModel by viewModels()

    private lateinit var transferUserAdapter : TransferUserAdapter

    private var profileList: List<User> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, light = true)
        initListener()
    }
    private fun initListener() {
        initRecyclerView()
        getProfileList()
        configSearchView()
    }
    private fun configSearchView() {
        binding.searchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("SimpleSearchView", "Submit:$query")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return if(newText.isNotEmpty()){
                    val newList = profileList.filter { it.name.contains(newText, true) }
                    emptyUserList(newList)
                    transferUserAdapter.submitList(newList)
                    true
                }else {
                    emptyUserList(profileList)
                    transferUserAdapter.submitList(profileList)
                    false
                }
            }

            override fun onQueryTextCleared(): Boolean {
                Log.d("SimpleSearchView", "Text cleared")
                return false
            }
        })

        binding.searchView.setOnSearchViewListener(object : SimpleSearchView.SearchViewListener {
            override fun onSearchViewShown() {
                Log.d("SimpleSearchView", "onSearchViewShown")
            }

            override fun onSearchViewClosed() {
                emptyUserList(profileList)
                transferUserAdapter.submitList(profileList)
            }

            override fun onSearchViewShownAnimation() {
                Log.d("SimpleSearchView", "onSearchViewShownAnimation")
            }

            override fun onSearchViewClosedAnimation() {
                Log.d("SimpleSearchView", "onSearchViewClosedAnimation")
            }
        })

    }

    private fun emptyUserList(list: List<User>) {
        binding.emptyText.isVisible = list.isEmpty()
    }


    private fun getProfileList() {
        transferUserListViewModel.getProfileList().observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {
                    binding.progressbar.isVisible = true
                }
                is StateView.Error -> {
                    showBottomSheet(message = stateView.message)
                    binding.progressbar.isVisible = false
                }
                is StateView.Sucess -> {
                    transferUserAdapter.submitList(stateView.data)
                    profileList = stateView.data ?: emptyList()
                    binding.progressbar.isVisible = false
                }
            }
        }
    }
    private fun initRecyclerView() {
        transferUserAdapter = TransferUserAdapter { userSelected ->
            val action =
                TransferUserListFragmentDirections.actionTransferUserListFragmentToTransferFormFragment(
                    userSelected
                )
            findNavController().navigate(action)
        }
        with(binding.rvusers) {
            setHasFixedSize(true)
            adapter = transferUserAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search,menu)
        val item = menu.findItem(R.id.action_search)
        binding.searchView.setMenuItem(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}