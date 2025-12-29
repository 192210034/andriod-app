package com.simats.aspirebridge.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.simats.aspirebridge.MentorshipApplication
import com.simats.aspirebridge.R
import com.simats.aspirebridge.data.model.UserType
import com.simats.aspirebridge.data.manager.UserSessionManager
import com.simats.aspirebridge.databinding.FragmentAspirantDashboardBinding
import com.simats.aspirebridge.databinding.FragmentAchieverDashboardBinding
import com.simats.aspirebridge.ui.ViewModelFactory
import kotlinx.coroutines.launch

/**
 * Home fragment showing role-based dashboard for Aspirants and Achievers
 */
class HomeFragment : Fragment() {
    
    private lateinit var userSessionManager: UserSessionManager
    
    private var _aspirantBinding: FragmentAspirantDashboardBinding? = null
    private var _achieverBinding: FragmentAchieverDashboardBinding? = null
    private val aspirantBinding get() = _aspirantBinding!!
    private val achieverBinding get() = _achieverBinding!!
    
    private lateinit var viewModel: HomeViewModel
    private lateinit var successStoriesAdapter: SuccessStoryPreviewAdapter
    private lateinit var mentorsAdapter: MentorPreviewAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize UserSessionManager
        userSessionManager = UserSessionManager(requireContext())
        
        // Set mock user for testing - In real app, this would be set during login
        // Change between UserType.ASPIRANT and UserType.ACHIEVER to test different dashboards
        userSessionManager.setMockUser(UserType.ASPIRANT)
        
        val currentUserType = userSessionManager.getCurrentUserType()
        
        // If admin user, redirect to admin dashboard
        if (currentUserType == UserType.ADMIN) {
            findNavController().navigate(R.id.action_home_to_admin_dashboard)
            return View(requireContext()) // Return empty view as we're navigating away
        }
        
        return when (currentUserType) {
            UserType.ASPIRANT -> {
                _aspirantBinding = FragmentAspirantDashboardBinding.inflate(inflater, container, false)
                aspirantBinding.root
            }
            UserType.ACHIEVER -> {
                _achieverBinding = FragmentAchieverDashboardBinding.inflate(inflater, container, false)
                achieverBinding.root
            }
            UserType.ADMIN -> {
                // This case is handled above, but keeping for completeness
                View(requireContext())
            }
        }
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize ViewModel with factory
        val dependencyContainer = (requireActivity().application as MentorshipApplication).container
        val factory = ViewModelFactory(dependencyContainer)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        
        val currentUserType = userSessionManager.getCurrentUserType()
        
        // Skip setup if admin (already navigated away)
        if (currentUserType == UserType.ADMIN) return
        
        when (currentUserType) {
            UserType.ASPIRANT -> setupAspirantDashboard()
            UserType.ACHIEVER -> setupAchieverDashboard()
            UserType.ADMIN -> { /* Already handled */ }
        }
        observeViewModel()
        loadData()
    }
    
    private fun setupAspirantDashboard() {
        val currentUser = userSessionManager.currentUser.value
        
        // Set greeting and user name
        aspirantBinding.textGreeting.text = getGreeting()
        aspirantBinding.textUserName.text = currentUser?.name ?: "Aspirant Dashboard"
        
        // Set stats
        aspirantBinding.textSessionsCount.text = currentUser?.totalSessions?.toString() ?: "5"
        aspirantBinding.textWalletBalance.text = "₹2,500"
        aspirantBinding.textCurrentBalance.text = "₹2,500"
        aspirantBinding.textMonthlySpent.text = "₹1,200"
        
        setupAspirantClickListeners()
    }
    
    private fun setupAchieverDashboard() {
        val currentUser = userSessionManager.currentUser.value
        
        // Set greeting and user name
        achieverBinding.textGreeting.text = getGreeting()
        achieverBinding.textUserName.text = currentUser?.name ?: "Achiever Dashboard"
        
        // Set stats
        achieverBinding.textSessionsCount.text = currentUser?.totalSessions?.toString() ?: "12"
        achieverBinding.textEarnings.text = "₹15,000"
        achieverBinding.textPendingRequests.text = "3"
        achieverBinding.textAvailableBalance.text = "₹15,000"
        achieverBinding.textMonthlyEarned.text = "₹8,500"
        
        setupAchieverClickListeners()
    }
    
    private fun setupAspirantClickListeners() {
        aspirantBinding.btnNotifications.setOnClickListener {
            // TODO: Navigate to notifications screen
            // findNavController().navigate(R.id.action_home_to_notifications)
        }
        
        aspirantBinding.btnViewAllSessions.setOnClickListener {
            findNavController().navigate(R.id.nav_sessions)
        }
        
        aspirantBinding.btnViewAllBookings.setOnClickListener {
            findNavController().navigate(R.id.nav_sessions)
        }
        
        aspirantBinding.btnFindMentor.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_browse_mentors)
        }
        
        aspirantBinding.btnScheduleSession.setOnClickListener {
            findNavController().navigate(R.id.nav_sessions)
        }
        
        aspirantBinding.btnAddMoney.setOnClickListener {
            // TODO: Navigate to add money screen
        }
        
        aspirantBinding.btnAddFunds.setOnClickListener {
            // TODO: Navigate to add funds screen
        }
        
        aspirantBinding.btnTransactionHistory.setOnClickListener {
            // TODO: Navigate to transaction history screen
        }
        
        aspirantBinding.btnExploreJobs.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_explore_jobs)
        }
        
        aspirantBinding.btnSuccessStories.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_success_stories)
        }
        
        aspirantBinding.btnResourceHub.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_resource_hub)
        }
        
        aspirantBinding.cardUpcomingSession.setOnClickListener {
            findNavController().navigate(R.id.nav_sessions)
        }
        
        aspirantBinding.cardWallet.setOnClickListener {
            // TODO: Navigate to wallet screen
        }
    }
    
    private fun setupAchieverClickListeners() {
        achieverBinding.btnNotifications.setOnClickListener {
            // TODO: Navigate to notifications screen
            // findNavController().navigate(R.id.action_home_to_notifications)
        }
        
        achieverBinding.btnViewAllSessions.setOnClickListener {
            findNavController().navigate(R.id.nav_sessions)
        }
        
        achieverBinding.btnManageAvailability.setOnClickListener {
            // TODO: Navigate to availability management screen
        }
        
        achieverBinding.btnSetAvailability.setOnClickListener {
            // TODO: Navigate to set availability screen
        }
        
        achieverBinding.btnPostSuccessStory.setOnClickListener {
            // TODO: Navigate to post success story screen
        }
        
        achieverBinding.btnPostResource.setOnClickListener {
            // TODO: Navigate to post resource screen
        }
        
        achieverBinding.btnWithdraw.setOnClickListener {
            // TODO: Navigate to withdraw screen
        }
        
        achieverBinding.btnWithdrawFunds.setOnClickListener {
            // TODO: Navigate to withdraw funds screen
        }
        
        achieverBinding.btnEarningsHistory.setOnClickListener {
            // TODO: Navigate to earnings history screen
        }
        
        achieverBinding.btnAcceptRequest.setOnClickListener {
            // TODO: Handle accept booking request
            viewModel.acceptBookingRequest("mock_request_id")
        }
        
        achieverBinding.btnDeclineRequest.setOnClickListener {
            // TODO: Handle decline booking request
            viewModel.declineBookingRequest("mock_request_id", "Schedule conflict")
        }
        
        achieverBinding.cardBookingRequests.setOnClickListener {
            // TODO: Navigate to booking requests screen
        }
        
        achieverBinding.cardUpcomingSession.setOnClickListener {
            findNavController().navigate(R.id.nav_sessions)
        }
        
        achieverBinding.cardAvailability.setOnClickListener {
            // TODO: Navigate to availability management screen
        }
        
        achieverBinding.cardWallet.setOnClickListener {
            // TODO: Navigate to wallet screen
        }
    }
    
    private fun observeViewModel() {
        val currentUserId = userSessionManager.getCurrentUserId() ?: "mock_user_id"
        val currentUserType = userSessionManager.getCurrentUserType()
        
        // Load role-specific data
        when (currentUserType) {
            UserType.ASPIRANT -> {
                viewModel.loadAspirantDashboardData(currentUserId)
            }
            UserType.ACHIEVER -> {
                viewModel.loadAchieverDashboardData(currentUserId)
            }
            UserType.ADMIN -> {
                // Admin users don't need dashboard data
            }
        }
        
        // Observe common data
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recentSuccessStories.collect { _ ->
                // Success stories are shown in both dashboards through quick access
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.featuredMentors.collect { _ ->
                // Mentors are relevant for aspirants primarily
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.resourceCount.collect { _ ->
                // Resource count is shown in both dashboards
            }
        }
        
        // Observe role-specific data
        if (currentUserType == UserType.ASPIRANT) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.upcomingSessions.collect { _ ->
                    // Update upcoming sessions UI
                }
            }
            
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.walletBalance.collect { wallet ->
                    wallet?.let {
                        aspirantBinding.textCurrentBalance.text = "₹${it.balance.toInt()}"
                        aspirantBinding.textMonthlySpent.text = "₹${it.monthlySpent.toInt()}"
                    }
                }
            }
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.pendingBookingRequests.collect { requests ->
                    achieverBinding.textPendingRequests.text = requests.size.toString()
                }
            }
            
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.monthlyEarnings.collect { earnings ->
                    achieverBinding.textMonthlyEarned.text = "₹${earnings.toInt()}"
                }
            }
            
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.totalSessions.collect { count ->
                    achieverBinding.textSessionsCount.text = count.toString()
                }
            }
        }
        
        // Observe errors
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    // TODO: Show error message to user
                    viewModel.clearError()
                }
            }
        }
    }
    
    private fun loadData() {
        // Data loading is handled in observeViewModel based on user type
    }
    
    private fun getGreeting(): String {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "Good Morning!"
            in 12..16 -> "Good Afternoon!"
            else -> "Good Evening!"
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _aspirantBinding = null
        _achieverBinding = null
    }
}