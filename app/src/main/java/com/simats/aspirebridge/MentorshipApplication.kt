package com.simats.aspirebridge

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the Mentorship Platform
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection
 */
@HiltAndroidApp
class MentorshipApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
    }
}