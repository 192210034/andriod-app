package com.simats.aspirebridge.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Authentication models
@Parcelize
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val userType: UserType
) : Parcelable

@Parcelize
data class LoginRequest(
    val email: String,
    val password: String
) : Parcelable

@Parcelize
data class ForgotPasswordRequest(
    val email: String
) : Parcelable

@Parcelize
data class AuthResponse(
    val success: Boolean,
    val token: String?,
    val user: User?,
    val message: String?
) : Parcelable

@Parcelize
data class MessageResponse(
    val success: Boolean,
    val message: String
) : Parcelable

// Search models
@Parcelize
data class SearchResponse(
    val success: Boolean,
    val mentors: List<Mentor>,
    val totalCount: Int,
    val page: Int,
    val hasMore: Boolean
) : Parcelable

@Parcelize
data class Mentor(
    val id: String,
    val name: String,
    val email: String,
    val profilePicture: String?,
    val bio: String,
    val skills: List<String>,
    val rating: Float,
    val totalSessions: Int,
    val availability: AvailabilityStatus,
    val location: String?,
    val industry: String?
) : Parcelable

@Parcelize
data class MentorDetails(
    val mentor: Mentor,
    val recentFeedback: List<Feedback>,
    val availableSlots: List<String>
) : Parcelable

// Request models
@Parcelize
data class MentorshipRequest(
    val id: String? = null,
    val menteeId: String,
    val mentorId: String,
    val message: String,
    val status: RequestStatus = RequestStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val respondedAt: Long? = null
) : Parcelable

enum class RequestStatus {
    PENDING, ACCEPTED, DECLINED
}

// Session models - Using SessionModels.kt for comprehensive session management
// Session and SessionStatus are defined in SessionModels.kt

// Message models
@Parcelize
data class Message(
    val id: String? = null,
    val senderId: String,
    val receiverId: String,
    val chatId: String,
    val content: String,
    val messageType: MessageType = MessageType.TEXT,
    val timestamp: Long = System.currentTimeMillis(),
    val delivered: Boolean = false,
    val read: Boolean = false
) : Parcelable

enum class MessageType {
    TEXT, FILE, LINK
}

// Feedback models
@Parcelize
data class Feedback(
    val id: String? = null,
    val sessionId: String,
    val fromUserId: String,
    val toUserId: String,
    val rating: Int, // 1-5
    val comment: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable