package com.simats.aspirebridge.ui.resources

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.aspirebridge.data.model.ExamCategory
import com.simats.aspirebridge.data.model.Resource
import com.simats.aspirebridge.data.model.ResourceFilter
import com.simats.aspirebridge.data.repository.ExamRepository
import com.simats.aspirebridge.data.repository.ResourceRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for Resource Hub screen
 */
class ResourceHubViewModel(
    private val resourceRepository: ResourceRepository,
    private val examRepository: ExamRepository
) : ViewModel() {

    private val _resources = MutableStateFlow<List<Resource>>(emptyList())
    val resources: StateFlow<List<Resource>> = _resources.asStateFlow()

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

    fun loadResources(filter: ResourceFilter, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                resourceRepository.getResources(filter).collect { resources ->
                    _resources.value = resources
                }
            } catch (e: Exception) {
                _error.value = "Failed to load resources: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun downloadResource(resourceId: String) {
        viewModelScope.launch {
            try {
                resourceRepository.downloadResource(resourceId).collect { success ->
                    if (success) {
                        // Update the resource in the current list
                        _resources.value = _resources.value.map { resource ->
                            if (resource.id == resourceId) {
                                resource.copy(downloads = resource.downloads + 1)
                            } else {
                                resource
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _error.value = "Failed to download resource: ${e.message}"
            }
        }
    }

    fun bookmarkResource(resourceId: String) {
        viewModelScope.launch {
            try {
                resourceRepository.bookmarkResource(resourceId).collect { success ->
                    if (success) {
                        // Update the resource in the current list
                        _resources.value = _resources.value.map { resource ->
                            if (resource.id == resourceId) {
                                resource.copy(isBookmarked = !resource.isBookmarked)
                            } else {
                                resource
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _error.value = "Failed to bookmark resource: ${e.message}"
            }
        }
    }

    fun rateResource(resourceId: String, rating: Float, review: String?) {
        viewModelScope.launch {
            try {
                resourceRepository.rateResource(resourceId, rating, review).collect { success ->
                    if (success) {
                        // TODO: Update resource rating in the list
                    }
                }
            } catch (e: Exception) {
                _error.value = "Failed to rate resource: ${e.message}"
            }
        }
    }

    fun searchResources(query: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                resourceRepository.searchResources(query).collect { resources ->
                    _resources.value = resources
                }
            } catch (e: Exception) {
                _error.value = "Failed to search resources: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getPopularResources(limit: Int = 10) {
        viewModelScope.launch {
            try {
                resourceRepository.getPopularResources(limit).collect { resources ->
                    _resources.value = resources
                }
            } catch (e: Exception) {
                _error.value = "Failed to load popular resources: ${e.message}"
            }
        }
    }

    fun getTopRatedResources(limit: Int = 10) {
        viewModelScope.launch {
            try {
                resourceRepository.getTopRatedResources(limit).collect { resources ->
                    _resources.value = resources
                }
            } catch (e: Exception) {
                _error.value = "Failed to load top rated resources: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}