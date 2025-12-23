package com.simats.aspirebridge.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.aspirebridge.data.repository.ResourceRepository
import com.simats.aspirebridge.data.repository.SuccessStoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Admin Dashboard
 */
@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val successStoryRepository: SuccessStoryRepository,
    private val resourceRepository: ResourceRepository
    // TODO: Add UserRepository, SessionRepository when implemented
) : ViewModel() {

    private val _platformStats = MutableStateFlow(PlatformStatistics())
    val platformStats: StateFlow<PlatformStatistics> = _platformStats.asStateFlow()

    private val _contentStats = MutableStateFlow(ContentStatistics())
    val contentStats: StateFlow<ContentStatistics> = _contentStats.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadPlatformStatistics() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // TODO: Implement actual data loading from repositories
                // For now, using mock data
                val stats = PlatformStatistics(
                    totalUsers = 1250,
                    aspirantsCount = 950,
                    achieversCount = 300,
                    activeSessions = 45,
                    totalSessions = 2340,
                    totalRevenue = 125000f
                )
                
                _platformStats.value = stats
                
            } catch (e: Exception) {
                _error.value = "Failed to load platform statistics: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadContentStatistics() {
        viewModelScope.launch {
            try {
                // Load success stories count
                successStoryRepository.getAllSuccessStories().collect { stories ->
                    val currentStats = _contentStats.value
                    _contentStats.value = currentStats.copy(
                        successStoriesCount = stories.size
                    )
                }
                
                // Load resources count
                resourceRepository.getResources().collect { resources ->
                    val currentStats = _contentStats.value
                    _contentStats.value = currentStats.copy(
                        resourcesCount = resources.size
                    )
                }
                
            } catch (e: Exception) {
                _error.value = "Failed to load content statistics: ${e.message}"
            }
        }
    }

    fun deleteSuccessStory(storyId: String) {
        viewModelScope.launch {
            try {
                // TODO: Implement story deletion
                // successStoryRepository.deleteStory(storyId)
                loadContentStatistics() // Refresh stats
            } catch (e: Exception) {
                _error.value = "Failed to delete success story: ${e.message}"
            }
        }
    }

    fun deleteResource(resourceId: String) {
        viewModelScope.launch {
            try {
                // TODO: Implement resource deletion
                // resourceRepository.deleteResource(resourceId)
                loadContentStatistics() // Refresh stats
            } catch (e: Exception) {
                _error.value = "Failed to delete resource: ${e.message}"
            }
        }
    }

    fun banUser(userId: String) {
        viewModelScope.launch {
            try {
                // TODO: Implement user banning
                // userRepository.banUser(userId)
                loadPlatformStatistics() // Refresh stats
            } catch (e: Exception) {
                _error.value = "Failed to ban user: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}

/**
 * Data class for platform statistics
 */
data class PlatformStatistics(
    val totalUsers: Int = 0,
    val aspirantsCount: Int = 0,
    val achieversCount: Int = 0,
    val activeSessions: Int = 0,
    val totalSessions: Int = 0,
    val totalRevenue: Float = 0f
)

/**
 * Data class for content statistics
 */
data class ContentStatistics(
    val successStoriesCount: Int = 0,
    val resourcesCount: Int = 0,
    val reportedContent: Int = 0,
    val pendingApprovals: Int = 0
)