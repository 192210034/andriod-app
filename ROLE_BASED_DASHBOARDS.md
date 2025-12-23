# Role-Based Dashboards Implementation

## Overview

The AspireBridge app now supports role-based dashboards that display different content and functionality based on the user's type (Aspirant or Achiever).

## Implementation Details

### User Types
- **Aspirant**: Users preparing for government exams seeking mentorship
- **Achiever**: Users who have successfully cleared exams and can mentor others

### Dashboard Features

#### Aspirant Dashboard
- **Overview Section**: Sessions count and wallet balance
- **My Sessions**: Upcoming sessions with mentors
- **Bookings**: Quick access to find mentors and schedule sessions
- **My Wallet**: Balance, monthly spending, add funds, transaction history
- **Quick Access**: Explore Jobs, Success Stories, Resource Hub

#### Achiever Dashboard
- **Overview Section**: Sessions count and total earnings
- **Booking Requests**: Pending requests from aspirants with accept/decline actions
- **My Sessions**: Upcoming mentoring sessions
- **Availability**: Set and manage available time slots
- **Content Creation**: Post success stories and resources
- **My Wallet**: Available balance, monthly earnings, withdraw funds, earnings history

### Key Components

#### 1. Updated User Model
```kotlin
enum class UserType {
    ASPIRANT, ACHIEVER
}
```

#### 2. UserSessionManager
- Manages user authentication state
- Provides user type detection
- Handles session persistence
- Includes mock user functionality for testing

#### 3. Role-Based Layouts
- `fragment_aspirant_dashboard.xml`: Aspirant-specific dashboard
- `fragment_achiever_dashboard.xml`: Achiever-specific dashboard

#### 4. Enhanced HomeFragment
- Dynamic layout inflation based on user type
- Role-specific click handlers
- Separate data loading for each user type

#### 5. Enhanced HomeViewModel
- Role-specific data streams
- Aspirant-specific methods (wallet, sessions)
- Achiever-specific methods (bookings, earnings, availability)

#### 6. New Data Models
- `WalletModels.kt`: Wallet, Transaction, PaymentMethod
- `SessionModels.kt`: Session, BookingRequest, AvailabilitySlot

## Testing the Implementation

### To Test Aspirant Dashboard:
1. In `HomeFragment.kt`, change the mock user type:
```kotlin
userSessionManager.setMockUser(UserType.ASPIRANT)
```

### To Test Achiever Dashboard:
1. In `HomeFragment.kt`, change the mock user type:
```kotlin
userSessionManager.setMockUser(UserType.ACHIEVER)
```

### Dashboard Differences

| Feature | Aspirant Dashboard | Achiever Dashboard |
|---------|-------------------|-------------------|
| Header Color | Green (#10B981) | Purple (#8B5CF6) |
| Overview Stats | Sessions & Wallet Balance | Sessions & Earnings |
| Primary Actions | Find Mentor, Schedule | Accept Requests, Set Availability |
| Content Actions | View Stories/Resources | Post Stories/Resources |
| Wallet Actions | Add Funds, History | Withdraw, Earnings History |

## Future Enhancements

### Planned Features
1. **Wallet System**: Complete payment integration
2. **Booking System**: Request/accept/decline flow
3. **Availability Management**: Calendar-based time slot selection
4. **Content Creation**: Rich text editors for stories and resources
5. **Analytics Dashboard**: Performance metrics for achievers
6. **Notification System**: Real-time updates for bookings and payments

### Repository Integration
The following repositories need to be implemented:
- `SessionRepository`: Manage mentoring sessions
- `BookingRepository`: Handle booking requests
- `WalletRepository`: Manage payments and transactions
- `AvailabilityRepository`: Manage mentor availability

## Navigation Flow

### Aspirant Flow
```
Home Dashboard → Find Mentor → Book Session → My Sessions → Wallet
                ↓
            Success Stories → Resource Hub → Explore Jobs
```

### Achiever Flow
```
Home Dashboard → Booking Requests → Accept/Decline → My Sessions
                ↓
            Set Availability → Post Content → Wallet (Withdraw)
```

## Color Theming

### Aspirant Theme (Green)
- Primary: `#10B981`
- Secondary: `#059669`
- Light: `#D1FAE5`
- Background: `#ECFDF5`

### Achiever Theme (Purple)
- Primary: `#8B5CF6`
- Secondary: `#7C3AED`
- Light: `#EDE9FE`
- Background: `#F5F3FF`

## Error Handling

The implementation includes comprehensive error handling for:
- Network failures during data loading
- Invalid user sessions
- Failed booking operations
- Payment processing errors

## Performance Considerations

- Lazy loading of dashboard data
- Efficient state management with StateFlow
- Minimal UI updates based on user type
- Cached user session data

## Security Features

- Secure session management
- User type validation
- Protected achiever-only actions
- Transaction security measures