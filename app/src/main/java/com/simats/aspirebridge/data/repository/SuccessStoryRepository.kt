package com.simats.aspirebridge.data.repository

import com.simats.aspirebridge.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SuccessStoryRepository @Inject constructor(
    private val examRepository: ExamRepository
) {

    fun getSuccessStories(filter: SuccessStoryFilter? = null): Flow<List<SuccessStory>> = flow {
        val stories = getSampleSuccessStories()
        val filteredStories = applyFilter(stories, filter)
        emit(filteredStories)
    }

    fun getSuccessStoryById(id: String): Flow<SuccessStory?> = flow {
        val story = getSampleSuccessStories().find { it.id == id }
        emit(story)
    }

    fun getSuccessStoriesByCategory(categoryId: String): Flow<List<SuccessStory>> = flow {
        val stories = getSampleSuccessStories().filter { it.examCategory.id == categoryId }
        emit(stories)
    }

    fun getSuccessStoriesBySubcategory(subcategoryId: String): Flow<List<SuccessStory>> = flow {
        val stories = getSampleSuccessStories().filter { it.examSubcategory.id == subcategoryId }
        emit(stories)
    }

    fun getRecentSuccessStories(limit: Int = 5): Flow<List<SuccessStory>> = flow {
        val stories = getSampleSuccessStories()
            .sortedByDescending { it.createdAt }
            .take(limit)
        emit(stories)
    }

    fun getPopularSuccessStories(limit: Int = 10): Flow<List<SuccessStory>> = flow {
        val stories = getSampleSuccessStories()
            .sortedByDescending { it.likes + it.views }
            .take(limit)
        emit(stories)
    }

    fun searchSuccessStories(query: String): Flow<List<SuccessStory>> = flow {
        val stories = getSampleSuccessStories().filter { story ->
            story.title.contains(query, ignoreCase = true) ||
            story.content.contains(query, ignoreCase = true) ||
            story.authorName.contains(query, ignoreCase = true) ||
            story.keyTips.any { it.contains(query, ignoreCase = true) }
        }
        emit(stories)
    }

    fun likeStory(storyId: String): Flow<Boolean> = flow {
        // TODO: Implement API call to like story
        emit(true)
    }

    fun bookmarkStory(storyId: String): Flow<Boolean> = flow {
        // TODO: Implement API call to bookmark story
        emit(true)
    }

    fun shareStory(storyId: String): Flow<Boolean> = flow {
        // TODO: Implement API call to track story share
        emit(true)
    }

    fun getStoryComments(storyId: String): Flow<List<SuccessStoryComment>> = flow {
        val comments = getSampleComments().filter { it.storyId == storyId }
        emit(comments)
    }

    fun addComment(storyId: String, content: String): Flow<SuccessStoryComment> = flow {
        // TODO: Implement API call to add comment
        val comment = SuccessStoryComment(
            id = "comment_${System.currentTimeMillis()}",
            storyId = storyId,
            userId = "current_user_id",
            userName = "Current User",
            userAvatar = null,
            content = content,
            createdAt = System.currentTimeMillis()
        )
        emit(comment)
    }

    private fun applyFilter(stories: List<SuccessStory>, filter: SuccessStoryFilter?): List<SuccessStory> {
        if (filter == null) return stories

        var filteredStories = stories

        filter.examCategory?.let { category ->
            filteredStories = filteredStories.filter { it.examCategory.id == category.id }
        }

        filter.examSubcategory?.let { subcategory ->
            filteredStories = filteredStories.filter { it.examSubcategory.id == subcategory.id }
        }

        filter.year?.let { year ->
            filteredStories = filteredStories.filter { it.year == year }
        }

        filter.minRank?.let { minRank ->
            filteredStories = filteredStories.filter { it.rank != null && it.rank >= minRank }
        }

        filter.maxRank?.let { maxRank ->
            filteredStories = filteredStories.filter { it.rank != null && it.rank <= maxRank }
        }

        filter.attempts?.let { attempts ->
            filteredStories = filteredStories.filter { it.attempts == attempts }
        }

        filter.searchQuery?.let { query ->
            if (query.isNotBlank()) {
                filteredStories = filteredStories.filter { story ->
                    story.title.contains(query, ignoreCase = true) ||
                    story.content.contains(query, ignoreCase = true) ||
                    story.authorName.contains(query, ignoreCase = true)
                }
            }
        }

        return when (filter.sortBy) {
            SortBy.RECENT -> filteredStories.sortedByDescending { it.createdAt }
            SortBy.POPULAR -> filteredStories.sortedByDescending { it.likes + it.views }
            SortBy.MOST_LIKED -> filteredStories.sortedByDescending { it.likes }
            SortBy.MOST_VIEWED -> filteredStories.sortedByDescending { it.views }
            SortBy.RANK_ASCENDING -> filteredStories.sortedBy { it.rank ?: Int.MAX_VALUE }
            SortBy.RANK_DESCENDING -> filteredStories.sortedByDescending { it.rank ?: 0 }
        }
    }

    private fun getSampleSuccessStories(): List<SuccessStory> {
        val upscCategory = examRepository.getExamCategoryById("upsc")!!
        val sscCategory = examRepository.getExamCategoryById("ssc")!!
        val railwaysCategory = examRepository.getExamCategoryById("railways")!!

        return listOf(
            SuccessStory(
                id = "story_1",
                authorId = "user_1",
                authorName = "Priya Sharma",
                authorAvatar = null,
                title = "From Village to IAS: My Journey of Determination",
                content = "Growing up in a small village in Rajasthan, I never imagined I would become an IAS officer. My journey started with a dream and countless hours of preparation. The key was consistency and never giving up, even when I failed in my first two attempts. Here's my complete strategy and preparation plan that helped me crack UPSC CSE 2023 with AIR 45...",
                examCategory = upscCategory,
                examSubcategory = upscCategory.subcategories.first { it.id == "upsc_civil_services" },
                rank = 45,
                year = 2023,
                attempts = 3,
                preparationDuration = "2.5 years",
                keyTips = listOf(
                    "Start with NCERT books for building strong foundation",
                    "Practice answer writing daily for mains preparation",
                    "Stay updated with current affairs through multiple sources",
                    "Take regular mock tests and analyze performance",
                    "Maintain physical and mental health throughout preparation"
                ),
                resources = listOf(
                    "Laxmikanth for Polity",
                    "Spectrum for Modern History",
                    "The Hindu newspaper for current affairs"
                ),
                createdAt = System.currentTimeMillis() - 86400000, // 1 day ago
                updatedAt = System.currentTimeMillis() - 86400000,
                likes = 1250,
                comments = 89,
                shares = 156,
                views = 5420,
                tags = listOf("IAS", "UPSC", "Village Background", "Third Attempt")
            ),
            SuccessStory(
                id = "story_2",
                authorId = "user_2",
                authorName = "Rahul Kumar",
                authorAvatar = null,
                title = "SSC CGL Success: Working Professional's Strategy",
                content = "Balancing a full-time job while preparing for SSC CGL was challenging, but not impossible. I cleared SSC CGL 2022 while working as a software engineer. Time management and smart study techniques were crucial for my success. Here's how I managed to study effectively with just 2-3 hours daily...",
                examCategory = sscCategory,
                examSubcategory = sscCategory.subcategories.first { it.id == "ssc_cgl" },
                rank = 156,
                year = 2022,
                attempts = 2,
                preparationDuration = "18 months",
                keyTips = listOf(
                    "Create a realistic study schedule that fits your work routine",
                    "Focus on high-yield topics first",
                    "Use mobile apps for quick revision during commute",
                    "Join online test series for regular practice",
                    "Don't compromise on sleep and health"
                ),
                resources = listOf(
                    "Kiran's SSC Mathematics",
                    "Lucent GK for General Awareness",
                    "Previous year question papers"
                ),
                createdAt = System.currentTimeMillis() - 172800000, // 2 days ago
                updatedAt = System.currentTimeMillis() - 172800000,
                likes = 890,
                comments = 67,
                shares = 123,
                views = 3210,
                tags = listOf("SSC CGL", "Working Professional", "Time Management")
            ),
            SuccessStory(
                id = "story_3",
                authorId = "user_3",
                authorName = "Anjali Patel",
                authorAvatar = null,
                title = "Railway NTPC Success: First Attempt Victory",
                content = "I cleared RRB NTPC 2021 in my first attempt with a good rank. Coming from a commerce background, I had to work extra hard on the technical sections. My preparation strategy focused on understanding concepts rather than rote learning...",
                examCategory = railwaysCategory,
                examSubcategory = railwaysCategory.subcategories.first { it.id == "rrb_ntpc" },
                rank = 234,
                year = 2021,
                attempts = 1,
                preparationDuration = "10 months",
                keyTips = listOf(
                    "Understand the exam pattern thoroughly",
                    "Focus on speed and accuracy for CBT",
                    "Practice mental math for quick calculations",
                    "Stay updated with railway-related current affairs",
                    "Take sectional tests regularly"
                ),
                resources = listOf(
                    "RRB NTPC Guide by Arihant",
                    "Railway GK by Upkar Publication",
                    "Online mock test series"
                ),
                createdAt = System.currentTimeMillis() - 259200000, // 3 days ago
                updatedAt = System.currentTimeMillis() - 259200000,
                likes = 567,
                comments = 45,
                shares = 89,
                views = 2340,
                tags = listOf("RRB NTPC", "First Attempt", "Commerce Background")
            )
        )
    }

    private fun getSampleComments(): List<SuccessStoryComment> {
        return listOf(
            SuccessStoryComment(
                id = "comment_1",
                storyId = "story_1",
                userId = "user_4",
                userName = "Amit Singh",
                userAvatar = null,
                content = "Very inspiring story! Your journey from village to IAS is truly motivational. Could you please share more details about your optional subject preparation?",
                createdAt = System.currentTimeMillis() - 43200000, // 12 hours ago
                likes = 23
            ),
            SuccessStoryComment(
                id = "comment_2",
                storyId = "story_1",
                userId = "user_5",
                userName = "Neha Gupta",
                userAvatar = null,
                content = "Thank you for sharing such detailed preparation strategy. The resource list is very helpful. All the best for your training!",
                createdAt = System.currentTimeMillis() - 21600000, // 6 hours ago
                likes = 15
            )
        )
    }
}