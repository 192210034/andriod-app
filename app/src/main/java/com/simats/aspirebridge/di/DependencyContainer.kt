package com.simats.aspirebridge.di

import android.content.Context
import com.simats.aspirebridge.data.api.ApiService
import com.simats.aspirebridge.data.manager.UserSessionManager
import com.simats.aspirebridge.data.repository.ChatRepository
import com.simats.aspirebridge.data.repository.ExamRepository
import com.simats.aspirebridge.data.repository.JobRepository
import com.simats.aspirebridge.data.repository.MentorRepository
import com.simats.aspirebridge.data.repository.ResourceRepository
import com.simats.aspirebridge.data.repository.SuccessStoryRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Central dependency container to replace Hilt dependency injection
 * Manages all application dependencies using lazy initialization
 */
class DependencyContainer(private val context: Context) {
    
    companion object {
        @Volatile
        private var INSTANCE: DependencyContainer? = null
        
        fun getInstance(context: Context): DependencyContainer {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DependencyContainer(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    // Network layer
    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.aspirebridge.com/") // TODO: Replace with actual API URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
    
    // Managers
    val userSessionManager: UserSessionManager by lazy {
        UserSessionManager(context)
    }
    
    // Repository layer
    val jobRepository: JobRepository by lazy {
        JobRepository(apiService)
    }
    
    val successStoryRepository: SuccessStoryRepository by lazy {
        SuccessStoryRepository(apiService)
    }
    
    val resourceRepository: ResourceRepository by lazy {
        ResourceRepository(apiService)
    }
    
    val examRepository: ExamRepository by lazy {
        ExamRepository(apiService)
    }
    
    val mentorRepository: MentorRepository by lazy {
        MentorRepository()
    }
    
    val chatRepository: ChatRepository by lazy {
        ChatRepository()
    }
}