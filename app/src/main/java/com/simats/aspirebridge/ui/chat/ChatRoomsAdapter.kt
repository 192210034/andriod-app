package com.simats.aspirebridge.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simats.aspirebridge.R
import com.simats.aspirebridge.data.model.ChatRoom
import com.simats.aspirebridge.databinding.ItemChatRoomBinding
import java.text.SimpleDateFormat
import java.util.*

class ChatRoomsAdapter(
    private val onChatRoomClick: (ChatRoom) -> Unit
) : ListAdapter<ChatRoom, ChatRoomsAdapter.ChatRoomViewHolder>(ChatRoomDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val binding = ItemChatRoomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChatRoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChatRoomViewHolder(
        private val binding: ItemChatRoomBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chatRoom: ChatRoom) {
            binding.apply {
                // Set chat name
                if (chatRoom.isGroup) {
                    tvChatName.text = chatRoom.groupName ?: "Group Chat"
                    ivChatAvatar.setImageResource(R.drawable.ic_group)
                } else {
                    val otherParticipant = chatRoom.participantDetails.firstOrNull { it.userId != "current_user" }
                    tvChatName.text = otherParticipant?.name ?: "Unknown"
                    ivChatAvatar.setImageResource(R.drawable.ic_person)
                }

                // Set last message
                chatRoom.lastMessage?.let { message ->
                    tvLastMessage.text = if (message.senderId == "current_user") {
                        "You: ${message.content}"
                    } else {
                        message.content
                    }
                    tvLastMessage.visibility = View.VISIBLE
                } ?: run {
                    tvLastMessage.visibility = View.GONE
                }

                // Set timestamp
                chatRoom.lastMessage?.let { message ->
                    tvTimestamp.text = formatTimestamp(message.timestamp)
                    tvTimestamp.visibility = View.VISIBLE
                } ?: run {
                    tvTimestamp.visibility = View.GONE
                }

                // Set unread count
                if (chatRoom.unreadCount > 0) {
                    tvUnreadCount.text = if (chatRoom.unreadCount > 99) "99+" else chatRoom.unreadCount.toString()
                    tvUnreadCount.visibility = View.VISIBLE
                } else {
                    tvUnreadCount.visibility = View.GONE
                }

                // Set online status for individual chats
                if (!chatRoom.isGroup) {
                    val otherParticipant = chatRoom.participantDetails.firstOrNull { it.userId != "current_user" }
                    if (otherParticipant?.isOnline == true) {
                        ivOnlineStatus.visibility = View.VISIBLE
                    } else {
                        ivOnlineStatus.visibility = View.GONE
                    }
                } else {
                    ivOnlineStatus.visibility = View.GONE
                }

                // Handle click
                root.setOnClickListener {
                    onChatRoomClick(chatRoom)
                }
            }
        }

        private fun formatTimestamp(timestamp: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val date = inputFormat.parse(timestamp) ?: return ""
                
                val now = Calendar.getInstance()
                val messageTime = Calendar.getInstance().apply { time = date }
                
                when {
                    // Same day
                    now.get(Calendar.DAY_OF_YEAR) == messageTime.get(Calendar.DAY_OF_YEAR) &&
                    now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR) -> {
                        SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
                    }
                    // Yesterday
                    now.get(Calendar.DAY_OF_YEAR) - messageTime.get(Calendar.DAY_OF_YEAR) == 1 &&
                    now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR) -> {
                        "Yesterday"
                    }
                    // This week
                    now.get(Calendar.WEEK_OF_YEAR) == messageTime.get(Calendar.WEEK_OF_YEAR) &&
                    now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR) -> {
                        SimpleDateFormat("EEE", Locale.getDefault()).format(date)
                    }
                    // This year
                    now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR) -> {
                        SimpleDateFormat("MMM dd", Locale.getDefault()).format(date)
                    }
                    // Different year
                    else -> {
                        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
                    }
                }
            } catch (e: Exception) {
                ""
            }
        }
    }

    private class ChatRoomDiffCallback : DiffUtil.ItemCallback<ChatRoom>() {
        override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem == newItem
        }
    }
}