package com.simats.aspirebridge.data.repository

import com.simats.aspirebridge.data.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatRepository {

    // Mock data for now - will be replaced with API calls and Socket.IO
    private val _chatRooms = MutableStateFlow<List<ChatRoom>>(emptyList())
    val chatRooms: Flow<List<ChatRoom>> = _chatRooms.asStateFlow()

    private val _messages = MutableStateFlow<Map<String, List<ChatMessage>>>(emptyMap())
    val messages: Flow<Map<String, List<ChatMessage>>> = _messages.asStateFlow()

    private val _typingIndicators = MutableStateFlow<List<TypingIndicator>>(emptyList())
    val typingIndicators: Flow<List<TypingIndicator>> = _typingIndicators.asStateFlow()

    init {
        // Initialize with mock data
        initializeMockData()
    }

    private fun initializeMockData() {
        val mockRooms = listOf(
            ChatRoom(
                id = "room1",
                participants = listOf("current_user", "mentor1"),
                participantDetails = listOf(
                    ChatParticipant(
                        userId = "mentor1",
                        name = "Rajesh Kumar",
                        profilePicture = "",
                        isOnline = true,
                        role = "mentor"
                    )
                ),
                lastMessage = ChatMessage(
                    id = "msg1",
                    roomId = "room1",
                    senderId = "mentor1",
                    senderName = "Rajesh Kumar",
                    content = "Hi! I'm ready for our session tomorrow. Do you have any specific topics you'd like to focus on?",
                    timestamp = "2024-01-15T10:30:00Z"
                ),
                unreadCount = 1,
                createdAt = "2024-01-10T09:00:00Z",
                updatedAt = "2024-01-15T10:30:00Z"
            ),
            ChatRoom(
                id = "room2",
                participants = listOf("current_user", "mentor2"),
                participantDetails = listOf(
                    ChatParticipant(
                        userId = "mentor2",
                        name = "Priya Sharma",
                        profilePicture = "",
                        isOnline = false,
                        lastSeen = "2024-01-15T08:45:00Z",
                        role = "mentor"
                    )
                ),
                lastMessage = ChatMessage(
                    id = "msg2",
                    roomId = "room2",
                    senderId = "current_user",
                    senderName = "You",
                    content = "Thank you for the session! The quantitative aptitude tips were really helpful.",
                    timestamp = "2024-01-14T16:20:00Z"
                ),
                unreadCount = 0,
                createdAt = "2024-01-12T14:00:00Z",
                updatedAt = "2024-01-14T16:20:00Z"
            ),
            ChatRoom(
                id = "room3",
                participants = listOf("current_user", "student1", "student2"),
                participantDetails = listOf(
                    ChatParticipant(
                        userId = "student1",
                        name = "Amit Singh",
                        profilePicture = "",
                        isOnline = true,
                        role = "student"
                    ),
                    ChatParticipant(
                        userId = "student2",
                        name = "Sneha Patel",
                        profilePicture = "",
                        isOnline = false,
                        lastSeen = "2024-01-15T09:15:00Z",
                        role = "student"
                    )
                ),
                lastMessage = ChatMessage(
                    id = "msg3",
                    roomId = "room3",
                    senderId = "student1",
                    senderName = "Amit Singh",
                    content = "Anyone has notes for General Studies Paper 2?",
                    timestamp = "2024-01-15T11:45:00Z"
                ),
                unreadCount = 2,
                isGroup = true,
                groupName = "UPSC Study Group",
                createdAt = "2024-01-08T12:00:00Z",
                updatedAt = "2024-01-15T11:45:00Z"
            )
        )

        val mockMessages = mapOf(
            "room1" to listOf(
                ChatMessage(
                    id = "msg1_1",
                    roomId = "room1",
                    senderId = "current_user",
                    senderName = "You",
                    content = "Hello! I booked a session with you for tomorrow at 2 PM.",
                    timestamp = "2024-01-15T09:00:00Z"
                ),
                ChatMessage(
                    id = "msg1_2",
                    roomId = "room1",
                    senderId = "mentor1",
                    senderName = "Rajesh Kumar",
                    content = "Hi! Yes, I can see the booking. Looking forward to our session.",
                    timestamp = "2024-01-15T09:15:00Z"
                ),
                ChatMessage(
                    id = "msg1_3",
                    roomId = "room1",
                    senderId = "mentor1",
                    senderName = "Rajesh Kumar",
                    content = "Hi! I'm ready for our session tomorrow. Do you have any specific topics you'd like to focus on?",
                    timestamp = "2024-01-15T10:30:00Z"
                )
            ),
            "room2" to listOf(
                ChatMessage(
                    id = "msg2_1",
                    roomId = "room2",
                    senderId = "mentor2",
                    senderName = "Priya Sharma",
                    content = "Great session today! Keep practicing those quantitative problems.",
                    timestamp = "2024-01-14T15:30:00Z"
                ),
                ChatMessage(
                    id = "msg2_2",
                    roomId = "room2",
                    senderId = "current_user",
                    senderName = "You",
                    content = "Thank you for the session! The quantitative aptitude tips were really helpful.",
                    timestamp = "2024-01-14T16:20:00Z"
                )
            )
        )

        _chatRooms.value = mockRooms
        _messages.value = mockMessages
    }

    suspend fun getChatRooms(): List<ChatRoom> {
        delay(500)
        return _chatRooms.value
    }

    suspend fun getChatRoom(roomId: String): ChatRoom? {
        delay(300)
        return _chatRooms.value.find { it.id == roomId }
    }

    suspend fun getMessages(roomId: String, page: Int = 1, pageSize: Int = 50): MessagesResponse {
        delay(400)
        val allMessages = _messages.value[roomId] ?: emptyList()
        val startIndex = (page - 1) * pageSize
        val endIndex = minOf(startIndex + pageSize, allMessages.size)
        
        val pageMessages = if (startIndex < allMessages.size) {
            allMessages.subList(startIndex, endIndex)
        } else {
            emptyList()
        }

        return MessagesResponse(
            messages = pageMessages,
            hasMore = endIndex < allMessages.size,
            nextPage = if (endIndex < allMessages.size) page + 1 else null
        )
    }

    suspend fun sendMessage(request: SendMessageRequest): ChatMessage {
        delay(200)
        
        val message = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            roomId = request.roomId,
            senderId = "current_user",
            senderName = "You",
            content = request.content,
            type = request.type,
            attachments = request.attachments,
            replyTo = request.replyTo,
            timestamp = "2024-01-15T${String.format("%02d:%02d:00Z", 
                (System.currentTimeMillis() / 1000 % 86400) / 3600,
                (System.currentTimeMillis() / 1000 % 3600) / 60)}"
        )

        // Update messages
        val currentMessages = _messages.value.toMutableMap()
        val roomMessages = currentMessages[request.roomId]?.toMutableList() ?: mutableListOf()
        roomMessages.add(message)
        currentMessages[request.roomId] = roomMessages
        _messages.value = currentMessages

        // Update chat room's last message
        val currentRooms = _chatRooms.value.toMutableList()
        val roomIndex = currentRooms.indexOfFirst { it.id == request.roomId }
        if (roomIndex != -1) {
            currentRooms[roomIndex] = currentRooms[roomIndex].copy(
                lastMessage = message,
                updatedAt = message.timestamp
            )
            _chatRooms.value = currentRooms
        }

        return message
    }

    suspend fun createChatRoom(request: CreateChatRoomRequest): ChatRoom {
        delay(600)
        
        val room = ChatRoom(
            id = "room_${System.currentTimeMillis()}",
            participants = request.participantIds + "current_user",
            participantDetails = emptyList(), // Would be populated from user data
            isGroup = request.isGroup,
            groupName = request.groupName,
            createdAt = "2024-01-15T12:00:00Z",
            updatedAt = "2024-01-15T12:00:00Z"
        )

        val currentRooms = _chatRooms.value.toMutableList()
        currentRooms.add(0, room) // Add to beginning
        _chatRooms.value = currentRooms

        return room
    }

    suspend fun markMessageAsRead(messageId: String, roomId: String) {
        delay(100)
        // Update read status - in real app, this would sync with backend
        
        // Update unread count
        val currentRooms = _chatRooms.value.toMutableList()
        val roomIndex = currentRooms.indexOfFirst { it.id == roomId }
        if (roomIndex != -1) {
            currentRooms[roomIndex] = currentRooms[roomIndex].copy(unreadCount = 0)
            _chatRooms.value = currentRooms
        }
    }

    suspend fun deleteMessage(messageId: String, roomId: String) {
        delay(200)
        
        val currentMessages = _messages.value.toMutableMap()
        val roomMessages = currentMessages[roomId]?.toMutableList()
        
        roomMessages?.let { messages ->
            val messageIndex = messages.indexOfFirst { it.id == messageId }
            if (messageIndex != -1) {
                messages[messageIndex] = messages[messageIndex].copy(
                    isDeleted = true,
                    content = "This message was deleted"
                )
                currentMessages[roomId] = messages
                _messages.value = currentMessages
            }
        }
    }

    suspend fun editMessage(messageId: String, roomId: String, newContent: String) {
        delay(200)
        
        val currentMessages = _messages.value.toMutableMap()
        val roomMessages = currentMessages[roomId]?.toMutableList()
        
        roomMessages?.let { messages ->
            val messageIndex = messages.indexOfFirst { it.id == messageId }
            if (messageIndex != -1) {
                messages[messageIndex] = messages[messageIndex].copy(
                    content = newContent,
                    isEdited = true,
                    editedAt = "2024-01-15T12:30:00Z"
                )
                currentMessages[roomId] = messages
                _messages.value = currentMessages
            }
        }
    }

    // Real-time features (would use Socket.IO in real app)
    fun startTyping(roomId: String) {
        val indicator = TypingIndicator(
            roomId = roomId,
            userId = "current_user",
            userName = "You",
            isTyping = true,
            timestamp = "2024-01-15T12:00:00Z"
        )
        
        val current = _typingIndicators.value.toMutableList()
        current.removeAll { it.roomId == roomId && it.userId == "current_user" }
        current.add(indicator)
        _typingIndicators.value = current
    }

    fun stopTyping(roomId: String) {
        val current = _typingIndicators.value.toMutableList()
        current.removeAll { it.roomId == roomId && it.userId == "current_user" }
        _typingIndicators.value = current
    }
}