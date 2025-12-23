package com.simats.aspirebridge.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SuccessStory(
    val id: String,
    val authorId: String,
    val authorName: String,
    val authorAvatar: String?,
    val title: String,
    val content: String,
    val examCategory: ExamCategory,
    val examSubcategory: ExamSubcategory,
    val rank: Int?,
    val year: Int,
    val attempts: Int,
    val preparationDuration: String,
    val keyTips: List<String>,
    val resources: List<String>,
    val createdAt: Long,
    val updatedAt: Long,
    val likes: Int = 0,
    val comments: Int = 0,
    val shares: Int = 0,
    val views: Int = 0,
    val isBookmarked: Boolean = false,
    val isLiked: Boolean = false,
    val tags: List<String> = emptyList(),
    val isVerified: Boolean = false
) : Parcelable

@Parcelize
data class SuccessStoryComment(
    val id: String,
    val storyId: String,
    val userId: String,
    val userName: String,
    val userAvatar: String?,
    val content: String,
    val createdAt: Long,
    val likes: Int = 0,
    val isLiked: Boolean = false,
    val replies: List<SuccessStoryReply> = emptyList()
) : Parcelable

@Parcelize
data class SuccessStoryReply(
    val id: String,
    val commentId: String,
    val userId: String,
    val userName: String,
    val userAvatar: String?,
    val content: String,
    val createdAt: Long,
    val likes: Int = 0,
    val isLiked: Boolean = false
) : Parcelable

@Parcelize
data class SuccessStoryEngagement(
    val storyId: String,
    val userId: String,
    val type: EngagementType,
    val timestamp: Long
) : Parcelable

enum class EngagementType {
    LIKE, UNLIKE, SHARE, BOOKMARK, UNBOOKMARK, VIEW, COMMENT
}

@Parcelize
data class SuccessStoryFilter(
    val examCategory: ExamCategory? = null,
    val examSubcategory: ExamSubcategory? = null,
    val year: Int? = null,
    val minRank: Int? = null,
    val maxRank: Int? = null,
    val attempts: Int? = null,
    val sortBy: SortBy = SortBy.RECENT,
    val searchQuery: String? = null
) : Parcelable

enum class SortBy {
    RECENT, POPULAR, MOST_LIKED, MOST_VIEWED, RANK_ASCENDING, RANK_DESCENDING
}