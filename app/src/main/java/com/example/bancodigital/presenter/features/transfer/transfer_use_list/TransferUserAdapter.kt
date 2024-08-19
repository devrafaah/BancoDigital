package com.example.bancodigital.presenter.features.transfer.transfer_use_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bancodigital.R
import com.example.bancodigital.data.model.User
import com.example.bancodigital.databinding.TransferUserItemBinding
import com.squareup.picasso.Picasso

class TransferUserAdapter(
    private val userSelected: (User) -> Unit
) : ListAdapter<User, TransferUserAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(
                oldItem:User,
                newItem:User
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem:User,
                newItem:User
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TransferUserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user = getItem(position)

        holder.binding.textUsername.text = user.name

        if(user.image.isNotEmpty()) {
            Picasso.get()
                .load(user?.image)
                .fit()
                .centerCrop()
                .into(holder.binding.userImage)
        }else {
            holder.binding.userImage.setImageResource(R.drawable.image_user)
        }




        holder.itemView.setOnClickListener {
            userSelected(user)
        }
    }


    inner class ViewHolder(val binding: TransferUserItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}