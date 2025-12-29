package com.simats.aspirebridge.ui.mentors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simats.aspirebridge.data.model.MentorReview
import com.simats.aspirebridge.databinding.ItemMentorReviewBinding
import java.text.SimpleDateFormat
import java.util.*

class ReviewsAdapter : ListAdapter<MentorReview, ReviewsAdapter.ReviewViewHolder>(ReviewDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemMentorReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ReviewViewHolder(
        private val binding: ItemMentorReviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(review: MentorReview) {
            binding.apply {
                tvStudentName.text = review.studentName
                tvReviewText.text = review.review
                ratingBar.rating = review.rating.toFloat()
                tvRating.text = review.rating.toString()

                // Format date
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val displayDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                
                try {
                    val date = dateFormat.parse(review.createdAt)
                    tvReviewDate.text = displayDateFormat.format(date ?: Date())
                } catch (e: Exception) {
                    tvReviewDate.text = review.createdAt
                }
            }
        }
    }

    private class ReviewDiffCallback : DiffUtil.ItemCallback<MentorReview>() {
        override fun areItemsTheSame(oldItem: MentorReview, newItem: MentorReview): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MentorReview, newItem: MentorReview): Boolean {
            return oldItem == newItem
        }
    }
}