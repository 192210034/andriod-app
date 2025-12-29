package com.simats.aspirebridge.ui.mentors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.aspirebridge.data.model.AvailabilitySlot
import com.simats.aspirebridge.data.model.Mentor
import com.simats.aspirebridge.data.model.MentorReview
import com.simats.aspirebridge.data.repository.MentorRepository
import kotlinx.coroutines.launch

class MentorProfileViewModel(
    private val mentorRepository: MentorRepository
) : ViewModel() {

    private val _mentor = MutableLiveData<Mentor?>()
    val mentor: LiveData<Mentor?> = _mentor

    private val _availableSlots = MutableLiveData<List<AvailabilitySlot>>()
    val availableSlots: LiveData<List<AvailabilitySlot>> = _availableSlots

    private val _reviews = MutableLiveData<List<MentorReview>>()
    val reviews: LiveData<List<MentorReview>> = _reviews

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadMentor(mentorId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                // Load mentor details
                val mentorData = mentorRepository.getMentorById(mentorId)
                _mentor.value = mentorData

                // Load availability slots (mock data for now)
                _availableSlots.value = generateMockAvailabilitySlots(mentorId)

                // Load reviews
                val reviewsData = mentorRepository.getMentorReviews(mentorId)
                _reviews.value = reviewsData

            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load mentor profile"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFollow() {
        val currentMentor = _mentor.value ?: return
        
        viewModelScope.launch {
            try {
                val success = mentorRepository.toggleFollow(currentMentor.id)
                if (success) {
                    _mentor.value = currentMentor.copy(isFollowing = !currentMentor.isFollowing)
                }
            } catch (e: Exception) {
                _error.value = "Failed to update follow status"
            }
        }
    }

    private fun generateMockAvailabilitySlots(mentorId: String): List<AvailabilitySlot> {
        // Mock availability slots - in real app, this would come from API
        return listOf(
            AvailabilitySlot(
                id = "slot1",
                mentorId = mentorId,
                date = "2024-01-16",
                startTime = "10:00",
                endTime = "11:00",
                isBooked = false
            ),
            AvailabilitySlot(
                id = "slot2",
                mentorId = mentorId,
                date = "2024-01-16",
                startTime = "14:00",
                endTime = "15:00",
                isBooked = false
            ),
            AvailabilitySlot(
                id = "slot3",
                mentorId = mentorId,
                date = "2024-01-17",
                startTime = "09:00",
                endTime = "10:00",
                isBooked = false
            ),
            AvailabilitySlot(
                id = "slot4",
                mentorId = mentorId,
                date = "2024-01-17",
                startTime = "16:00",
                endTime = "17:00",
                isBooked = false
            ),
            AvailabilitySlot(
                id = "slot5",
                mentorId = mentorId,
                date = "2024-01-18",
                startTime = "11:00",
                endTime = "12:00",
                isBooked = false
            )
        )
    }
}