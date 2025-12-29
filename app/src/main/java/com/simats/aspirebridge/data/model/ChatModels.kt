package com.simats.aspirebridge.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatRoom(
    val id: String,
    val participants: List<String>, // User IDs
    val participantDetails: List<ChatParticipant> = emptyList(),
    val lastMessage: ChatMessage? = null,
    val unreadCount: Int = 0,
    val isGroup: Boolean = false,
    val groupName: String? = null,
    val groupIcon: String? = null,
    val createdAt: String,
    val updatedAt: String
) : Parcelable

@Parcelize
data class ChatParticipant(
    val userId: String,
    val name: String,
    val profilePicture: String = "",
    val isOnline: Boolean = false,
    val lastSeen: String? = null,
    val role: String = "member" // member, admin, mentor, student
) : Parcelable

@Parcelize
data class ChatMessage(
    val id: String,
    val roomId: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val type: MessageType = MessageType.TEXT,
    val attachments: List<MessageAttachment> = emptyList(),
    val replyTo: String? = null, // Message ID being replied to
    val isEdited: Boolean = false,
    val isDeleted: Boolean = false,
    val readBy: List<String> = emptyList(), // User IDs who have read this message
    val reactions: List<MessageReaction> = emptyList(),
    val timestamp: String,
    val editedAt: String? = null
) : Parcelable

enum class MessageType {
    TEXT,
    IMAGE,
    FILE,
    VOICE,
    VIDEO,
    LOCATION,
    SYSTEM // For system messages like "User joined", "Session booked", etc.
}

@Parcelize
data class MessageAttachment(
    val id: String,
    val type: AttachmentType,
    val url: String,
    val fileName: String,
    val fileSize: Long,
    val mimeType: String,
    val thumbnailUrl: String? = null
) : Parcelable

enum class AttachmentType {
    IMAGE,
    DOCUMENT,
    AUDIO,
    VIDEO
}

@Parcelize
data class MessageReaction(
    val userId: String,
    val userName: String,
    val emoji: String,
    val timestamp: String
) : Parcelable

@Parcelize
data class TypingIndicator(
    val roomId: String,
    val userId: String,
    val userName: String,
    val isTyping: Boolean,
    val timestamp: String
) : Parcelable

// Chat-related API models
data class SendMessageRequest(
    val roomId: String,
    val content: String,
    val type: MessageType = MessageType.TEXT,
    val replyTo: String? = null,
    val attachments: List<MessageAttachment> = emptyList()
)

data class CreateChatRoomRequest(
    val participantIds: List<String>,
    val isGroup: Boolean = false,
    val groupName: String? = null
)

data class ChatRoomResponse(
    val room: ChatRoom,
    val messages: List<ChatMessage> = emptyList()
)

data class MessagesResponse(
    val messages: List<ChatMessage>,
    val hasMore: Boolean,
    val nextPage: Int?
)

// Socket events for real-time chat
sealed class ChatSocketEvent {
    data class MessageReceived(val message: ChatMessage) : ChatSocketEvent()
    data class MessageUpdated(val message: ChatMessage) : ChatSocketEvent()
    data class MessageDeleted(val messageId: String, val roomId: String) : ChatSocketEvent()
    data class UserTyping(val indicator: TypingIndicator) : ChatSocketEvent()
    data class UserOnlineStatus(val userId: String, val isOnline: Boolean, val lastSeen: String?) : ChatSocketEvent()
    data class MessageRead(val messageId: String, val userId: String, val roomId: String) : ChatSocketEvent()
    data class ReactionAdded(val messageId: String, val reaction: MessageReaction) : ChatSocketEvent()
    data class ReactionRemoved(val messageId: String, val userId: String, val emoji: String) : ChatSocketEvent()
}

// Chat preferences and settings
@Parcelize
data class ChatSettings(
    val userId: String,
    val notificationsEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val showReadReceipts: Boolean = true,
    val showOnlineStatus: Boolean = true,
    val autoDownloadImages: Boolean = true,
    val autoDownloadVideos: Boolean = false,
    val autoDownloadDocuments: Boolean = false
) : Parcelable