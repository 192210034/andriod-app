package com.simats.aspirebridge.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.aspirebridge.data.model.User
import com.simats.aspirebridge.data.model.UserType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for Admin User Management
 */
class AdminUserManagementViewModel(
    // TODO: Add UserRepository when implemented
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _filteredUsers = MutableStateFlow<List<User>>(emptyList())
    val filteredUsers: StateFlow<List<User>> = _filteredUsers.asStateFlow()

    private val _currentFilter = MutableStateFlow(UserFilter.ALL)
    val currentFilter: StateFlow<UserFilter> = _currentFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _userStats = MutableStateFlow(UserStats())
    val userStats: StateFlow<UserStats> = _userStats.asStateFlow()

    init {
        loadUsers()
        setupFiltering()
    }

    private fun setupFiltering() {
        combine(
            _users,
            _currentFilter,
            _searchQuery
        ) { users, filter, query ->
            var filtered = when (filter) {
                UserFilter.ALL -> users
                UserFilter.ASPIRANTS -> users.filter { it.userType == UserType.ASPIRANT }
                UserFilter.ACHIEVERS -> users.filter { it.userType == UserType.ACHIEVER }
            }

            if (query.isNotBlank()) {
                filtered = filtered.filter { user ->
                    user.name.contains(query, ignoreCase = true) ||
                    user.email.contains(query, ignoreCase = true)
                }
            }

            filtered
        }.onEach { filteredList ->
            _filteredUsers.value = filteredList
        }.launchIn(viewModelScope)
    }

    fun loadUsers() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // TODO: Load from actual repository
                // For now, using mock data
                val mockUsers = generateMockUsers()
                _users.value = mockUsers
                
                // Update stats
                updateUserStats(mockUsers)
                
            } catch (e: Exception) {
                _error.value = "Failed to load users: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setFilter(filter: UserFilter) {
        _currentFilter.value = filter
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun banUser(user: User) {
        viewModelScope.launch {
            try {
                // TODO: Implement actual user banning
                // userRepository.banUser(user.id)
                
                // For now, just remove from list
                val updatedUsers = _users.value.filter { it.id != user.id }
                _users.value = updatedUsers
                updateUserStats(updatedUsers)
                
            } catch (e: Exception) {
                _error.value = "Failed to ban user: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    private fun updateUserStats(users: List<User>) {
        val stats = UserStats(
            totalUsers = users.size,
            aspirantsCount = users.count { it.userType == UserType.ASPIRANT },
            achieversCount = users.count { it.userType == UserType.ACHIEVER }
        )
        _userStats.value = stats
    }

    private fun generateMockUsers(): List<User> {
        return listOf(
            // Aspirants
            User(
                id = "asp_001",
                name = "Priya Sharma",
                email = "priya.sharma@example.com",
                userType = UserType.ASPIRANT,
                bio = "UPSC aspirant preparing for Civil Services",
                totalSessions = 15,
                createdAt = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000) // 30 days ago
            ),
            User(
                id = "asp_002",
                name = "Amit Kumar",
                email = "amit.kumar@example.com",
                userType = UserType.ASPIRANT,
                bio = "SSC CGL aspirant",
                totalSessions = 8,
                createdAt = System.currentTimeMillis() - (45L * 24 * 60 * 60 * 1000) // 45 days ago
            ),
            User(
                id = "asp_003",
                name = "Sneha Patel",
                email = "sneha.patel@example.com",
                userType = UserType.ASPIRANT,
                bio = "Banking exam preparation",
                totalSessions = 22,
                createdAt = System.currentTimeMillis() - (60L * 24 * 60 * 60 * 1000) // 60 days ago
            ),
            User(
                id = "asp_004",
                name = "Rahul Singh",
                email = "rahul.singh@example.com",
                userType = UserType.ASPIRANT,
                bio = "Railway exam aspirant",
                totalSessions = 5,
                createdAt = System.currentTimeMillis() - (15L * 24 * 60 * 60 * 1000) // 15 days ago
            ),
            User(
                id = "asp_005",
                name = "Kavya Reddy",
                email = "kavya.reddy@example.com",
                userType = UserType.ASPIRANT,
                bio = "Defence exam preparation",
                totalSessions = 12,
                createdAt = System.currentTimeMillis() - (90L * 24 * 60 * 60 * 1000) // 90 days ago
            ),

            // Achievers
            User(
                id = "ach_001",
                name = "Rajesh Kumar",
                email = "rajesh.kumar@example.com",
                userType = UserType.ACHIEVER,
                bio = "IAS 2019, Rank 45. Helping aspirants achieve their dreams.",
                rating = 4.8f,
                totalSessions = 120,
                createdAt = System.currentTimeMillis() - (365L * 24 * 60 * 60 * 1000) // 1 year ago
            ),
            User(
                id = "ach_002",
                name = "Dr. Meera Joshi",
                email = "meera.joshi@example.com",
                userType = UserType.ACHIEVER,
                bio = "IPS 2020, Rank 23. Specialized in interview guidance.",
                rating = 4.9f,
                totalSessions = 95,
                createdAt = System.currentTimeMillis() - (300L * 24 * 60 * 60 * 1000) // 300 days ago
            ),
            User(
                id = "ach_003",
                name = "Suresh Gupta",
                email = "suresh.gupta@example.com",
                userType = UserType.ACHIEVER,
                bio = "SSC CGL 2018, Topper. Expert in quantitative aptitude.",
                rating = 4.7f,
                totalSessions = 85,
                createdAt = System.currentTimeMillis() - (400L * 24 * 60 * 60 * 1000) // 400 days ago
            ),
            User(
                id = "ach_004",
                name = "Anita Verma",
                email = "anita.verma@example.com",
                userType = UserType.ACHIEVER,
                bio = "SBI PO 2019. Banking sector expert and mentor.",
                rating = 4.6f,
                totalSessions = 67,
                createdAt = System.currentTimeMillis() - (250L * 24 * 60 * 60 * 1000) // 250 days ago
            ),
            User(
                id = "ach_005",
                name = "Captain Vikram",
                email = "vikram.captain@example.com",
                userType = UserType.ACHIEVER,
                bio = "NDA 2017. Defence services mentor and trainer.",
                rating = 4.8f,
                totalSessions = 78,
                createdAt = System.currentTimeMillis() - (500L * 24 * 60 * 60 * 1000) // 500 days ago
            )
        )
    }
}

/**
 * User filter options
 */
enum class UserFilter {
    ALL, ASPIRANTS, ACHIEVERS
}

/**
 * User statistics data class
 */
data class UserStats(
    val totalUsers: Int = 0,
    val aspirantsCount: Int = 0,
    val achieversCount: Int = 0
)