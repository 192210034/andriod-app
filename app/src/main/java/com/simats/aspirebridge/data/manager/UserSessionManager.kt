package com.simats.aspirebridge.data.manager

import android.content.Context
import android.content.SharedPreferences
import com.simats.aspirebridge.data.model.User
import com.simats.aspirebridge.data.model.UserType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages user session and authentication state
 */
@Singleton
class UserSessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    init {
        loadUserSession()
    }
    
    fun getCurrentUserType(): UserType {
        return _currentUser.value?.userType ?: UserType.ASPIRANT
    }
    
    fun getCurrentUserId(): String? {
        return _currentUser.value?.id
    }
    
    fun isAdmin(): Boolean {
        return getCurrentUserType() == UserType.ADMIN
    }
    
    fun isAchiever(): Boolean {
        return getCurrentUserType() == UserType.ACHIEVER
    }
    
    fun isAspirant(): Boolean {
        return getCurrentUserType() == UserType.ASPIRANT
    }
    
    fun setCurrentUser(user: User) {
        _currentUser.value = user
        _isLoggedIn.value = true
        saveUserSession(user)
    }
    
    fun logout() {
        _currentUser.value = null
        _isLoggedIn.value = false
        clearUserSession()
    }
    
    private fun loadUserSession() {
        val userId = prefs.getString(KEY_USER_ID, null)
        val userName = prefs.getString(KEY_USER_NAME, null)
        val userEmail = prefs.getString(KEY_USER_EMAIL, null)
        val userTypeString = prefs.getString(KEY_USER_TYPE, null)
        
        if (userId != null && userName != null && userEmail != null && userTypeString != null) {
            val userType = try {
                UserType.valueOf(userTypeString)
            } catch (e: IllegalArgumentException) {
                UserType.ASPIRANT
            }
            
            val user = User(
                id = userId,
                name = userName,
                email = userEmail,
                userType = userType,
                profilePicture = prefs.getString(KEY_USER_PROFILE_PICTURE, null),
                bio = prefs.getString(KEY_USER_BIO, "") ?: "",
                rating = prefs.getFloat(KEY_USER_RATING, 0f),
                totalSessions = prefs.getInt(KEY_USER_TOTAL_SESSIONS, 0)
            )
            
            _currentUser.value = user
            _isLoggedIn.value = true
        }
    }
    
    private fun saveUserSession(user: User) {
        prefs.edit().apply {
            putString(KEY_USER_ID, user.id)
            putString(KEY_USER_NAME, user.name)
            putString(KEY_USER_EMAIL, user.email)
            putString(KEY_USER_TYPE, user.userType.name)
            putString(KEY_USER_PROFILE_PICTURE, user.profilePicture)
            putString(KEY_USER_BIO, user.bio)
            putFloat(KEY_USER_RATING, user.rating)
            putInt(KEY_USER_TOTAL_SESSIONS, user.totalSessions)
            apply()
        }
    }
    
    private fun clearUserSession() {
        prefs.edit().clear().apply()
    }
    
    // Mock method for testing - sets a mock user
    fun setMockUser(userType: UserType) {
        val mockUser = when (userType) {
            UserType.ASPIRANT -> User(
                id = "aspirant_001",
                name = "Priya Sharma",
                email = "priya.sharma@example.com",
                userType = UserType.ASPIRANT,
                bio = "UPSC aspirant preparing for Civil Services",
                totalSessions = 5
            )
            UserType.ACHIEVER -> User(
                id = "achiever_001",
                name = "Rajesh Kumar",
                email = "rajesh.kumar@example.com",
                userType = UserType.ACHIEVER,
                bio = "IAS 2019, Rank 45. Helping aspirants achieve their dreams.",
                rating = 4.8f,
                totalSessions = 120
            )
            UserType.ADMIN -> User(
                id = "admin_001",
                name = "Platform Administrator",
                email = "admin@aspirebridge.com",
                userType = UserType.ADMIN,
                bio = "System Administrator",
                totalSessions = 0
            )
        }
        setCurrentUser(mockUser)
    }
    
    companion object {
        private const val PREFS_NAME = "user_session"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_TYPE = "user_type"
        private const val KEY_USER_PROFILE_PICTURE = "user_profile_picture"
        private const val KEY_USER_BIO = "user_bio"
        private const val KEY_USER_RATING = "user_rating"
        private const val KEY_USER_TOTAL_SESSIONS = "user_total_sessions"
    }
}