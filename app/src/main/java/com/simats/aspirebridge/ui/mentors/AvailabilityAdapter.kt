package com.simats.aspirebridge.ui.mentors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simats.aspirebridge.data.model.AvailabilitySlot
import com.simats.aspirebridge.databinding.ItemAvailabilitySlotBinding
import java.text.SimpleDateFormat
import java.util.*

class AvailabilityAdapter(
    private val onSlotClick: (AvailabilitySlot) -> Unit
) : ListAdapter<AvailabilitySlot, AvailabilityAdapter.AvailabilityViewHolder>(AvailabilityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailabilityViewHolder {
        val binding = ItemAvailabilitySlotBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AvailabilityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AvailabilityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AvailabilityViewHolder(
        private val binding: ItemAvailabilitySlotBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(slot: AvailabilitySlot) {
            binding.apply {
                // Format date
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val displayDateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
                
                try {
                    val date = dateFormat.parse(slot.date)
                    tvDate.text = displayDateFormat.format(date ?: Date())
                } catch (e: Exception) {
                    tvDate.text = slot.date
                }

                // Format time
                tvTime.text = "${slot.startTime} - ${slot.endTime}"

                // Set availability status
                if (slot.isBooked) {
                    tvStatus.text = "Booked"
                    cardSlot.isEnabled = false
                    cardSlot.alpha = 0.6f
                } else {
                    tvStatus.text = "Available"
                    cardSlot.isEnabled = true
                    cardSlot.alpha = 1.0f
                }

                // Handle click
                cardSlot.setOnClickListener {
                    if (!slot.isBooked) {
                        onSlotClick(slot)
                    }
                }
            }
        }
    }

    private class AvailabilityDiffCallback : DiffUtil.ItemCallback<AvailabilitySlot>() {
        override fun areItemsTheSame(oldItem: AvailabilitySlot, newItem: AvailabilitySlot): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AvailabilitySlot, newItem: AvailabilitySlot): Boolean {
            return oldItem == newItem
        }
    }
}