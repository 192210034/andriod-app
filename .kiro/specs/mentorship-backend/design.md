# Backend API Design Document

## Overview

A RESTful API backend built with Node.js and Express.js, using MongoDB for data persistence. The API provides comprehensive endpoints for user authentication, profile management, mentor discovery, session scheduling, real-time messaging, and feedback systems. The backend is designed to be stateless, scalable, and secure.

## Architecture

### Technology Stack
- **Runtime**: Node.js with Express.js framework
- **Database**: MongoDB with Mongoose ODM
- **Authentication**: JWT (JSON Web Tokens)
- **Real-time**: Socket.io for WebSocket connections
- **File Upload**: Multer for image handling
- **Validation**: Joi for request validation
- **Security**: Helmet, CORS, rate limiting

### API Structure
```
/api
├── /auth          # Authentication endpoints
├── /users         # User profile management
├── /mentors       # Mentor discovery and search
├── /requests      # Mentorship request management
├── /sessions      # Session scheduling
├── /messages      # Messaging system
├── /feedback      # Rating and feedback
└── /uploads       # File upload handling
```

## Components and Interfaces

### Authentication Module
```javascript
// Auth Controller
class AuthController {
  async register(req, res) {
    // User registration logic
    // Hash password, create user, return JWT
  }
  
  async login(req, res) {
    // User login logic
    // Validate credentials, return JWT
  }
  
  async forgotPassword(req, res) {
    // Password reset logic
    // Generate reset token, send email
  }
}

// JWT Middleware
const authenticateToken = (req, res, next) => {
  // Verify JWT token from Authorization header
  // Add user info to req.user
}
```

### User Profile Module
```javascript
// User Model (Mongoose Schema)
const userSchema = new mongoose.Schema({
  name: { type: String, required: true },
  email: { type: String, required: true, unique: true },
  password: { type: String, required: true },
  profilePicture: { type: String },
  bio: { type: String },
  skills: [{ type: String }],
  userType: { type: String, enum: ['mentor', 'mentee', 'both'] },
  availability: { type: String, enum: ['available', 'busy', 'offline'] },
  rating: { type: Number, default: 0 },
  totalSessions: { type: Number, default: 0 },
  createdAt: { type: Date, default: Date.now }
});

// User Controller
class UserController {
  async getProfile(req, res) {
    // Get user profile by ID
  }
  
  async updateProfile(req, res) {
    // Update user profile data
  }
  
  async uploadAvatar(req, res) {
    // Handle profile picture upload
  }
}
```

### Mentor Discovery Module
```javascript
// Mentor Controller
class MentorController {
  async searchMentors(req, res) {
    // Search mentors with filters
    // Support pagination, sorting
  }
  
  async getMentorDetails(req, res) {
    // Get detailed mentor information
  }
  
  async getPopularMentors(req, res) {
    // Get top-rated mentors
  }
}

// Search Service
class SearchService {
  static buildSearchQuery(filters) {
    // Build MongoDB query from search filters
    // Handle skills, location, industry, rating filters
  }
}
```

### Request Management Module
```javascript
// Request Model
const requestSchema = new mongoose.Schema({
  menteeId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  mentorId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  message: { type: String, required: true },
  status: { type: String, enum: ['pending', 'accepted', 'declined'], default: 'pending' },
  createdAt: { type: Date, default: Date.now },
  respondedAt: { type: Date }
});

// Request Controller
class RequestController {
  async createRequest(req, res) {
    // Create mentorship request
    // Send notification to mentor
  }
  
  async acceptRequest(req, res) {
    // Accept request, create match
    // Enable communication features
  }
  
  async declineRequest(req, res) {
    // Decline request with optional reason
  }
}
```

### Session Management Module
```javascript
// Session Model
const sessionSchema = new mongoose.Schema({
  mentorId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  menteeId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  scheduledTime: { type: Date, required: true },
  duration: { type: Number, required: true }, // in minutes
  status: { type: String, enum: ['scheduled', 'completed', 'cancelled'], default: 'scheduled' },
  meetingLink: { type: String },
  notes: { type: String },
  createdAt: { type: Date, default: Date.now }
});

// Session Controller
class SessionController {
  async createSession(req, res) {
    // Create new session
    // Check for conflicts
  }
  
  async getUserSessions(req, res) {
    // Get all sessions for user
    // Support filtering by status, date range
  }
  
  async updateSession(req, res) {
    // Update session details
    // Handle rescheduling
  }
}
```

### Messaging Module
```javascript
// Message Model
const messageSchema = new mongoose.Schema({
  senderId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  receiverId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  chatId: { type: String, required: true }, // Unique chat identifier
  content: { type: String, required: true },
  messageType: { type: String, enum: ['text', 'file', 'link'], default: 'text' },
  timestamp: { type: Date, default: Date.now },
  delivered: { type: Boolean, default: false },
  read: { type: Boolean, default: false }
});

// Message Controller
class MessageController {
  async sendMessage(req, res) {
    // Send message
    // Emit via Socket.io for real-time delivery
  }
  
  async getMessages(req, res) {
    // Get message history for chat
    // Support pagination
  }
}

// Socket.io Integration
io.on('connection', (socket) => {
  socket.on('join-chat', (chatId) => {
    socket.join(chatId);
  });
  
  socket.on('send-message', (messageData) => {
    // Broadcast message to chat room
    io.to(messageData.chatId).emit('new-message', messageData);
  });
});
```

### Feedback Module
```javascript
// Feedback Model
const feedbackSchema = new mongoose.Schema({
  sessionId: { type: mongoose.Schema.Types.ObjectId, ref: 'Session', required: true },
  fromUserId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  toUserId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  rating: { type: Number, min: 1, max: 5, required: true },
  comment: { type: String },
  createdAt: { type: Date, default: Date.now }
});

// Feedback Controller
class FeedbackController {
  async submitFeedback(req, res) {
    // Submit feedback and rating
    // Update mentor's average rating
  }
  
  async getUserRatings(req, res) {
    // Get rating summary for user
  }
}
```

## Data Models

### MongoDB Collections

#### Users Collection
```javascript
{
  _id: ObjectId,
  name: String,
  email: String (unique),
  password: String (hashed),
  profilePicture: String (URL),
  bio: String,
  skills: [String],
  userType: String, // 'mentor', 'mentee', 'both'
  availability: String, // 'available', 'busy', 'offline'
  rating: Number,
  totalSessions: Number,
  createdAt: Date,
  updatedAt: Date
}
```

#### Requests Collection
```javascript
{
  _id: ObjectId,
  menteeId: ObjectId (ref: Users),
  mentorId: ObjectId (ref: Users),
  message: String,
  status: String, // 'pending', 'accepted', 'declined'
  createdAt: Date,
  respondedAt: Date
}
```

#### Sessions Collection
```javascript
{
  _id: ObjectId,
  mentorId: ObjectId (ref: Users),
  menteeId: ObjectId (ref: Users),
  scheduledTime: Date,
  duration: Number, // minutes
  status: String, // 'scheduled', 'completed', 'cancelled'
  meetingLink: String,
  notes: String,
  createdAt: Date,
  updatedAt: Date
}
```

#### Messages Collection
```javascript
{
  _id: ObjectId,
  senderId: ObjectId (ref: Users),
  receiverId: ObjectId (ref: Users),
  chatId: String,
  content: String,
  messageType: String, // 'text', 'file', 'link'
  timestamp: Date,
  delivered: Boolean,
  read: Boolean
}
```

#### Feedback Collection
```javascript
{
  _id: ObjectId,
  sessionId: ObjectId (ref: Sessions),
  fromUserId: ObjectId (ref: Users),
  toUserId: ObjectId (ref: Users),
  rating: Number, // 1-5
  comment: String,
  createdAt: Date
}
```

## API Endpoints

### Authentication Endpoints
```
POST /api/auth/register
POST /api/auth/login
POST /api/auth/forgot-password
POST /api/auth/reset-password
GET  /api/auth/verify-token
```

### User Profile Endpoints
```
GET    /api/users/:id
PUT    /api/users/:id
POST   /api/users/upload-avatar
GET    /api/users/:id/stats
```

### Mentor Discovery Endpoints
```
GET /api/mentors/search?q=:query&skills=:skills&location=:location
GET /api/mentors/:id
GET /api/mentors/popular
GET /api/mentors/categories
```

### Request Management Endpoints
```
POST   /api/requests
GET    /api/requests/user/:id
PUT    /api/requests/:id/accept
PUT    /api/requests/:id/decline
GET    /api/requests/:id
```

### Session Management Endpoints
```
POST   /api/sessions
GET    /api/sessions/user/:id
PUT    /api/sessions/:id
DELETE /api/sessions/:id
GET    /api/sessions/:id
```

### Messaging Endpoints
```
POST /api/messages
GET  /api/messages/chat/:chatId
GET  /api/messages/chats/:userId
PUT  /api/messages/:id/read
```

### Feedback Endpoints
```
POST /api/feedback
GET  /api/feedback/user/:id
GET  /api/feedback/session/:id
```

## Error Handling

### HTTP Status Codes
- **200**: Success
- **201**: Created
- **400**: Bad Request (validation errors)
- **401**: Unauthorized (invalid/missing token)
- **403**: Forbidden (insufficient permissions)
- **404**: Not Found
- **409**: Conflict (duplicate data)
- **429**: Too Many Requests (rate limiting)
- **500**: Internal Server Error

### Error Response Format
```javascript
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid input data",
    "details": [
      {
        "field": "email",
        "message": "Email is required"
      }
    ]
  }
}
```

## Security Implementation

### Authentication & Authorization
- JWT tokens with expiration
- Password hashing with bcrypt
- Protected routes middleware
- Role-based access control

### Input Validation & Sanitization
- Joi schema validation
- MongoDB injection prevention
- XSS protection
- File upload validation

### Rate Limiting & Monitoring
- Express rate limiting
- Request logging
- Error tracking
- Performance monitoring

## Testing Strategy

### API Testing Approach
- **Unit Tests**: Individual controller and service functions
- **Integration Tests**: Full API endpoint testing with test database
- **Property Tests**: API contract validation with generated data

### Testing Tools
- **Jest**: Testing framework
- **Supertest**: HTTP assertion library
- **MongoDB Memory Server**: In-memory database for testing
- **Fast-check**: Property-based testing library

### Test Coverage Requirements
- Minimum 80% code coverage
- All API endpoints tested
- Error scenarios validated
- Authentication flows verified