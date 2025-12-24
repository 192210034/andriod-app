# Android Build Fixes - Implementation Tasks

## Task Breakdown

### Phase 1: Core Infrastructure Setup (CRITICAL - Day 1)

#### Task 1.1: Create Dependency Container
- **File**: `app/src/main/java/com/simats/aspirebridge/di/DependencyContainer.kt`
- **Priority**: P0 (Blocking)
- **Estimated Time**: 2 hours
- **Description**: Create central dependency management system to replace Hilt
- **Acceptance Criteria**:
  - [ ] DependencyContainer class created with lazy initialization
  - [ ] All repositories properly instantiated
  - [ ] API service creation handled
  - [ ] UserSessionManager integration
  - [ ] No compilation errors in container

#### Task 1.2: Update MentorshipApplication
- **File**: `app/src/main/java/com/simats/aspirebridge/MentorshipApplication.kt`
- **Priority**: P0 (Blocking)
- **Estimated Time**: 30 minutes
- **Description**: Remove Hilt annotations and add dependency container
- **Acceptance Criteria**:
  - [ ] `@HiltAndroidApp` annotation removed
  - [ ] DependencyContainer initialized in onCreate()
  - [ ] Global access to container provided
  - [ ] No Hilt imports remaining

#### Task 1.3: Create ViewModel Factory
- **File**: `app/src/main/java/com/simats/aspirebridge/ui/ViewModelFactory.kt`
- **Priority**: P0 (Blocking)
- **Estimated Time**: 1.5 hours
- **Description**: Replace Hilt's ViewModel injection with manual factory
- **Acceptance Criteria**:
  - [ ] ViewModelFactory class extends ViewModelProvider.Factory
  - [ ] All ViewModels handled in create() method
  - [ ] Proper dependency injection for each ViewModel
  - [ ] Type-safe ViewModel creation

#### Task 1.4: Test Core Infrastructure
- **Priority**: P0 (Blocking)
- **Estimated Time**: 30 minutes
- **Description**: Verify basic compilation and instantiation
- **Acceptance Criteria**:
  - [ ] App compiles without DI-related errors
  - [ ] DependencyContainer instantiates successfully
  - [ ] ViewModelFactory creates ViewModels without crashes

### Phase 2: Repository Layer Updates (HIGH - Day 2)

#### Task 2.1: Update JobRepository
- **File**: `app/src/main/java/com/simats/aspirebridge/data/repository/JobRepository.kt`
- **Priority**: P1 (High)
- **Estimated Time**: 45 minutes
- **Description**: Remove Hilt annotations and ensure all methods implemented
- **Acceptance Criteria**:
  - [ ] `@Singleton` and `@Inject` annotations removed
  - [ ] Constructor updated for manual dependency injection
  - [ ] All referenced methods implemented
  - [ ] Sample data provided for missing API calls

#### Task 2.2: Update SuccessStoryRepository
- **File**: `app/src/main/java/com/simats/aspirebridge/data/repository/SuccessStoryRepository.kt`
- **Priority**: P1 (High)
- **Estimated Time**: 1 hour
- **Description**: Remove Hilt annotations and implement missing methods
- **Acceptance Criteria**:
  - [ ] `@Singleton` and `@Inject` annotations removed
  - [ ] `getAllSuccessStories()` method implemented
  - [ ] `deleteSuccessStory()` method implemented
  - [ ] Sample data generation for testing
  - [ ] Category filtering methods working

#### Task 2.3: Update ResourceRepository
- **File**: `app/src/main/java/com/simats/aspirebridge/data/repository/ResourceRepository.kt`
- **Priority**: P1 (High)
- **Estimated Time**: 1 hour
- **Description**: Remove Hilt annotations and implement missing methods
- **Acceptance Criteria**:
  - [ ] `@Singleton` and `@Inject` annotations removed
  - [ ] `getAllResources()` method implemented
  - [ ] `deleteResource()` method implemented
  - [ ] Sample data generation for testing
  - [ ] Category filtering methods working

#### Task 2.4: Update ExamRepository
- **File**: `app/src/main/java/com/simats/aspirebridge/data/repository/ExamRepository.kt`
- **Priority**: P1 (High)
- **Estimated Time**: 30 minutes
- **Description**: Remove Hilt annotations and verify implementation
- **Acceptance Criteria**:
  - [ ] `@Singleton` and `@Inject` annotations removed
  - [ ] Constructor updated for manual DI
  - [ ] All exam data methods working

#### Task 2.5: Fix NetworkModule
- **File**: `app/src/main/java/com/simats/aspirebridge/di/NetworkModule.kt`
- **Priority**: P1 (High)
- **Estimated Time**: 45 minutes
- **Description**: Remove Hilt annotations or integrate into DependencyContainer
- **Acceptance Criteria**:
  - [ ] All Hilt annotations removed
  - [ ] API service creation moved to DependencyContainer
  - [ ] Retrofit configuration preserved
  - [ ] No compilation errors

### Phase 3: ViewModel Layer Updates (HIGH - Day 3)

#### Task 3.1: Update HomeViewModel
- **File**: `app/src/main/java/com/simats/aspirebridge/ui/home/HomeViewModel.kt`
- **Priority**: P1 (High)
- **Estimated Time**: 30 minutes
- **Description**: Remove Hilt annotations and update constructor
- **Acceptance Criteria**:
  - [ ] `@HiltViewModel` annotation removed
  - [ ] `@Inject` removed from constructor
  - [ ] Dependencies passed via constructor parameters
  - [ ] All functionality preserved

#### Task 3.2: Update Admin ViewModels
- **Files**: 
  - `AdminDashboardViewModel.kt`
  - `AdminContentManagementViewModel.kt`
  - `AdminUserManagementViewModel.kt`
- **Priority**: P1 (High)
- **Estimated Time**: 1 hour
- **Description**: Remove Hilt annotations from all admin ViewModels
- **Acceptance Criteria**:
  - [ ] All `@HiltViewModel` annotations removed
  - [ ] All `@Inject` annotations removed
  - [ ] Constructor parameters updated
  - [ ] ViewModelFactory handles all admin ViewModels

#### Task 3.3: Update Story & Resource ViewModels
- **Files**:
  - `SuccessStoriesViewModel.kt`
  - `ResourceHubViewModel.kt`
- **Priority**: P1 (High)
- **Estimated Time**: 45 minutes
- **Description**: Remove Hilt annotations and update dependencies
- **Acceptance Criteria**:
  - [ ] Hilt annotations removed
  - [ ] Repository dependencies properly injected
  - [ ] ViewModelFactory integration complete

#### Task 3.4: Update Remaining ViewModels
- **Files**: All other ViewModel files in the project
- **Priority**: P2 (Medium)
- **Estimated Time**: 1 hour
- **Description**: Systematically remove Hilt from all ViewModels
- **Acceptance Criteria**:
  - [ ] No `@HiltViewModel` annotations remain
  - [ ] No `@Inject` annotations in constructors
  - [ ] All ViewModels handled by factory

### Phase 4: UI Layer Updates (MEDIUM - Day 4)

#### Task 4.1: Update Core Fragments
- **Files**:
  - `HomeFragment.kt`
  - `LoginFragment.kt`
  - `MainActivity.kt`
- **Priority**: P2 (Medium)
- **Estimated Time**: 1.5 hours
- **Description**: Remove Hilt annotations and update ViewModel initialization
- **Acceptance Criteria**:
  - [ ] `@AndroidEntryPoint` annotations removed
  - [ ] ViewModel initialization uses ViewModelFactory
  - [ ] No Hilt imports remaining
  - [ ] Fragments load without crashes

#### Task 4.2: Update Admin Fragments
- **Files**:
  - `AdminLoginFragment.kt`
  - `AdminDashboardFragment.kt`
  - `AdminContentManagementFragment.kt`
  - `AdminUserManagementFragment.kt`
- **Priority**: P2 (Medium)
- **Estimated Time**: 2 hours
- **Description**: Remove Hilt annotations and fix navigation
- **Acceptance Criteria**:
  - [ ] All `@AndroidEntryPoint` annotations removed
  - [ ] ViewModel initialization updated
  - [ ] Navigation arguments handled manually
  - [ ] Admin flow works end-to-end

#### Task 4.3: Update Auth Fragments
- **Files**: All fragments in `ui/auth/` package
- **Priority**: P2 (Medium)
- **Estimated Time**: 1 hour
- **Description**: Remove Hilt annotations from authentication flow
- **Acceptance Criteria**:
  - [ ] All auth fragments updated
  - [ ] ViewModel factories used
  - [ ] Navigation between auth screens works
  - [ ] User type selection preserved

#### Task 4.4: Update Remaining Fragments
- **Files**: All other Fragment files
- **Priority**: P3 (Low)
- **Estimated Time**: 1.5 hours
- **Description**: Complete Hilt removal from all UI components
- **Acceptance Criteria**:
  - [ ] No `@AndroidEntryPoint` annotations remain
  - [ ] All fragments use ViewModelFactory
  - [ ] Navigation works correctly

### Phase 5: Navigation Fixes (MEDIUM - Day 4)

#### Task 5.1: Replace Safe Args in Admin Navigation
- **Files**: Admin fragments with navigation
- **Priority**: P2 (Medium)
- **Estimated Time**: 1 hour
- **Description**: Replace Safe Args with manual Bundle navigation
- **Acceptance Criteria**:
  - [ ] AdminContentManagementFragmentArgs replaced
  - [ ] AdminUserManagementFragmentArgs replaced
  - [ ] Manual Bundle creation implemented
  - [ ] Navigation arguments passed correctly

#### Task 5.2: Replace Safe Args in Main Navigation
- **Files**: Main app fragments with navigation
- **Priority**: P2 (Medium)
- **Estimated Time**: 1 hour
- **Description**: Replace remaining Safe Args usage
- **Acceptance Criteria**:
  - [ ] ResourceHubFragmentDirections replaced
  - [ ] SuccessStoriesFragmentDirections replaced
  - [ ] Job navigation arguments handled manually
  - [ ] All navigation flows work

#### Task 5.3: Create Navigation Utilities
- **File**: `app/src/main/java/com/simats/aspirebridge/utils/NavigationUtils.kt`
- **Priority**: P3 (Low)
- **Estimated Time**: 45 minutes
- **Description**: Create helper functions for common navigation patterns
- **Acceptance Criteria**:
  - [ ] Utility functions for common navigations
  - [ ] Type-safe argument handling
  - [ ] Consistent navigation patterns
  - [ ] Reduced code duplication

### Phase 6: Integration & Testing (CRITICAL - Day 5)

#### Task 6.1: Full Compilation Test
- **Priority**: P0 (Blocking)
- **Estimated Time**: 1 hour
- **Description**: Verify complete app compilation
- **Acceptance Criteria**:
  - [ ] App builds successfully in Android Studio
  - [ ] No compilation errors in any files
  - [ ] APK generates without issues
  - [ ] All resources compile correctly

#### Task 6.2: Runtime Testing
- **Priority**: P0 (Blocking)
- **Estimated Time**: 2 hours
- **Description**: Test app launch and basic functionality
- **Acceptance Criteria**:
  - [ ] App launches without crashes
  - [ ] Splash screen displays
  - [ ] Login screen loads
  - [ ] Basic navigation works
  - [ ] No immediate runtime exceptions

#### Task 6.3: Feature Validation
- **Priority**: P1 (High)
- **Estimated Time**: 2 hours
- **Description**: Test all major features
- **Acceptance Criteria**:
  - [ ] Authentication flow completes
  - [ ] Role-based dashboards display
  - [ ] Job exploration navigates correctly
  - [ ] Success stories load
  - [ ] Admin login works
  - [ ] Admin dashboard accessible

#### Task 6.4: Bug Fixes & Polish
- **Priority**: P2 (Medium)
- **Estimated Time**: 2 hours
- **Description**: Fix any issues found during testing
- **Acceptance Criteria**:
  - [ ] All critical bugs fixed
  - [ ] Navigation flows smooth
  - [ ] Error handling works
  - [ ] Performance acceptable

## Implementation Checklist

### Pre-Implementation
- [ ] Backup current codebase
- [ ] Create feature branch for fixes
- [ ] Document current error state
- [ ] Set up testing environment

### Phase 1 Completion Criteria
- [ ] DependencyContainer created and functional
- [ ] MentorshipApplication updated
- [ ] ViewModelFactory implemented
- [ ] Basic compilation successful

### Phase 2 Completion Criteria
- [ ] All repositories updated
- [ ] NetworkModule fixed
- [ ] Missing methods implemented
- [ ] Repository layer compiles

### Phase 3 Completion Criteria
- [ ] All ViewModels updated
- [ ] Hilt annotations removed
- [ ] ViewModelFactory handles all cases
- [ ] ViewModel layer compiles

### Phase 4 Completion Criteria
- [ ] All UI components updated
- [ ] Navigation arguments handled
- [ ] Fragment initialization works
- [ ] UI layer compiles

### Phase 5 Completion Criteria
- [ ] Safe Args completely replaced
- [ ] Manual navigation implemented
- [ ] All navigation flows work
- [ ] Navigation utilities created

### Phase 6 Completion Criteria
- [ ] Full app compilation successful
- [ ] Runtime testing passed
- [ ] All major features validated
- [ ] Critical bugs resolved

## Risk Management

### High-Risk Tasks
- **Task 1.1**: DependencyContainer creation - Complex dependency graph
- **Task 4.2**: Admin fragments - Complex navigation with arguments
- **Task 6.1**: Full compilation - May reveal hidden dependencies

### Mitigation Strategies
- **Incremental Testing**: Test after each phase
- **Rollback Plan**: Keep backup of working state
- **Parallel Development**: Work on independent components simultaneously
- **Documentation**: Document all changes for future reference

## Success Metrics
- **Compilation Success**: 0 build errors
- **Runtime Stability**: App launches without crashes
- **Feature Preservation**: All major features functional
- **Navigation Integrity**: All screen transitions work
- **Performance**: No significant performance degradation

This task breakdown provides a systematic approach to fixing all build issues while maintaining feature integrity and minimizing risk.