package com.simats.aspirebridge.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.aspirebridge.data.model.ChatRoom
import com.simats.aspirebridge.data.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _chatRooms = MutableLiveData<List<ChatRoom>>()
    val chatRooms: LiveData<List<ChatRoom>> = _chatRooms

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadChatRooms()
    }

    fun loadChatRooms() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val rooms = chatRepository.getChatRooms()
                _chatRooms.value = rooms

            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load chats"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshChatRooms() {
        loadChatRooms()
    }

    fun clearError() {
        _error.value = null
    }
}