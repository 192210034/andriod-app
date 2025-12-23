package com.simats.aspirebridge.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Resource(
    val id: String,
    val title: String,
    val description: String,
    val type: ResourceType,
    val url: String,
    val thumbnailUrl: String?,
    val fileSize: Long?,
    val fileName: String?,
    val uploaderId: String,
    val uploaderName: String,
    val uploaderAvatar: String?,
    val examCategory: ExamCategory,
    val examSubcategory: ExamSubcategory,
    val tags: List<String>,
    val downloads: Int = 0,
    val views: Int = 0,
    val rating: Float = 0f,
    val ratingCount: Int = 0,
    val createdAt: Long,
    val updatedAt: Long,
    val isBookmarked: Boolean = false,
    val isVerified: Boolean = false,
    val language: String = "English",
    val difficulty: DifficultyLevel = DifficultyLevel.INTERMEDIATE
) : Parcelable

enum class ResourceType {
    PDF, VIDEO, NOTES, PRACTICE_TEST, STRATEGY, TIMETABLE, LINK, AUDIO, IMAGE, PRESENTATION
}

enum class DifficultyLevel {
    BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
}

@Parcelize
data class ResourceRating(
    val id: String,
    val resourceId: String,
    val userId: String,
    val userName: String,
    val rating: Float,
    val review: String?,
    val createdAt: Long,
    val isHelpful: Boolean = false,
    val helpfulCount: Int = 0
) : Parcelable

@Parcelize
data class ResourceCollection(
    val id: String,
    val name: String,
    val description: String?,
    val userId: String,
    val resourceIds: List<String>,
    val isPublic: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long,
    val followers: Int = 0,
    val isFollowing: Boolean = false
) : Parcelable

@Parcelize
data class ResourceFilter(
    val examCategory: ExamCategory? = null,
    val examSubcategory: ExamSubcategory? = null,
    val resourceType: ResourceType? = null,
    val difficulty: DifficultyLevel? = null,
    val language: String? = null,
    val minRating: Float? = null,
    val tags: List<String> = emptyList(),
    val sortBy: ResourceSortBy = ResourceSortBy.RECENT,
    val searchQuery: String? = null
) : Parcelable

enum class ResourceSortBy {
    RECENT, POPULAR, MOST_DOWNLOADED, HIGHEST_RATED, MOST_VIEWED, ALPHABETICAL
}

@Parcelize
data class ResourceEngagement(
    val resourceId: String,
    val userId: String,
    val type: ResourceEngagementType,
    val timestamp: Long
) : Parcelable

enum class ResourceEngagementType {
    VIEW, DOWNLOAD, BOOKMARK, UNBOOKMARK, RATE, SHARE
}