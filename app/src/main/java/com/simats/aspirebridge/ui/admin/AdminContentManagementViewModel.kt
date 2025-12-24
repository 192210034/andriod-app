package com.simats.aspirebridge.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.aspirebridge.data.repository.ResourceRepository
import com.simats.aspirebridge.data.repository.SuccessStoryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for Admin Content Management
 */
class AdminContentManagementViewModel(
    private val successStoryRepository: SuccessStoryRepository,
    private val resourceRepository: ResourceRepository
) : ViewModel() {

    private val _contentList = MutableStateFlow<List<AdminContentItem>>(emptyList())
    val contentList: StateFlow<List<AdminContentItem>> = _contentList.asStateFlow()

    private val _allContent = MutableStateFlow<List<AdminContentItem>>(emptyList())
    private val _currentFilter = MutableStateFlow("ALL")

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadContent(contentType: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                when (contentType) {
                    "STORIES" -> loadSuccessStories()
                    "RESOURCES" -> loadResources()
                    else -> loadAllContent()
                }
                
            } catch (e: Exception) {
                _error.value = "Failed to load content: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun loadSuccessStories() {
        val stories = successStoryRepository.getAllSuccessStories()
        val contentItems = stories.map { story ->
            AdminContentItem(
                id = story.id,
                title = story.title,
                authorName = "Author Name", // TODO: Get from user repository
                type = "STORY",
                status = "Active",
                createdAt = story.createdAt,
                likesCount = story.likes,
                isReported = false // TODO: Implement reporting system
            )
        }
        _allContent.value = contentItems
        applyCurrentFilter()
    }

    private suspend fun loadResources() {
        val resources = resourceRepository.getAllResources()
        val contentItems = resources.map { resource ->
                AdminContentItem(
                    id = resource.id,
                    title = resource.title,
                    authorName = "Author Name", // TODO: Get from user repository
                    type = "RESOURCE",
                    status = "Active",
                    createdAt = resource.createdAt,
                    likesCount = resource.downloads, // Using downloads as likes for resources
                    isReported = false // TODO: Implement reporting system
                )
            }
            _allContent.value = contentItems
            applyCurrentFilter()
    }

    private suspend fun loadAllContent() {
        // Load both stories and resources
        val allItems = mutableListOf<AdminContentItem>()
        
        val stories = successStoryRepository.getAllSuccessStories()
        allItems.addAll(stories.map { story ->
            AdminContentItem(
                id = story.id,
                title = story.title,
                authorName = "Author Name",
                type = "STORY",
                status = "Active",
                createdAt = story.createdAt,
                likesCount = story.likes,
                isReported = false
            )
        })
        
        val resources = resourceRepository.getAllResources()
        allItems.addAll(resources.map { resource ->
            AdminContentItem(
                id = resource.id,
                title = resource.title,
                authorName = "Author Name",
                type = "RESOURCE",
                status = "Active",
                createdAt = resource.createdAt,
                likesCount = resource.downloads,
                isReported = false
            )
        })
        
        _allContent.value = allItems.sortedByDescending { it.createdAt }
        applyCurrentFilter()
    }

    fun filterContent(filter: String) {
        _currentFilter.value = filter
        applyCurrentFilter()
    }

    private fun applyCurrentFilter() {
        val filtered = when (_currentFilter.value) {
            "REPORTED" -> _allContent.value.filter { it.isReported }
            "RECENT" -> _allContent.value.sortedByDescending { it.createdAt }.take(20)
            else -> _allContent.value
        }
        _contentList.value = filtered
    }

    fun deleteContent(contentId: String, contentType: String) {
        viewModelScope.launch {
            try {
                when (contentType) {
                    "STORIES" -> {
                        // TODO: Implement story deletion
                        // successStoryRepository.deleteStory(contentId)
                    }
                    "RESOURCES" -> {
                        // TODO: Implement resource deletion
                        // resourceRepository.deleteResource(contentId)
                    }
                }
                
                // Remove from current list
                val updatedList = _allContent.value.filter { it.id != contentId }
                _allContent.value = updatedList
                applyCurrentFilter()
                
            } catch (e: Exception) {
                _error.value = "Failed to delete content: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}