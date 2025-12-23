package com.simats.aspirebridge.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simats.aspirebridge.databinding.ItemMentorCardBinding

/**
 * Adapter for displaying mentor previews in home screen
 * TODO: Replace Any with proper MentorProfile model when available
 */
class MentorPreviewAdapter(
    private val onMentorClick: (Any) -> Unit
) : ListAdapter<Any, MentorPreviewAdapter.MentorViewHolder>(MentorDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorViewHolder {
        val binding = ItemMentorCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MentorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MentorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MentorViewHolder(
        private val binding: ItemMentorCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(mentor: Any) {
            with(binding) {
                // TODO: Implement mentor binding when MentorProfile model is available
                // For now, show placeholder data
                
                // Click listeners
                root.setOnClickListener { onMentorClick(mentor) }
            }
        }
    }

    private class MentorDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            // TODO: Implement proper comparison when MentorProfile model is available
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            // TODO: Implement proper comparison when MentorProfile model is available
            return oldItem == newItem
        }
    }
}