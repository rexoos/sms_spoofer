package com.example.smsspoof.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smsspoof.R
import com.example.smsspoof.model.Sender

class SenderAdapter(
    private val onSenderClick: (Sender) -> Unit
) : ListAdapter<Sender, SenderAdapter.SenderViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Sender>() {
            override fun areItemsTheSame(oldItem: Sender, newItem: Sender): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Sender, newItem: Sender): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewSenderName: TextView = itemView.findViewById(R.id.textViewSenderName)
        val switchSenderEnabled: Switch = itemView.findViewById(R.id.switchSenderEnabled)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SenderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sender, parent, false)
        return SenderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SenderViewHolder, position: Int) {
        val sender = getItem(position)
        holder.textViewSenderName.text = sender.name
        holder.switchSenderEnabled.isChecked = sender.isEnabled

        // Handle switch changes
        holder.switchSenderEnabled.setOnCheckedChangeListener { buttonView, isChecked ->
            // Update the sender's enabled state
            val updatedSender = sender.copy(isEnabled = isChecked)
            // Notify the activity to update the sender in the database
            // We'll need to pass a callback for this or use a ViewModel
            // For now, we'll just update the list item
            val currentPos = holder.bindingAdapterPosition
            if (currentPos != RecyclerView.NO_POSITION) {
                submitList(getCurrentList().apply { set(currentPos, updatedSender) })
            }
        }

        // Handle item click
        holder.itemView.setOnClickListener { onSenderClick(sender) }
    }

    fun getSenderAt(position: Int): Sender? {
        return if (position in 0 until itemCount) getItem(position) else null
    }

    fun getSenderById(id: Long): Sender? {
        return currentList.firstOrNull { it.id == id }
    }
}
