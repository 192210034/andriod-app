package com.simats.aspirebridge.ui.mentors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.simats.aspirebridge.R
import com.simats.aspirebridge.data.model.Mentor
import com.simats.aspirebridge.databinding.ItemMentorCardBinding

class MentorsAdapter(
    private val onMentorClick: (Mentor) -> Unit,
    private val onBookSessionClick: (Mentor) -> Unit,
    private val onFollowClick: (Mentor) -> Unit
) : ListAdapter<Mentor, MentorsAdapter.MentorViewHolder>(MentorDiffCallback()) {

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

        fun bind(mentor: Mentor) {
            binding.apply {
                // Basic info
                tvMentorName.text = mentor.name
                tvExamRank.text = "${mentor.examCategory} ${mentor.examYear} • Rank ${mentor.rank}"
                tvBio.text = mentor.bio
                tvRating.text = mentor.rating.toString()
                tvReviewCount.text = "(${mentor.reviewsCount} reviews)"
                tvSessionsCompleted.text = mentor.sessionsCompleted.toString()
                tvStudentsHelped.text = mentor.studentsHelped.toString()
                tvExperience.text = "${mentor.experienceYears}+"
                tvPrice.text = "₹${mentor.hourlyRate}/hr"

                // Verification badge
                ivVerified.visibility = if (mentor.isVerified) {
                    android.view.View.VISIBLE
                } else {
                    android.view.View.GONE
                }

                // Follow button state
                btnFollow.apply {
                    if (mentor.isFollowing) {
                        text = "Following"
                        setIconResource(R.drawable.ic_check)
                        setTextColor(context.getColor(R.color.success))
                        iconTint = context.getColorStateList(R.color.success)
                    } else {
                        text = "Follow"
                        setIconResource(R.drawable.ic_add)
                        setTextColor(context.getColor(R.color.primary))
                        iconTint = context.getColorStateList(R.color.primary)
                    }
                }

                // Expertise chips
                chipGroupExpertise.removeAllViews()
                mentor.expertise.take(3).forEach { expertise ->
                    val chip = Chip(binding.root.context).apply {
                        text = expertise
                        textSize = 10f
                        isClickable = false
                        isFocusable = false
                    }
                    chipGroupExpertise.addView(chip)
                }

                // Profile image (placeholder for now)
                // TODO: Load actual image using Glide or similar
                ivMentorPhoto.setImageResource(R.drawable.ic_person)

                // Click listeners
                root.setOnClickListener {
                    onMentorClick(mentor)
                }

                btnBookSession.setOnClickListener {
                    onBookSessionClick(mentor)
                }

                btnFollow.setOnClickListener {
                    onFollowClick(mentor)
                }
            }
        }
    }

    private class MentorDiffCallback : DiffUtil.ItemCallback<Mentor>() {
        override fun areItemsTheSame(oldItem: Mentor, newItem: Mentor): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Mentor, newItem: Mentor): Boolean {
            return oldItem == newItem
        }
    }
}