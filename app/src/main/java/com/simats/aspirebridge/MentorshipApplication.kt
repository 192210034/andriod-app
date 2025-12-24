package com.simats.aspirebridge

import android.app.Application
import com.simats.aspirebridge.di.DependencyContainer

/**
 * Application class for the Mentorship Platform
 */
class MentorshipApplication : Application() {
    
    // Central dependency container
    lateinit var container: DependencyContainer
        private set
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize dependency container
        container = DependencyContainer(this)
    }
}