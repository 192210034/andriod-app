package com.simats.aspirebridge.utils

/**
 * Application constants
 */
object Constants {
    
    // API Configuration
    const val BASE_URL = "http://10.0.2.2:3000/" // Android emulator localhost
    // For physical device, use: "http://YOUR_COMPUTER_IP:3000/"
    
    // SharedPreferences keys
    const val PREF_NAME = "mentorship_prefs"
    const val KEY_AUTH_TOKEN = "auth_token"
    const val KEY_USER_ID = "user_id"
    const val KEY_USER_TYPE = "user_type"
    
    // Request codes
    const val REQUEST_CODE_CAMERA = 1001
    const val REQUEST_CODE_GALLERY = 1002
    
    // Database
    const val DATABASE_NAME = "mentorship_database"
    const val DATABASE_VERSION = 1
    
    // Pagination
    const val PAGE_SIZE = 20
    
    // Socket events
    const val SOCKET_EVENT_JOIN_CHAT = "join-chat"
    const val SOCKET_EVENT_SEND_MESSAGE = "send-message"
    const val SOCKET_EVENT_NEW_MESSAGE = "new-message"
    const val SOCKET_EVENT_TYPING = "typing"
    const val SOCKET_EVENT_STOP_TYPING = "stop-typing"
}