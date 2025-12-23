package com.simats.aspirebridge.ui.stories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.simats.aspirebridge.R
import com.simats.aspirebridge.data.model.SuccessStory
import com.simats.aspirebridge.databinding.ItemSuccessStoryBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter for displaying success stories in RecyclerView
 */
class SuccessStoriesAdapter(
    private val onStoryClick: (SuccessStory) -> Unit,
    private val onLikeClick: (SuccessStory) -> Unit,
    private val onCommentClick: (SuccessStory) -> Unit,
    private val onShareClick: (SuccessStory) -> Unit,
    private val onBookmarkClick: (SuccessStory) -> Unit,
    private val onAuthorClick: (String) -> Unit
) : ListAdapter<SuccessStory, SuccessStoriesAdapter.StoryViewHolder>(StoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemSuccessStoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StoryViewHolder(
        private val binding: ItemSuccessStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: SuccessStory) {
            with(binding) {
                // Author info
                textAuthorName.text = story.authorName
                
                // Load author avatar (placeholder for now)
                imgAuthorAvatar.setImageResource(R.drawable.placeholder_avatar)
                
                // Verification badge
                imgVerifiedBadge.visibility = if (story.isVerified) 
                    android.view.View.VISIBLE else android.view.View.GONE
                
                // Exam category badge
                textExamBadge.text = story.examCategory.name
                textExamBadge.setBackgroundColor(
                    ContextCompat.getColor(itemView.context, story.examCategory.colorRes)
                )
                
                // Rank and year
                val rankText = story.rank?.let { "Rank $it" } ?: "Selected"
                textRankYear.text = "$rankText • ${story.year}"
                
                // Attempts
                val attemptText = when (story.attempts) {
                    1 -> "1st Attempt"
                    2 -> "2nd Attempt"
                    3 -> "3rd Attempt"
                    else -> "${story.attempts}th Attempt"
                }
                textAttempts.text = attemptText
                
                // Story content
                textStoryTitle.text = story.title
                textStoryPreview.text = story.content
                
                // Tags
                setupTags(story.tags)
                
                // Engagement counts
                textLikesCount.text = formatCount(story.likes)
                textCommentsCount.text = formatCount(story.comments)
                textSharesCount.text = formatCount(story.shares)
                
                // Like state
                if (story.isLiked) {
                    imgLike.setImageResource(R.drawable.ic_star) // Using star as filled heart
                    imgLike.setColorFilter(ContextCompat.getColor(itemView.context, R.color.error))
                } else {
                    imgLike.setImageResource(R.drawable.ic_favorite_border)
                    imgLike.setColorFilter(ContextCompat.getColor(itemView.context, R.color.text_secondary))
                }
                
                // Bookmark state
                if (story.isBookmarked) {
                    btnBookmark.setImageResource(R.drawable.ic_star) // Using star as filled bookmark
                    btnBookmark.setColorFilter(ContextCompat.getColor(itemView.context, R.color.warning))
                } else {
                    btnBookmark.setImageResource(R.drawable.ic_bookmark_border)
                    btnBookmark.setColorFilter(ContextCompat.getColor(itemView.context, R.color.text_secondary))
                }
                
                // Click listeners
                root.setOnClickListener { onStoryClick(story) }
                btnReadMore.setOnClickListener { onStoryClick(story) }
                
                layoutLike.setOnClickListener { onLikeClick(story) }
                layoutComment.setOnClickListener { onCommentClick(story) }
                layoutShare.setOnClickListener { onShareClick(story) }
                btnBookmark.setOnClickListener { onBookmarkClick(story) }
                
                imgAuthorAvatar.setOnClickListener { onAuthorClick(story.authorId) }
                textAuthorName.setOnClickListener { onAuthorClick(story.authorId) }
                
                btnMoreOptions.setOnClickListener {
                    // TODO: Show options menu
                }
            }
        }
        
        private fun setupTags(tags: List<String>) {
            binding.layoutTags.removeAllViews()
            
            tags.take(3).forEach { tag -> // Show max 3 tags
                val chip = Chip(itemView.context).apply {
                    text = tag
                    isClickable = false
                    isCheckable = false
                    setChipBackgroundColorResource(R.color.surface_variant)
                    setTextColor(ContextCompat.getColor(itemView.context, R.color.text_secondary))
                    textSize = 10f
                    
                    val layoutParams = FlexboxLayout.LayoutParams(
                        FlexboxLayout.LayoutParams.WRAP_CONTENT,
                        FlexboxLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 0, 8, 8)
                    }
                    this.layoutParams = layoutParams
                }
                binding.layoutTags.addView(chip)
            }
        }
        
        private fun formatCount(count: Int): String {
            return when {
                count >= 1000000 -> String.format("%.1fM", count / 1000000.0)
                count >= 1000 -> String.format("%.1fK", count / 1000.0)
                else -> count.toString()
            }
        }
    }

    private class StoryDiffCallback : DiffUtil.ItemCallback<SuccessStory>() {
        override fun areItemsTheSame(oldItem: SuccessStory, newItem: SuccessStory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SuccessStory, newItem: SuccessStory): Boolean {
            return oldItem == newItem
        }
    }
}