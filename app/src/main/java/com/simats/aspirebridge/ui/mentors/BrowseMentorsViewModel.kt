package com.simats.aspirebridge.ui.mentors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.aspirebridge.data.model.Mentor
import com.simats.aspirebridge.data.repository.MentorRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BrowseMentorsViewModel(
    private val mentorRepository: MentorRepository
) : ViewModel() {

    private val _mentors = MutableLiveData<List<Mentor>>()
    val mentors: LiveData<List<Mentor>> = _mentors

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    private val _selectedFilters = MutableLiveData<List<String>>()
    val selectedFilters: LiveData<List<String>> = _selectedFilters

    private var allMentors: List<Mentor> = emptyList()
    private var currentSearchQuery = ""
    private var currentFilters: List<String> = listOf("All")
    private var currentSortBy = "rating"
    
    private var searchJob: Job? = null

    init {
        _selectedFilters.value = listOf("All")
    }

    fun loadMentors() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                allMentors = mentorRepository.getAllMentors()
                applyFiltersAndSort()
            } catch (e: Exception) {
                // Handle error
                _mentors.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSearchQuery(query: String) {
        currentSearchQuery = query
        
        // Debounce search to avoid too many API calls
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // Wait 300ms before executing search
            applyFiltersAndSort()
        }
    }

    fun updateFilters(filters: List<String>) {
        currentFilters = if (filters.isEmpty()) listOf("All") else filters
        _selectedFilters.value = currentFilters
        applyFiltersAndSort()
    }

    fun sortBy(sortType: String) {
        currentSortBy = sortType
        applyFiltersAndSort()
    }

    fun toggleFollow(mentorId: String) {
        viewModelScope.launch {
            try {
                mentorRepository.toggleFollow(mentorId)
                // Update the mentor in the current list
                val updatedMentors = _mentors.value?.map { mentor ->
                    if (mentor.id == mentorId) {
                        mentor.copy(isFollowing = !mentor.isFollowing)
                    } else {
                        mentor
                    }
                }
                _mentors.value = updatedMentors ?: emptyList()
            } catch (e: Exception) {
                // Handle error - maybe show a toast
            }
        }
    }

    private fun applyFiltersAndSort() {
        var filteredMentors = allMentors

        // Apply search filter
        if (currentSearchQuery.isNotBlank()) {
            filteredMentors = filteredMentors.filter { mentor ->
                mentor.name.contains(currentSearchQuery, ignoreCase = true) ||
                mentor.bio.contains(currentSearchQuery, ignoreCase = true) ||
                mentor.expertise.any { it.contains(currentSearchQuery, ignoreCase = true) }
            }
        }

        // Apply category filters
        if (!currentFilters.contains("All")) {
            filteredMentors = filteredMentors.filter { mentor ->
                currentFilters.any { filter ->
                    mentor.examCategory.contains(filter, ignoreCase = true) ||
                    mentor.expertise.any { expertise -> 
                        expertise.contains(filter, ignoreCase = true) 
                    }
                }
            }
        }

        // Apply sorting
        filteredMentors = when (currentSortBy) {
            "rating" -> filteredMentors.sortedByDescending { it.rating }
            "price" -> filteredMentors.sortedBy { it.hourlyRate }
            "experience" -> filteredMentors.sortedByDescending { it.experienceYears }
            else -> filteredMentors
        }

        _mentors.value = filteredMentors
        _isEmpty.value = filteredMentors.isEmpty() && !(_isLoading.value == true)
    }
}