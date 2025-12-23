package com.simats.aspirebridge.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.aspirebridge.data.model.SuccessStory
import com.simats.aspirebridge.data.model.UserType
import com.simats.aspirebridge.data.model.Session
import com.simats.aspirebridge.data.model.BookingRequest
import com.simats.aspirebridge.data.model.Wallet
import com.simats.aspirebridge.data.repository.ResourceRepository
import com.simats.aspirebridge.data.repository.SuccessStoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Home screen with role-based dashboard support
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val successStoryRepository: SuccessStoryRepository,
    private val resourceRepository: ResourceRepository
    // TODO: Add SessionRepository, WalletRepository, BookingRepository when implemented
) : ViewModel() {

    private val _recentSuccessStories = MutableStateFlow<List<SuccessStory>>(emptyList())
    val recentSuccessStories: StateFlow<List<SuccessStory>> = _recentSuccessStories.asStateFlow()

    private val _featuredMentors = MutableStateFlow<List<Any>>(emptyList()) // TODO: Replace with MentorProfile
    val featuredMentors: StateFlow<List<Any>> = _featuredMentors.asStateFlow()

    private val _resourceCount = MutableStateFlow(0)
    val resourceCount: StateFlow<Int> = _resourceCount.asStateFlow()

    // Aspirant-specific data
    private val _upcomingSessions = MutableStateFlow<List<Session>>(emptyList())
    val upcomingSessions: StateFlow<List<Session>> = _upcomingSessions.asStateFlow()

    private val _walletBalance = MutableStateFlow<Wallet?>(null)
    val walletBalance: StateFlow<Wallet?> = _walletBalance.asStateFlow()

    // Achiever-specific data
    private val _pendingBookingRequests = MutableStateFlow<List<BookingRequest>>(emptyList())
    val pendingBookingRequests: StateFlow<List<BookingRequest>> = _pendingBookingRequests.asStateFlow()

    private val _monthlyEarnings = MutableStateFlow(0f)
    val monthlyEarnings: StateFlow<Float> = _monthlyEarnings.asStateFlow()

    private val _totalSessions = MutableStateFlow(0)
    val totalSessions: StateFlow<Int> = _totalSessions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadRecentSuccessStories(limit: Int = 3) {
        viewModelScope.launch {
            try {
                successStoryRepository.getRecentSuccessStories(limit).collect { stories ->
                    _recentSuccessStories.value = stories
                }
            } catch (e: Exception) {
                _error.value = "Failed to load recent success stories: ${e.message}"
            }
        }
    }

    fun loadFeaturedMentors(limit: Int = 3) {
        viewModelScope.launch {
            try {
                // TODO: Implement mentor repository and load featured mentors
                _featuredMentors.value = emptyList()
            } catch (e: Exception) {
                _error.value = "Failed to load featured mentors: ${e.message}"
            }
        }
    }

    fun loadMentorsByCategory(categoryId: String?) {
        viewModelScope.launch {
            try {
                // TODO: Implement mentor filtering by category
                _featuredMentors.value = emptyList()
            } catch (e: Exception) {
                _error.value = "Failed to load mentors by category: ${e.message}"
            }
        }
    }

    fun loadResourceCount() {
        viewModelScope.launch {
            try {
                resourceRepository.getResources().collect { resources ->
                    _resourceCount.value = resources.size
                }
            } catch (e: Exception) {
                _error.value = "Failed to load resource count: ${e.message}"
            }
        }
    }

    // Aspirant-specific methods
    fun loadAspirantDashboardData(userId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // Load upcoming sessions
                loadUpcomingSessions(userId)
                
                // Load wallet balance
                loadWalletBalance(userId)
                
                // Load common data
                loadRecentSuccessStories()
                loadResourceCount()
                
            } catch (e: Exception) {
                _error.value = "Failed to load aspirant dashboard data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadUpcomingSessions(userId: String) {
        viewModelScope.launch {
            try {
                // TODO: Implement session repository
                // sessionRepository.getUpcomingSessions(userId).collect { sessions ->
                //     _upcomingSessions.value = sessions
                // }
                
                // Mock data for now
                _upcomingSessions.value = emptyList()
            } catch (e: Exception) {
                _error.value = "Failed to load upcoming sessions: ${e.message}"
            }
        }
    }

    private fun loadWalletBalance(userId: String) {
        viewModelScope.launch {
            try {
                // TODO: Implement wallet repository
                // walletRepository.getWallet(userId).collect { wallet ->
                //     _walletBalance.value = wallet
                // }
                
                // Mock data for now
                _walletBalance.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load wallet balance: ${e.message}"
            }
        }
    }

    // Achiever-specific methods
    fun loadAchieverDashboardData(userId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // Load pending booking requests
                loadPendingBookingRequests(userId)
                
                // Load monthly earnings
                loadMonthlyEarnings(userId)
                
                // Load total sessions
                loadTotalSessions(userId)
                
                // Load upcoming sessions
                loadUpcomingSessions(userId)
                
                // Load wallet balance
                loadWalletBalance(userId)
                
                // Load common data
                loadRecentSuccessStories()
                loadResourceCount()
                
            } catch (e: Exception) {
                _error.value = "Failed to load achiever dashboard data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadPendingBookingRequests(userId: String) {
        viewModelScope.launch {
            try {
                // TODO: Implement booking repository
                // bookingRepository.getPendingRequests(userId).collect { requests ->
                //     _pendingBookingRequests.value = requests
                // }
                
                // Mock data for now
                _pendingBookingRequests.value = emptyList()
            } catch (e: Exception) {
                _error.value = "Failed to load pending booking requests: ${e.message}"
            }
        }
    }

    private fun loadMonthlyEarnings(userId: String) {
        viewModelScope.launch {
            try {
                // TODO: Implement wallet repository
                // walletRepository.getMonthlyEarnings(userId).collect { earnings ->
                //     _monthlyEarnings.value = earnings
                // }
                
                // Mock data for now
                _monthlyEarnings.value = 8500f
            } catch (e: Exception) {
                _error.value = "Failed to load monthly earnings: ${e.message}"
            }
        }
    }

    private fun loadTotalSessions(userId: String) {
        viewModelScope.launch {
            try {
                // TODO: Implement session repository
                // sessionRepository.getTotalSessionsCount(userId).collect { count ->
                //     _totalSessions.value = count
                // }
                
                // Mock data for now
                _totalSessions.value = 12
            } catch (e: Exception) {
                _error.value = "Failed to load total sessions: ${e.message}"
            }
        }
    }

    // Common methods
    fun acceptBookingRequest(requestId: String) {
        viewModelScope.launch {
            try {
                // TODO: Implement booking repository
                // bookingRepository.acceptRequest(requestId)
                // Reload pending requests
                // loadPendingBookingRequests(currentUserId)
            } catch (e: Exception) {
                _error.value = "Failed to accept booking request: ${e.message}"
            }
        }
    }

    fun declineBookingRequest(requestId: String, reason: String = "") {
        viewModelScope.launch {
            try {
                // TODO: Implement booking repository
                // bookingRepository.declineRequest(requestId, reason)
                // Reload pending requests
                // loadPendingBookingRequests(currentUserId)
            } catch (e: Exception) {
                _error.value = "Failed to decline booking request: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}