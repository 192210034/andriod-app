package com.simats.aspirebridge.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simats.aspirebridge.R
import com.simats.aspirebridge.data.model.User
import com.simats.aspirebridge.data.model.UserType
import com.simats.aspirebridge.databinding.ItemAdminUserBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter for displaying users in admin user management
 */
class AdminUserAdapter(
    private val onViewProfileClick: (User) -> Unit,
    private val onSendMessageClick: (User) -> Unit,
    private val onBanUserClick: (User) -> Unit
) : ListAdapter<User, AdminUserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemAdminUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UserViewHolder(
        private val binding: ItemAdminUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.apply {
                // Basic user info
                textUserName.text = user.name
                textUserEmail.text = user.email
                textSessionsCount.text = user.totalSessions.toString()

                // User type badge
                textUserType.text = user.userType.name.lowercase().replaceFirstChar { 
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
                }
                
                // Set user type badge color
                when (user.userType) {
                    UserType.ASPIRANT -> {
                        textUserType.setBackgroundResource(R.drawable.job_count_background_green)
                    }
                    UserType.ACHIEVER -> {
                        textUserType.setBackgroundResource(R.drawable.job_count_background_purple)
                    }
                    UserType.ADMIN -> {
                        textUserType.setBackgroundResource(R.drawable.job_count_background_red)
                    }
                }

                // Show rating only for achievers
                if (user.userType == UserType.ACHIEVER && user.rating > 0) {
                    layoutRating.visibility = View.VISIBLE
                    textRating.text = String.format("%.1f", user.rating)
                } else {
                    layoutRating.visibility = View.GONE
                }

                // Format join date
                val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
                textJoinDate.text = dateFormat.format(Date(user.createdAt))

                // Set click listeners
                btnViewProfile.setOnClickListener {
                    onViewProfileClick(user)
                }

                btnSendMessage.setOnClickListener {
                    onSendMessageClick(user)
                }

                btnBanUser.setOnClickListener {
                    onBanUserClick(user)
                }

                // Load user avatar if available
                // TODO: Implement image loading with Glide/Coil
                // For now, using placeholder
                imageAvatar.setImageResource(R.drawable.placeholder_avatar)
            }
        }
    }

    private class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}