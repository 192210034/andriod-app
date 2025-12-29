package com.simats.aspirebridge.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mentor(
    val id: String,
    val name: String,
    val email: String,
    val photoUrl: String = "",
    val bio: String,
    val examCategory: String, // UPSC, SSC, Railways, etc.
    val examSubCategory: String = "",
    val examCleared: String,
    val rank: String,
    val examYear: String,
    val hourlyRate: Int,
    val experienceYears: Int,
    val rating: Double,
    val reviewsCount: Int,
    val sessionsCompleted: Int,
    val studentsHelped: Int,
    val expertise: List<String>,
    val isVerified: Boolean = false,
    val isApproved: Boolean = false,
    val isFollowing: Boolean = false,
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList(),
    val availability: List<AvailabilitySlot> = emptyList(),
    val createdAt: String,
    val updatedAt: String
) : Parcelable

@Parcelize
data class AvailabilitySlot(
    val id: String,
    val mentorId: String,
    val date: String, // YYYY-MM-DD format
    val startTime: String, // HH:MM format
    val endTime: String, // HH:MM format
    val isBooked: Boolean = false,
    val bookingId: String? = null
) : Parcelable

@Parcelize
data class MentorReview(
    val id: String,
    val mentorId: String,
    val studentId: String,
    val studentName: String,
    val rating: Int, // 1-5
    val review: String,
    val sessionId: String,
    val createdAt: String
) : Parcelable

@Parcelize
data class MentorStats(
    val totalSessions: Int,
    val totalStudents: Int,
    val averageRating: Double,
    val totalReviews: Int,
    val totalEarnings: Double,
    val successRate: Double, // Percentage of successful sessions
    val responseTime: String, // Average response time
    val completionRate: Double // Percentage of completed sessions
) : Parcelable

@Parcelize
data class BookingRequest(
    val id: String,
    val mentorId: String,
    val studentId: String,
    val studentName: String,
    val studentEmail: String,
    val date: String,
    val time: String,
    val duration: Int, // in minutes
    val message: String,
    val amount: Int,
    val status: BookingStatus,
    val paymentId: String? = null,
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING,
    val meetingLink: String? = null,
    val createdAt: String,
    val updatedAt: String
) : Parcelable

enum class BookingStatus {
    PENDING,
    CONFIRMED,
    REJECTED,
    COMPLETED,
    CANCELLED
}

enum class PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED,
    REFUNDED
}

@Parcelize
data class Session(
    val id: String,
    val bookingId: String,
    val mentorId: String,
    val studentId: String,
    val roomId: String,
    val status: SessionStatus,
    val scheduledStartTime: String,
    val scheduledEndTime: String,
    val actualStartTime: String? = null,
    val actualEndTime: String? = null,
    val duration: Int, // in minutes
    val meetingLink: String? = null,
    val notes: String = "",
    val feedback: SessionFeedback? = null,
    val createdAt: String,
    val updatedAt: String
) : Parcelable

enum class SessionStatus {
    SCHEDULED,
    ONGOING,
    COMPLETED,
    CANCELLED,
    NO_SHOW
}

@Parcelize
data class SessionFeedback(
    val studentRating: Int? = null,
    val studentReview: String = "",
    val mentorRating: Int? = null,
    val mentorReview: String = "",
    val submittedAt: String? = null
) : Parcelable

// Filter and search models
data class MentorFilter(
    val examCategories: List<String> = emptyList(),
    val minRating: Double? = null,
    val maxRating: Double? = null,
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
    val minExperience: Int? = null,
    val maxExperience: Int? = null,
    val expertise: List<String> = emptyList(),
    val availability: String? = null, // "today", "this_week", "this_month"
    val isVerified: Boolean? = null
)

data class MentorSearchResult(
    val mentors: List<Mentor>,
    val totalCount: Int,
    val hasMore: Boolean,
    val nextPage: Int?
)