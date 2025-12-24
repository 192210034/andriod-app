package com.simats.aspirebridge.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * User data model for the mentorship platform
 */
@Parcelize
data class User(
    val id: String,
    val name: String,
    val email: String,
    val profilePicture: String? = null,
    val bio: String = "",
    val skills: List<String> = emptyList(),
    val userType: UserType = UserType.ASPIRANT,
    val availability: AvailabilityStatus = AvailabilityStatus.AVAILABLE,
    val rating: Float = 0f,
    val totalSessions: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable

/**
 * User type enumeration
 */
enum class UserType {
    ASPIRANT, ACHIEVER, ADMIN
}

/**
 * Availability status enumeration
 */
enum class AvailabilityStatus {
    AVAILABLE, BUSY, OFFLINE
}