package com.anmol.otpviewer.ui.smslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anmol.otpviewer.databinding.ItemSmsBinding
import com.anmol.otpviewer.ui.smslist.data.SMSData

class SmsListAdapter(private val onClick: (SMSData) -> Unit) :
    ListAdapter<SMSData, SmsListAdapter.SMSViewHolder>(SMSDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SMSViewHolder {

        return SMSViewHolder(
            ItemSmsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SMSViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class SMSViewHolder(binding: ItemSmsBinding) : RecyclerView.ViewHolder(binding.root) {
        private val tvSender = binding.tvSender
        private val tvContent = binding.tvContent
        private val root = binding.root
        fun bind(item: SMSData) {
            tvSender.text = item.sender
            tvContent.text = item.content

            root.setOnClickListener {
                onClick(item)
            }
        }
    }

    class SMSDiffUtil : DiffUtil.ItemCallback<SMSData>() {
        override fun areItemsTheSame(oldItem: SMSData, newItem: SMSData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SMSData, newItem: SMSData): Boolean =
            oldItem == newItem

    }

}