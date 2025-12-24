package com.simats.aspirebridge.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.simats.aspirebridge.R

@Parcelize
data class ExamCategory(
    val id: String,
    val name: String,
    val fullName: String,
    val description: String,
    val iconRes: Int,
    val colorRes: Int,
    val subcategories: List<ExamSubcategory> = emptyList(),
    val isActive: Boolean = true,
    val sortOrder: Int = 0
) : Parcelable

@Parcelize
data class ExamSubcategory(
    val id: String,
    val name: String,
    val fullName: String,
    val parentCategoryId: String,
    val description: String,
    val eligibility: String,
    val examPattern: String,
    val iconRes: Int = 0,
    val isActive: Boolean = true,
    val sortOrder: Int = 0,
    val examFrequency: String = "", // e.g., "Annual", "Bi-annual"
    val applicationFee: String = "",
    val officialWebsite: String = ""
) : Parcelable

@Parcelize
data class ExamAchievement(
    val id: String,
    val examCategory: ExamCategory,
    val examSubcategory: ExamSubcategory,
    val rank: Int?,
    val totalCandidates: Int?,
    val year: Int,
    val attempts: Int,
    val preparationDuration: String, // e.g., "18 months"
    val verificationStatus: VerificationStatus,
    val verificationDocuments: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val marks: Float? = null,
    val totalMarks: Float? = null,
    val percentage: Float? = null,
    val cutoffMarks: Float? = null
) : Parcelable

enum class VerificationStatus {
    PENDING,
    VERIFIED,
    REJECTED,
    NOT_SUBMITTED
}

@Parcelize
data class AchieverProfile(
    val userId: String,
    val examAchievements: List<ExamAchievement>,
    val isVerified: Boolean,
    val specializations: List<String>,
    val mentorRating: Float = 0f,
    val totalSessions: Int = 0,
    val successStoriesCount: Int = 0,
    val resourcesSharedCount: Int = 0,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val totalLikes: Int = 0,
    val joinedAt: Long = System.currentTimeMillis(),
    val bio: String = "",
    val currentRole: String = "",
    val location: String = "",
    val languages: List<String> = emptyList(),
    val availability: AvailabilityStatus = AvailabilityStatus.AVAILABLE
) : Parcelable

// AvailabilityStatus is defined in User.kt

@Parcelize
data class MentorProfile(
    val userId: String,
    val achieverProfile: AchieverProfile,
    val hourlyRate: Float? = null,
    val sessionTypes: List<SessionType>,
    val availableSlots: List<TimeSlot>,
    val preferredLanguages: List<String>,
    val mentoringSince: Long,
    val totalEarnings: Float = 0f,
    val responseTime: String = "Within 24 hours",
    val isTopMentor: Boolean = false,
    val badges: List<MentorBadge> = emptyList()
) : Parcelable

enum class SessionType {
    ONE_ON_ONE,
    GROUP_SESSION,
    MOCK_INTERVIEW,
    DOUBT_CLEARING,
    STRATEGY_PLANNING,
    DOCUMENT_REVIEW
}

@Parcelize
data class TimeSlot(
    val dayOfWeek: Int, // 1-7 (Monday-Sunday)
    val startTime: String, // "09:00"
    val endTime: String, // "17:00"
    val isAvailable: Boolean = true
) : Parcelable

@Parcelize
data class MentorBadge(
    val id: String,
    val name: String,
    val description: String,
    val iconRes: Int,
    val earnedAt: Long
) : Parcelable