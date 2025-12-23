package com.simats.aspirebridge.ui.resources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.simats.aspirebridge.R
import com.simats.aspirebridge.data.model.Resource
import com.simats.aspirebridge.data.model.ResourceType
import com.simats.aspirebridge.databinding.ItemResourceBinding

/**
 * Adapter for displaying resources in RecyclerView
 */
class ResourcesAdapter(
    private val onResourceClick: (Resource) -> Unit,
    private val onDownloadClick: (Resource) -> Unit,
    private val onBookmarkClick: (Resource) -> Unit,
    private val onUploaderClick: (String) -> Unit
) : ListAdapter<Resource, ResourcesAdapter.ResourceViewHolder>(ResourceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val binding = ItemResourceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ResourceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ResourceViewHolder(
        private val binding: ItemResourceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(resource: Resource) {
            with(binding) {
                // Resource type icon
                val iconRes = when (resource.type) {
                    ResourceType.PDF -> R.drawable.ic_upload
                    ResourceType.VIDEO -> R.drawable.ic_upload
                    ResourceType.NOTES -> R.drawable.ic_edit
                    ResourceType.PRACTICE_TEST -> R.drawable.ic_calendar_view
                    ResourceType.STRATEGY -> R.drawable.ic_trophy
                    ResourceType.TIMETABLE -> R.drawable.ic_calendar
                    else -> R.drawable.ic_upload
                }
                imgResourceType.setImageResource(iconRes)
                
                // Resource info
                textResourceTitle.text = resource.title
                textResourceDescription.text = resource.description
                textUploaderName.text = "by ${resource.uploaderName}"
                
                // Exam category badge
                textExamBadge.text = resource.examCategory.name
                textExamBadge.setBackgroundColor(
                    ContextCompat.getColor(itemView.context, resource.examCategory.colorRes)
                )
                
                // Verification badge
                imgVerifiedBadge.visibility = if (resource.isVerified) 
                    android.view.View.VISIBLE else android.view.View.GONE
                
                // File size
                resource.fileSize?.let { size ->
                    textFileSize.text = formatFileSize(size)
                } ?: run {
                    textFileSize.text = resource.type.name
                }
                
                // Language and difficulty
                textLanguage.text = resource.language
                textDifficulty.text = resource.difficulty.name.lowercase().replaceFirstChar { it.uppercase() }
                
                // Rating
                textRating.text = String.format("%.1f", resource.rating)
                textRatingCount.text = "(${resource.ratingCount})"
                
                // Engagement counts
                textDownloadsCount.text = formatCount(resource.downloads)
                textViewsCount.text = "${formatCount(resource.views)} views"
                
                // Bookmark state
                if (resource.isBookmarked) {
                    btnBookmark.setImageResource(R.drawable.ic_star) // Using star as filled bookmark
                    btnBookmark.setColorFilter(ContextCompat.getColor(itemView.context, R.color.warning))
                } else {
                    btnBookmark.setImageResource(R.drawable.ic_bookmark_border)
                    btnBookmark.setColorFilter(ContextCompat.getColor(itemView.context, R.color.text_secondary))
                }
                
                // Tags
                setupTags(resource.tags)
                
                // Click listeners
                root.setOnClickListener { onResourceClick(resource) }
                btnDownload.setOnClickListener { onDownloadClick(resource) }
                btnBookmark.setOnClickListener { onBookmarkClick(resource) }
                textUploaderName.setOnClickListener { onUploaderClick(resource.uploaderId) }
                
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
        
        private fun formatFileSize(bytes: Long): String {
            val kb = bytes / 1024.0
            val mb = kb / 1024.0
            val gb = mb / 1024.0
            
            return when {
                gb >= 1 -> String.format("%.1f GB", gb)
                mb >= 1 -> String.format("%.1f MB", mb)
                kb >= 1 -> String.format("%.1f KB", kb)
                else -> "$bytes B"
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

    private class ResourceDiffCallback : DiffUtil.ItemCallback<Resource>() {
        override fun areItemsTheSame(oldItem: Resource, newItem: Resource): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Resource, newItem: Resource): Boolean {
            return oldItem == newItem
        }
    }
}