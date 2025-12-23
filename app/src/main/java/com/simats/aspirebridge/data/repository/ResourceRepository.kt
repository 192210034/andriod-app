package com.simats.aspirebridge.data.repository

import com.simats.aspirebridge.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceRepository @Inject constructor(
    private val examRepository: ExamRepository
) {

    fun getResources(filter: ResourceFilter? = null): Flow<List<Resource>> = flow {
        val resources = getSampleResources()
        val filteredResources = applyFilter(resources, filter)
        emit(filteredResources)
    }

    fun getResourceById(id: String): Flow<Resource?> = flow {
        val resource = getSampleResources().find { it.id == id }
        emit(resource)
    }

    fun getResourcesByCategory(categoryId: String): Flow<List<Resource>> = flow {
        val resources = getSampleResources().filter { it.examCategory.id == categoryId }
        emit(resources)
    }

    fun getResourcesBySubcategory(subcategoryId: String): Flow<List<Resource>> = flow {
        val resources = getSampleResources().filter { it.examSubcategory.id == subcategoryId }
        emit(resources)
    }

    fun getResourcesByType(type: ResourceType): Flow<List<Resource>> = flow {
        val resources = getSampleResources().filter { it.type == type }
        emit(resources)
    }

    fun getRecentResources(limit: Int = 10): Flow<List<Resource>> = flow {
        val resources = getSampleResources()
            .sortedByDescending { it.createdAt }
            .take(limit)
        emit(resources)
    }

    fun getPopularResources(limit: Int = 10): Flow<List<Resource>> = flow {
        val resources = getSampleResources()
            .sortedByDescending { it.downloads + it.views }
            .take(limit)
        emit(resources)
    }

    fun getTopRatedResources(limit: Int = 10): Flow<List<Resource>> = flow {
        val resources = getSampleResources()
            .filter { it.rating >= 4.0f }
            .sortedByDescending { it.rating }
            .take(limit)
        emit(resources)
    }

    fun searchResources(query: String): Flow<List<Resource>> = flow {
        val resources = getSampleResources().filter { resource ->
            resource.title.contains(query, ignoreCase = true) ||
            resource.description.contains(query, ignoreCase = true) ||
            resource.uploaderName.contains(query, ignoreCase = true) ||
            resource.tags.any { it.contains(query, ignoreCase = true) }
        }
        emit(resources)
    }

    fun bookmarkResource(resourceId: String): Flow<Boolean> = flow {
        // TODO: Implement API call to bookmark resource
        emit(true)
    }

    fun downloadResource(resourceId: String): Flow<Boolean> = flow {
        // TODO: Implement API call to track download
        emit(true)
    }

    fun rateResource(resourceId: String, rating: Float, review: String?): Flow<Boolean> = flow {
        // TODO: Implement API call to rate resource
        emit(true)
    }

    fun getResourceRatings(resourceId: String): Flow<List<ResourceRating>> = flow {
        val ratings = getSampleRatings().filter { it.resourceId == resourceId }
        emit(ratings)
    }

    fun getUserCollections(userId: String): Flow<List<ResourceCollection>> = flow {
        val collections = getSampleCollections().filter { it.userId == userId }
        emit(collections)
    }

    fun createCollection(name: String, description: String?, isPublic: Boolean): Flow<ResourceCollection> = flow {
        val collection = ResourceCollection(
            id = "collection_${System.currentTimeMillis()}",
            name = name,
            description = description,
            userId = "current_user_id",
            resourceIds = emptyList(),
            isPublic = isPublic,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        emit(collection)
    }

    private fun applyFilter(resources: List<Resource>, filter: ResourceFilter?): List<Resource> {
        if (filter == null) return resources

        var filteredResources = resources

        filter.examCategory?.let { category ->
            filteredResources = filteredResources.filter { it.examCategory.id == category.id }
        }

        filter.examSubcategory?.let { subcategory ->
            filteredResources = filteredResources.filter { it.examSubcategory.id == subcategory.id }
        }

        filter.resourceType?.let { type ->
            filteredResources = filteredResources.filter { it.type == type }
        }

        filter.difficulty?.let { difficulty ->
            filteredResources = filteredResources.filter { it.difficulty == difficulty }
        }

        filter.language?.let { language ->
            filteredResources = filteredResources.filter { it.language == language }
        }

        filter.minRating?.let { minRating ->
            filteredResources = filteredResources.filter { it.rating >= minRating }
        }

        if (filter.tags.isNotEmpty()) {
            filteredResources = filteredResources.filter { resource ->
                filter.tags.any { tag -> resource.tags.contains(tag) }
            }
        }

        filter.searchQuery?.let { query ->
            if (query.isNotBlank()) {
                filteredResources = filteredResources.filter { resource ->
                    resource.title.contains(query, ignoreCase = true) ||
                    resource.description.contains(query, ignoreCase = true) ||
                    resource.uploaderName.contains(query, ignoreCase = true)
                }
            }
        }

        return when (filter.sortBy) {
            ResourceSortBy.RECENT -> filteredResources.sortedByDescending { it.createdAt }
            ResourceSortBy.POPULAR -> filteredResources.sortedByDescending { it.downloads + it.views }
            ResourceSortBy.MOST_DOWNLOADED -> filteredResources.sortedByDescending { it.downloads }
            ResourceSortBy.HIGHEST_RATED -> filteredResources.sortedByDescending { it.rating }
            ResourceSortBy.MOST_VIEWED -> filteredResources.sortedByDescending { it.views }
            ResourceSortBy.ALPHABETICAL -> filteredResources.sortedBy { it.title }
        }
    }

    private fun getSampleResources(): List<Resource> {
        val upscCategory = examRepository.getExamCategoryById("upsc")!!
        val sscCategory = examRepository.getExamCategoryById("ssc")!!
        val railwaysCategory = examRepository.getExamCategoryById("railways")!!

        return listOf(
            Resource(
                id = "resource_1",
                title = "UPSC Prelims Previous Year Papers (2015-2023)",
                description = "Complete collection of UPSC Civil Services Preliminary examination question papers from 2015 to 2023 with detailed solutions and explanations.",
                type = ResourceType.PDF,
                url = "https://example.com/upsc-prelims-papers.pdf",
                thumbnailUrl = null,
                fileSize = 15728640, // 15 MB
                fileName = "UPSC_Prelims_Papers_2015-2023.pdf",
                uploaderId = "user_1",
                uploaderName = "Priya Sharma",
                uploaderAvatar = null,
                examCategory = upscCategory,
                examSubcategory = upscCategory.subcategories.first { it.id == "upsc_civil_services" },
                tags = listOf("Previous Year Papers", "Prelims", "CSAT", "GS Paper 1"),
                downloads = 2340,
                views = 5670,
                rating = 4.7f,
                ratingCount = 156,
                createdAt = System.currentTimeMillis() - 86400000,
                updatedAt = System.currentTimeMillis() - 86400000,
                language = "English",
                difficulty = DifficultyLevel.INTERMEDIATE
            ),
            Resource(
                id = "resource_2",
                title = "SSC CGL Mathematics Formula Sheet",
                description = "Comprehensive formula sheet covering all important mathematical formulas and shortcuts for SSC CGL examination. Includes geometry, algebra, trigonometry, and arithmetic formulas.",
                type = ResourceType.PDF,
                url = "https://example.com/ssc-math-formulas.pdf",
                thumbnailUrl = null,
                fileSize = 2097152, // 2 MB
                fileName = "SSC_CGL_Math_Formulas.pdf",
                uploaderId = "user_2",
                uploaderName = "Rahul Kumar",
                uploaderAvatar = null,
                examCategory = sscCategory,
                examSubcategory = sscCategory.subcategories.first { it.id == "ssc_cgl" },
                tags = listOf("Mathematics", "Formulas", "Quick Reference", "Shortcuts"),
                downloads = 1890,
                views = 3450,
                rating = 4.5f,
                ratingCount = 89,
                createdAt = System.currentTimeMillis() - 172800000,
                updatedAt = System.currentTimeMillis() - 172800000,
                language = "English",
                difficulty = DifficultyLevel.BEGINNER
            ),
            Resource(
                id = "resource_3",
                title = "Railway NTPC General Awareness Notes",
                description = "Detailed notes covering all important topics for Railway NTPC General Awareness section including current affairs, static GK, and railway-specific information.",
                type = ResourceType.NOTES,
                url = "https://example.com/railway-ga-notes",
                thumbnailUrl = null,
                fileSize = null,
                fileName = null,
                uploaderId = "user_3",
                uploaderName = "Anjali Patel",
                uploaderAvatar = null,
                examCategory = railwaysCategory,
                examSubcategory = railwaysCategory.subcategories.first { it.id == "rrb_ntpc" },
                tags = listOf("General Awareness", "Current Affairs", "Static GK", "Railway GK"),
                downloads = 1234,
                views = 2890,
                rating = 4.3f,
                ratingCount = 67,
                createdAt = System.currentTimeMillis() - 259200000,
                updatedAt = System.currentTimeMillis() - 259200000,
                language = "English",
                difficulty = DifficultyLevel.INTERMEDIATE
            ),
            Resource(
                id = "resource_4",
                title = "UPSC Essay Writing Strategy Video",
                description = "Complete video guide on how to write high-scoring essays for UPSC Mains examination. Covers structure, content, and presentation techniques.",
                type = ResourceType.VIDEO,
                url = "https://example.com/upsc-essay-strategy",
                thumbnailUrl = "https://example.com/thumbnails/essay-video.jpg",
                fileSize = null,
                fileName = null,
                uploaderId = "user_4",
                uploaderName = "Dr. Vikash Ranjan",
                uploaderAvatar = null,
                examCategory = upscCategory,
                examSubcategory = upscCategory.subcategories.first { it.id == "upsc_civil_services" },
                tags = listOf("Essay Writing", "Mains", "Strategy", "High Scoring"),
                downloads = 0, // Videos are not downloaded
                views = 8920,
                rating = 4.8f,
                ratingCount = 234,
                createdAt = System.currentTimeMillis() - 345600000,
                updatedAt = System.currentTimeMillis() - 345600000,
                language = "Hindi",
                difficulty = DifficultyLevel.ADVANCED
            ),
            Resource(
                id = "resource_5",
                title = "SSC CHSL English Practice Tests",
                description = "Collection of 50 practice tests for SSC CHSL English section covering grammar, vocabulary, comprehension, and error detection.",
                type = ResourceType.PRACTICE_TEST,
                url = "https://example.com/ssc-chsl-english-tests",
                thumbnailUrl = null,
                fileSize = null,
                fileName = null,
                uploaderId = "user_5",
                uploaderName = "Meera Joshi",
                uploaderAvatar = null,
                examCategory = sscCategory,
                examSubcategory = sscCategory.subcategories.first { it.id == "ssc_chsl" },
                tags = listOf("English", "Practice Tests", "Grammar", "Vocabulary"),
                downloads = 567,
                views = 1890,
                rating = 4.2f,
                ratingCount = 45,
                createdAt = System.currentTimeMillis() - 432000000,
                updatedAt = System.currentTimeMillis() - 432000000,
                language = "English",
                difficulty = DifficultyLevel.INTERMEDIATE
            )
        )
    }

    private fun getSampleRatings(): List<ResourceRating> {
        return listOf(
            ResourceRating(
                id = "rating_1",
                resourceId = "resource_1",
                userId = "user_6",
                userName = "Amit Verma",
                rating = 5.0f,
                review = "Excellent collection of previous year papers. Very helpful for understanding the exam pattern and difficulty level.",
                createdAt = System.currentTimeMillis() - 86400000,
                helpfulCount = 23
            ),
            ResourceRating(
                id = "rating_2",
                resourceId = "resource_1",
                userId = "user_7",
                userName = "Sneha Reddy",
                rating = 4.5f,
                review = "Good resource but could have included more detailed explanations for some questions.",
                createdAt = System.currentTimeMillis() - 172800000,
                helpfulCount = 12
            )
        )
    }

    private fun getSampleCollections(): List<ResourceCollection> {
        return listOf(
            ResourceCollection(
                id = "collection_1",
                name = "UPSC Preparation Essentials",
                description = "My curated collection of must-have resources for UPSC preparation",
                userId = "current_user_id",
                resourceIds = listOf("resource_1", "resource_4"),
                isPublic = true,
                createdAt = System.currentTimeMillis() - 86400000,
                updatedAt = System.currentTimeMillis() - 86400000,
                followers = 45
            ),
            ResourceCollection(
                id = "collection_2",
                name = "SSC Mathematics Resources",
                description = "Complete mathematics preparation material for SSC exams",
                userId = "current_user_id",
                resourceIds = listOf("resource_2"),
                isPublic = false,
                createdAt = System.currentTimeMillis() - 172800000,
                updatedAt = System.currentTimeMillis() - 172800000,
                followers = 0
            )
        )
    }
}