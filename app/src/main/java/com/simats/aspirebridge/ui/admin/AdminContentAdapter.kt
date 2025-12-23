package com.simats.aspirebridge.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simats.aspirebridge.R
import com.simats.aspirebridge.databinding.ItemAdminContentBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter for admin content management list
 */
class AdminContentAdapter(
    private val onViewClick: (AdminContentItem) -> Unit,
    private val onEditClick: (AdminContentItem) -> Unit,
    private val onDeleteClick: (AdminContentItem) -> Unit
) : ListAdapter<AdminContentItem, AdminContentAdapter.ContentViewHolder>(ContentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val binding = ItemAdminContentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ContentViewHolder(
        private val binding: ItemAdminContentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AdminContentItem) {
            binding.apply {
                textContentTitle.text = item.title
                textAuthorName.text = "by ${item.authorName}"
                textCreatedDate.text = formatDate(item.createdAt)
                textLikesCount.text = "${item.likesCount} likes"
                textStatus.text = item.status
                
                // Set content type icon
                val iconRes = when (item.type) {
                    "STORY" -> R.drawable.ic_trophy
                    "RESOURCE" -> R.drawable.ic_upload
                    else -> R.drawable.ic_work
                }
                imgContentIcon.setImageResource(iconRes)
                
                // Set status color
                val statusColor = when (item.status) {
                    "Active" -> R.color.success
                    "Reported" -> R.color.warning
                    "Deleted" -> R.color.error
                    else -> R.color.text_secondary
                }
                textStatus.setBackgroundColor(itemView.context.getColor(statusColor))
                
                // Click listeners
                btnViewContent.setOnClickListener { onViewClick(item) }
                btnEditContent.setOnClickListener { onEditClick(item) }
                btnDeleteContent.setOnClickListener { onDeleteClick(item) }
                
                root.setOnClickListener { onViewClick(item) }
            }
        }
        
        private fun formatDate(timestamp: Long): String {
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }

    private class ContentDiffCallback : DiffUtil.ItemCallback<AdminContentItem>() {
        override fun areItemsTheSame(oldItem: AdminContentItem, newItem: AdminContentItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AdminContentItem, newItem: AdminContentItem): Boolean {
            return oldItem == newItem
        }
    }
}

/**
 * Data class for admin content items
 */
data class AdminContentItem(
    val id: String,
    val title: String,
    val authorName: String,
    val type: String, // "STORY" or "RESOURCE"
    val status: String, // "Active", "Reported", "Deleted"
    val createdAt: Long,
    val likesCount: Int,
    val isReported: Boolean = false
)