package com.simats.aspirebridge.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Session and booking data models for the mentorship platform
 */

@Parcelize
data class Session(
    val id: String,
    val mentorId: String,
    val menteeId: String,
    val title: String,
    val description: String,
    val type: SessionType,
    val status: SessionStatus,
    val scheduledAt: Long,
    val duration: Int, // in minutes
    val price: Float,
    val meetingLink: String? = null,
    val notes: String = "",
    val rating: Float? = null,
    val feedback: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val cancelledAt: Long? = null,
    val cancellationReason: String? = null
) : Parcelable

enum class SessionStatus {
    REQUESTED,
    CONFIRMED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    NO_SHOW
}

@Parcelize
data class BookingRequest(
    val id: String,
    val sessionId: String,
    val mentorId: String,
    val menteeId: String,
    val requestedAt: Long,
    val preferredDate: Long,
    val preferredTime: String,
    val message: String = "",
    val status: BookingStatus,
    val respondedAt: Long? = null,
    val responseMessage: String = ""
) : Parcelable

enum class BookingStatus {
    PENDING,
    ACCEPTED,
    DECLINED,
    EXPIRED
}

@Parcelize
data class AvailabilitySlot(
    val id: String,
    val mentorId: String,
    val dayOfWeek: Int, // 1-7 (Monday-Sunday)
    val startTime: String, // "09:00"
    val endTime: String, // "17:00"
    val isAvailable: Boolean = true,
    val isRecurring: Boolean = true,
    val specificDate: Long? = null, // For one-time slots
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
data class SessionFeedback(
    val id: String,
    val sessionId: String,
    val fromUserId: String,
    val toUserId: String,
    val rating: Float,
    val feedback: String,
    val isPublic: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
data class SessionReport(
    val id: String,
    val sessionId: String,
    val reportedBy: String,
    val reason: String,
    val description: String,
    val status: ReportStatus = ReportStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val resolvedAt: Long? = null
) : Parcelable

enum class ReportStatus {
    PENDING,
    UNDER_REVIEW,
    RESOLVED,
    DISMISSED
}