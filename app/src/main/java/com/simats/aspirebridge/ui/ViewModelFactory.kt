package com.simats.aspirebridge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.simats.aspirebridge.di.DependencyContainer
import com.simats.aspirebridge.ui.admin.AdminContentManagementViewModel
import com.simats.aspirebridge.ui.admin.AdminDashboardViewModel
import com.simats.aspirebridge.ui.admin.AdminUserManagementViewModel
import com.simats.aspirebridge.ui.booking.BookSessionViewModel
import com.simats.aspirebridge.ui.chat.ChatListViewModel
import com.simats.aspirebridge.ui.home.HomeViewModel
import com.simats.aspirebridge.ui.mentors.BrowseMentorsViewModel
import com.simats.aspirebridge.ui.mentors.MentorProfileViewModel
import com.simats.aspirebridge.ui.resources.ResourceHubViewModel
import com.simats.aspirebridge.ui.stories.SuccessStoriesViewModel

/**
 * ViewModelFactory to replace Hilt's ViewModel injection
 * Creates ViewModels with proper dependency injection
 */
class ViewModelFactory(private val container: DependencyContainer) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            HomeViewModel::class.java -> HomeViewModel(
                container.successStoryRepository,
                container.resourceRepository
            ) as T
            
            SuccessStoriesViewModel::class.java -> SuccessStoriesViewModel(
                container.successStoryRepository,
                container.examRepository
            ) as T
            
            ResourceHubViewModel::class.java -> ResourceHubViewModel(
                container.resourceRepository,
                container.examRepository
            ) as T
            
            AdminDashboardViewModel::class.java -> AdminDashboardViewModel(
                container.successStoryRepository,
                container.resourceRepository
            ) as T
            
            AdminContentManagementViewModel::class.java -> AdminContentManagementViewModel(
                container.successStoryRepository,
                container.resourceRepository
            ) as T
            
            AdminUserManagementViewModel::class.java -> AdminUserManagementViewModel() as T
            
            BrowseMentorsViewModel::class.java -> BrowseMentorsViewModel(
                container.mentorRepository
            ) as T
            
            MentorProfileViewModel::class.java -> MentorProfileViewModel(
                container.mentorRepository
            ) as T
            
            BookSessionViewModel::class.java -> BookSessionViewModel(
                container.mentorRepository
            ) as T
            
            ChatListViewModel::class.java -> ChatListViewModel(
                container.chatRepository
            ) as T
            
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}