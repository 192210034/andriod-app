package com.simats.aspirebridge.data.repository

import com.simats.aspirebridge.data.model.*
import kotlinx.coroutines.delay

class MentorRepository {

    // Mock data for now - will be replaced with API calls
    private val mockMentors = listOf(
        Mentor(
            id = "1",
            name = "Rajesh Kumar",
            email = "rajesh@example.com",
            bio = "Experienced IAS officer with 5+ years in administration. Specialized in General Studies and Essay writing. Helped 200+ aspirants achieve their dreams.",
            examCategory = "UPSC",
            examSubCategory = "Civil Services",
            examCleared = "UPSC CSE",
            rank = "45",
            examYear = "2020",
            hourlyRate = 500,
            experienceYears = 5,
            rating = 4.8,
            reviewsCount = 124,
            sessionsCompleted = 150,
            studentsHelped = 89,
            expertise = listOf("General Studies", "Essay Writing", "Interview Prep"),
            isVerified = true,
            isApproved = true,
            createdAt = "2023-01-15",
            updatedAt = "2024-01-15"
        ),
        Mentor(
            id = "2",
            name = "Priya Sharma",
            email = "priya@example.com",
            bio = "Banking professional turned mentor. Cleared SBI PO and IBPS exams. Expert in Quantitative Aptitude and Reasoning.",
            examCategory = "Banking",
            examSubCategory = "SBI PO",
            examCleared = "SBI PO",
            rank = "12",
            examYear = "2019",
            hourlyRate = 400,
            experienceYears = 4,
            rating = 4.9,
            reviewsCount = 98,
            sessionsCompleted = 120,
            studentsHelped = 67,
            expertise = listOf("Quantitative Aptitude", "Reasoning", "Banking Awareness"),
            isVerified = true,
            isApproved = true,
            createdAt = "2023-02-20",
            updatedAt = "2024-01-10"
        ),
        Mentor(
            id = "3",
            name = "Amit Singh",
            email = "amit@example.com",
            bio = "Railway engineer with expertise in technical subjects. Cleared RRB JE and helped many aspirants in technical preparation.",
            examCategory = "Railways",
            examSubCategory = "RRB JE",
            examCleared = "RRB JE",
            rank = "8",
            examYear = "2021",
            hourlyRate = 350,
            experienceYears = 3,
            rating = 4.7,
            reviewsCount = 76,
            sessionsCompleted = 95,
            studentsHelped = 54,
            expertise = listOf("Technical Subjects", "General Awareness", "Mathematics"),
            isVerified = true,
            isApproved = true,
            createdAt = "2023-03-10",
            updatedAt = "2024-01-05"
        ),
        Mentor(
            id = "4",
            name = "Sneha Patel",
            email = "sneha@example.com",
            bio = "SSC CGL topper with strong foundation in all subjects. Passionate about teaching and helping aspirants achieve their goals.",
            examCategory = "SSC",
            examSubCategory = "CGL",
            examCleared = "SSC CGL",
            rank = "3",
            examYear = "2022",
            hourlyRate = 450,
            experienceYears = 2,
            rating = 4.6,
            reviewsCount = 52,
            sessionsCompleted = 78,
            studentsHelped = 43,
            expertise = listOf("General Intelligence", "English", "Quantitative Aptitude"),
            isVerified = true,
            isApproved = true,
            createdAt = "2023-04-05",
            updatedAt = "2024-01-12"
        ),
        Mentor(
            id = "5",
            name = "Captain Vikram",
            email = "vikram@example.com",
            bio = "Retired Army officer and NDA alumnus. Expert in defense preparation and personality development for defense aspirants.",
            examCategory = "Defence",
            examSubCategory = "NDA",
            examCleared = "NDA",
            rank = "15",
            examYear = "2018",
            hourlyRate = 600,
            experienceYears = 6,
            rating = 4.9,
            reviewsCount = 145,
            sessionsCompleted = 200,
            studentsHelped = 112,
            expertise = listOf("Mathematics", "General Ability", "Personality Development"),
            isVerified = true,
            isApproved = true,
            createdAt = "2023-01-01",
            updatedAt = "2024-01-20"
        )
    )

    suspend fun getAllMentors(): List<Mentor> {
        // Simulate network delay
        delay(1000)
        return mockMentors
    }

    suspend fun getMentorById(id: String): Mentor? {
        delay(500)
        return mockMentors.find { it.id == id }
    }

    suspend fun searchMentors(
        query: String,
        filters: MentorFilter? = null,
        page: Int = 1,
        pageSize: Int = 20
    ): MentorSearchResult {
        delay(800)
        
        var filteredMentors = mockMentors
        
        // Apply search query
        if (query.isNotBlank()) {
            filteredMentors = filteredMentors.filter { mentor ->
                mentor.name.contains(query, ignoreCase = true) ||
                mentor.bio.contains(query, ignoreCase = true) ||
                mentor.expertise.any { it.contains(query, ignoreCase = true) }
            }
        }
        
        // Apply filters
        filters?.let { filter ->
            if (filter.examCategories.isNotEmpty()) {
                filteredMentors = filteredMentors.filter { mentor ->
                    filter.examCategories.any { category ->
                        mentor.examCategory.contains(category, ignoreCase = true)
                    }
                }
            }
            
            filter.minRating?.let { minRating ->
                filteredMentors = filteredMentors.filter { it.rating >= minRating }
            }
            
            filter.maxRating?.let { maxRating ->
                filteredMentors = filteredMentors.filter { it.rating <= maxRating }
            }
            
            filter.minPrice?.let { minPrice ->
                filteredMentors = filteredMentors.filter { it.hourlyRate >= minPrice }
            }
            
            filter.maxPrice?.let { maxPrice ->
                filteredMentors = filteredMentors.filter { it.hourlyRate <= maxPrice }
            }
            
            filter.minExperience?.let { minExp ->
                filteredMentors = filteredMentors.filter { it.experienceYears >= minExp }
            }
            
            filter.maxExperience?.let { maxExp ->
                filteredMentors = filteredMentors.filter { it.experienceYears <= maxExp }
            }
            
            if (filter.expertise.isNotEmpty()) {
                filteredMentors = filteredMentors.filter { mentor ->
                    filter.expertise.any { expertise ->
                        mentor.expertise.any { mentorExpertise ->
                            mentorExpertise.contains(expertise, ignoreCase = true)
                        }
                    }
                }
            }
            
            filter.isVerified?.let { verified ->
                filteredMentors = filteredMentors.filter { it.isVerified == verified }
            }
        }
        
        // Pagination
        val startIndex = (page - 1) * pageSize
        val endIndex = minOf(startIndex + pageSize, filteredMentors.size)
        val paginatedMentors = if (startIndex < filteredMentors.size) {
            filteredMentors.subList(startIndex, endIndex)
        } else {
            emptyList()
        }
        
        return MentorSearchResult(
            mentors = paginatedMentors,
            totalCount = filteredMentors.size,
            hasMore = endIndex < filteredMentors.size,
            nextPage = if (endIndex < filteredMentors.size) page + 1 else null
        )
    }

    suspend fun toggleFollow(mentorId: String): Boolean {
        delay(300)
        // Mock implementation - in real app, this would make API call
        return true
    }

    suspend fun getMentorReviews(mentorId: String): List<MentorReview> {
        delay(500)
        // Mock reviews
        return listOf(
            MentorReview(
                id = "1",
                mentorId = mentorId,
                studentId = "student1",
                studentName = "Rahul Verma",
                rating = 5,
                review = "Excellent mentor! Very knowledgeable and patient. Helped me clear my doubts effectively.",
                sessionId = "session1",
                createdAt = "2024-01-10"
            ),
            MentorReview(
                id = "2",
                mentorId = mentorId,
                studentId = "student2",
                studentName = "Anita Sharma",
                rating = 4,
                review = "Good session. The mentor explained concepts clearly and provided useful tips.",
                sessionId = "session2",
                createdAt = "2024-01-08"
            )
        )
    }

    suspend fun getMentorStats(mentorId: String): MentorStats {
        delay(400)
        return MentorStats(
            totalSessions = 150,
            totalStudents = 89,
            averageRating = 4.8,
            totalReviews = 124,
            totalEarnings = 75000.0,
            successRate = 95.5,
            responseTime = "< 2 hours",
            completionRate = 98.2
        )
    }

    suspend fun bookSession(bookingRequest: BookingRequest): BookingRequest {
        delay(1000)
        // Mock booking - in real app, this would create actual booking
        return bookingRequest.copy(
            id = "booking_${System.currentTimeMillis()}",
            status = BookingStatus.PENDING,
            createdAt = "2024-01-15",
            updatedAt = "2024-01-15"
        )
    }
}