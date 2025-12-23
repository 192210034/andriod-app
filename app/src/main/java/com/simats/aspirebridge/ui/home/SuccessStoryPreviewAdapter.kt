package com.simats.aspirebridge.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simats.aspirebridge.R
import com.simats.aspirebridge.data.model.SuccessStory
import com.simats.aspirebridge.databinding.ItemSuccessStoryPreviewBinding

/**
 * Adapter for displaying success story previews in home screen
 */
class SuccessStoryPreviewAdapter(
    private val onStoryClick: (SuccessStory) -> Unit
) : ListAdapter<SuccessStory, SuccessStoryPreviewAdapter.PreviewViewHolder>(StoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val binding = ItemSuccessStoryPreviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PreviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PreviewViewHolder(
        private val binding: ItemSuccessStoryPreviewBinding
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
                
                // Rank
                val rankText = story.rank?.let { "Rank $it" } ?: "Selected"
                textRank.text = rankText
                
                // Story content
                textStoryTitle.text = story.title
                textStoryPreview.text = story.content
                
                // Engagement counts
                textLikesCount.text = formatCount(story.likes)
                textCommentsCount.text = formatCount(story.comments)
                
                // Click listeners
                root.setOnClickListener { onStoryClick(story) }
                btnReadMore.setOnClickListener { onStoryClick(story) }
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