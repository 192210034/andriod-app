package com.simats.aspirebridge.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simats.aspirebridge.data.model.MentorProfile
import com.simats.aspirebridge.databinding.ItemMentorCardBinding

/**
 * Adapter for displaying mentor previews in home screen
 */
class MentorPreviewAdapter(
    private val onMentorClick: (MentorProfile) -> Unit
) : ListAdapter<MentorProfile, MentorPreviewAdapter.MentorViewHolder>(MentorDiffCallback()) {

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

        fun bind(mentor: MentorProfile) {
            with(binding) {
                // TODO: Implement mentor binding when layout is available
                // For now, show placeholder data
                
                // Click listeners
                root.setOnClickListener { onMentorClick(mentor) }
            }
        }
    }

    private class MentorDiffCallback : DiffUtil.ItemCallback<MentorProfile>() {
        override fun areItemsTheSame(oldItem: MentorProfile, newItem: MentorProfile): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: MentorProfile, newItem: MentorProfile): Boolean {
            return oldItem == newItem
        }
    }
}