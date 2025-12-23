package com.simats.aspirebridge.data.api

import com.simats.aspirebridge.data.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit API service interface for backend communication
 */
interface ApiService {
    
    // Authentication endpoints
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<MessageResponse>
    
    // User profile endpoints
    @GET("api/users/{id}")
    suspend fun getUserProfile(@Path("id") userId: String): Response<User>
    
    @PUT("api/users/{id}")
    suspend fun updateProfile(@Path("id") userId: String, @Body user: User): Response<User>
    
    // Mentor discovery endpoints
    @GET("api/mentors/search")
    suspend fun searchMentors(
        @Query("q") query: String? = null,
        @Query("skills") skills: String? = null,
        @Query("location") location: String? = null,
        @Query("page") page: Int = 0,
        @Query("limit") limit: Int = 20
    ): Response<SearchResponse>
    
    @GET("api/mentors/{id}")
    suspend fun getMentorDetails(@Path("id") mentorId: String): Response<MentorDetails>
    
    // Request endpoints
    @POST("api/requests")
    suspend fun sendMentorshipRequest(@Body request: MentorshipRequest): Response<MentorshipRequest>
    
    @GET("api/requests/user/{id}")
    suspend fun getUserRequests(@Path("id") userId: String): Response<List<MentorshipRequest>>
    
    @PUT("api/requests/{id}/accept")
    suspend fun acceptRequest(@Path("id") requestId: String): Response<MessageResponse>
    
    @PUT("api/requests/{id}/decline")
    suspend fun declineRequest(@Path("id") requestId: String): Response<MessageResponse>
    
    // Session endpoints
    @POST("api/sessions")
    suspend fun createSession(@Body session: Session): Response<Session>
    
    @GET("api/sessions/user/{id}")
    suspend fun getUserSessions(@Path("id") userId: String): Response<List<Session>>
    
    @PUT("api/sessions/{id}")
    suspend fun updateSession(@Path("id") sessionId: String, @Body session: Session): Response<Session>
    
    // Messaging endpoints
    @POST("api/messages")
    suspend fun sendMessage(@Body message: Message): Response<Message>
    
    @GET("api/messages/chat/{chatId}")
    suspend fun getMessages(@Path("chatId") chatId: String): Response<List<Message>>
    
    // Feedback endpoints
    @POST("api/feedback")
    suspend fun submitFeedback(@Body feedback: Feedback): Response<Feedback>
    
    @GET("api/feedback/user/{id}")
    suspend fun getUserFeedback(@Path("id") userId: String): Response<List<Feedback>>
}