package com.simats.aspirebridge.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.simats.aspirebridge.MentorshipApplication
import com.simats.aspirebridge.R
import com.simats.aspirebridge.data.manager.UserSessionManager
import com.simats.aspirebridge.databinding.FragmentAdminDashboardBinding
import com.simats.aspirebridge.ui.ViewModelFactory
import kotlinx.coroutines.launch

/**
 * Admin dashboard fragment for platform management
 */
class AdminDashboardFragment : Fragment() {
    
    private lateinit var userSessionManager: UserSessionManager
    
    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: AdminDashboardViewModel
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize dependencies
        val dependencyContainer = (requireActivity().application as MentorshipApplication).container
        userSessionManager = dependencyContainer.userSessionManager
        val factory = ViewModelFactory(dependencyContainer)
        viewModel = ViewModelProvider(this, factory)[AdminDashboardViewModel::class.java]
        
        setupUI()
        setupClickListeners()
        observeViewModel()
        loadData()
    }
    
    private fun setupUI() {
        val currentUser = userSessionManager.currentUser.value
        binding.textAdminName.text = currentUser?.name ?: "Platform Administrator"
        
        // Set initial statistics (will be updated by ViewModel)
        binding.textTotalUsers.text = "1,250"
        binding.textActiveSessions.text = "45"
        binding.textAspirantsCount.text = "950"
        binding.textAchieversCount.text = "300"
        binding.textStoriesCount.text = "156"
        binding.textResourcesCount.text = "423"
    }
    
    private fun setupClickListeners() {
        binding.btnLogout.setOnClickListener {
            userSessionManager.logout()
            findNavController().navigate(R.id.action_admin_dashboard_to_login)
        }
        
        binding.btnViewAllUsers.setOnClickListener {
            findNavController().navigate(R.id.action_admin_dashboard_to_user_management)
        }
        
        binding.btnManageAspirants.setOnClickListener {
            val bundle = Bundle().apply {
                putString("userType", "ASPIRANT")
            }
            findNavController().navigate(R.id.action_admin_dashboard_to_user_management, bundle)
        }
        
        binding.btnManageAchievers.setOnClickListener {
            val bundle = Bundle().apply {
                putString("userType", "ACHIEVER")
            }
            findNavController().navigate(R.id.action_admin_dashboard_to_user_management, bundle)
        }
        
        binding.cardSuccessStories.setOnClickListener {
            val bundle = Bundle().apply {
                putString("contentType", "SUCCESS_STORIES")
            }
            findNavController().navigate(R.id.action_admin_dashboard_to_content_management_stories, bundle)
        }
        
        binding.btnManageStories.setOnClickListener {
            val bundle = Bundle().apply {
                putString("contentType", "SUCCESS_STORIES")
            }
            findNavController().navigate(R.id.action_admin_dashboard_to_content_management_stories, bundle)
        }
        
        binding.cardResources.setOnClickListener {
            val bundle = Bundle().apply {
                putString("contentType", "RESOURCES")
            }
            findNavController().navigate(R.id.action_admin_dashboard_to_content_management_resources, bundle)
        }
        
        binding.btnManageResources.setOnClickListener {
            val bundle = Bundle().apply {
                putString("contentType", "RESOURCES")
            }
            findNavController().navigate(R.id.action_admin_dashboard_to_content_management_resources, bundle)
        }
        
        binding.btnPlatformSettings.setOnClickListener {
            // TODO: Navigate to platform settings
        }
        
        binding.btnReports.setOnClickListener {
            // TODO: Navigate to reports screen
        }
        
        binding.cardUserManagement.setOnClickListener {
            findNavController().navigate(R.id.action_admin_dashboard_to_user_management)
        }
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.platformStats.collect { stats ->
                binding.textTotalUsers.text = stats.totalUsers.toString()
                binding.textActiveSessions.text = stats.activeSessions.toString()
                binding.textAspirantsCount.text = stats.aspirantsCount.toString()
                binding.textAchieversCount.text = stats.achieversCount.toString()
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contentStats.collect { stats ->
                binding.textStoriesCount.text = stats.successStoriesCount.toString()
                binding.textResourcesCount.text = stats.resourcesCount.toString()
            }
        }
        
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
        viewModel.loadPlatformStatistics()
        viewModel.loadContentStatistics()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}