package com.simats.aspirebridge.ui.stories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.aspirebridge.data.model.ExamCategory
import com.simats.aspirebridge.data.model.SuccessStory
import com.simats.aspirebridge.data.model.SuccessStoryFilter
import com.simats.aspirebridge.data.repository.ExamRepository
import com.simats.aspirebridge.data.repository.SuccessStoryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for Success Stories screen
 */
class SuccessStoriesViewModel(
    private val successStoryRepository: SuccessStoryRepository,
    private val examRepository: ExamRepository
) : ViewModel() {

    private val _stories = MutableStateFlow<List<SuccessStory>>(emptyList())
    val stories: StateFlow<List<SuccessStory>> = _stories.asStateFlow()

    private val _examCategories = MutableStateFlow<List<ExamCategory>>(emptyList())
    val examCategories: StateFlow<List<ExamCategory>> = _examCategories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isAchiever = MutableStateFlow(false) // TODO: Get from user session
    val isAchiever: StateFlow<Boolean> = _isAchiever.asStateFlow()

    fun loadExamCategories() {
        viewModelScope.launch {
            try {
                val categories = examRepository.getAllExamCategories()
                _examCategories.value = categories
            } catch (e: Exception) {
                _error.value = "Failed to load exam categories: ${e.message}"
            }
        }
    }

    fun loadSuccessStories(filter: SuccessStoryFilter, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                successStoryRepository.getSuccessStories(filter).collect { stories ->
                    _stories.value = stories
                }
            } catch (e: Exception) {
                _error.value = "Failed to load success stories: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun likeStory(storyId: String) {
        viewModelScope.launch {
            try {
                successStoryRepository.likeStory(storyId).collect { success ->
                    if (success) {
                        // Update the story in the current list
                        _stories.value = _stories.value.map { story ->
                            if (story.id == storyId) {
                                story.copy(
                                    isLiked = !story.isLiked,
                                    likes = if (story.isLiked) story.likes - 1 else story.likes + 1
                                )
                            } else {
                                story
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _error.value = "Failed to like story: ${e.message}"
            }
        }
    }

    fun bookmarkStory(storyId: String) {
        viewModelScope.launch {
            try {
                successStoryRepository.bookmarkStory(storyId).collect { success ->
                    if (success) {
                        // Update the story in the current list
                        _stories.value = _stories.value.map { story ->
                            if (story.id == storyId) {
                                story.copy(isBookmarked = !story.isBookmarked)
                            } else {
                                story
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _error.value = "Failed to bookmark story: ${e.message}"
            }
        }
    }

    fun shareStory(storyId: String) {
        viewModelScope.launch {
            try {
                successStoryRepository.shareStory(storyId).collect { success ->
                    if (success) {
                        // Update the story in the current list
                        _stories.value = _stories.value.map { story ->
                            if (story.id == storyId) {
                                story.copy(shares = story.shares + 1)
                            } else {
                                story
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _error.value = "Failed to share story: ${e.message}"
            }
        }
    }

    fun searchStories(query: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                successStoryRepository.searchSuccessStories(query).collect { stories ->
                    _stories.value = stories
                }
            } catch (e: Exception) {
                _error.value = "Failed to search stories: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getRecentStories(limit: Int = 5) {
        viewModelScope.launch {
            try {
                successStoryRepository.getRecentSuccessStories(limit).collect { stories ->
                    _stories.value = stories
                }
            } catch (e: Exception) {
                _error.value = "Failed to load recent stories: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}