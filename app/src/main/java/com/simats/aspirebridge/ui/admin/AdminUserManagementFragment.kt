package com.simats.aspirebridge.ui.admin

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.aspirebridge.MentorshipApplication
import com.simats.aspirebridge.R
import com.simats.aspirebridge.data.model.User
import com.simats.aspirebridge.databinding.FragmentAdminUserManagementBinding
import com.simats.aspirebridge.ui.ViewModelFactory
import kotlinx.coroutines.launch

/**
 * Fragment for admin user management
 */
class AdminUserManagementFragment : Fragment() {

    private var _binding: FragmentAdminUserManagementBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AdminUserManagementViewModel
    private lateinit var userAdapter: AdminUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminUserManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize ViewModel with factory
        val dependencyContainer = (requireActivity().application as MentorshipApplication).container
        val factory = ViewModelFactory(dependencyContainer)
        viewModel = ViewModelProvider(this, factory)[AdminUserManagementViewModel::class.java]
        
        setupUI()
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
        handleArguments()
    }

    private fun setupUI() {
        // Setup search functionality
        binding.btnSearch.setOnClickListener {
            // TODO: Implement search view toggle
            showSearchDialog()
        }
    }

    private fun setupRecyclerView() {
        userAdapter = AdminUserAdapter(
            onViewProfileClick = { user ->
                // TODO: Navigate to user profile detail
                showUserProfileDialog(user)
            },
            onSendMessageClick = { user ->
                // TODO: Navigate to chat with user
                showMessageDialog(user)
            },
            onBanUserClick = { user ->
                showBanUserConfirmation(user)
            }
        )

        binding.recyclerUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // Filter buttons
        binding.btnFilterAll.setOnClickListener {
            viewModel.setFilter(UserFilter.ALL)
            updateFilterButtons(UserFilter.ALL)
        }

        binding.btnFilterAspirants.setOnClickListener {
            viewModel.setFilter(UserFilter.ASPIRANTS)
            updateFilterButtons(UserFilter.ASPIRANTS)
        }

        binding.btnFilterAchievers.setOnClickListener {
            viewModel.setFilter(UserFilter.ACHIEVERS)
            updateFilterButtons(UserFilter.ACHIEVERS)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filteredUsers.collect { users ->
                userAdapter.submitList(users)
                binding.layoutEmptyState.isVisible = users.isEmpty() && !viewModel.isLoading.value
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userStats.collect { stats ->
                binding.textTotalUsers.text = stats.totalUsers.toString()
                binding.textAspirants.text = stats.aspirantsCount.toString()
                binding.textAchievers.text = stats.achieversCount.toString()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressLoading.isVisible = isLoading
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    showErrorDialog(it)
                    viewModel.clearError()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentFilter.collect { filter ->
                updateFilterButtons(filter)
            }
        }
    }

    private fun handleArguments() {
        // Handle user type filter from navigation arguments
        val userType = arguments?.getString("userType")
        when (userType) {
            "ASPIRANT" -> {
                viewModel.setFilter(UserFilter.ASPIRANTS)
                binding.textTitle.text = "Aspirant Management"
            }
            "ACHIEVER" -> {
                viewModel.setFilter(UserFilter.ACHIEVERS)
                binding.textTitle.text = "Achiever Management"
            }
            else -> {
                viewModel.setFilter(UserFilter.ALL)
                binding.textTitle.text = "User Management"
            }
        }
    }

    private fun updateFilterButtons(currentFilter: UserFilter) {
        // Reset all button colors
        binding.btnFilterAll.setTextColor(resources.getColor(R.color.text_secondary, null))
        binding.btnFilterAspirants.setTextColor(resources.getColor(R.color.text_secondary, null))
        binding.btnFilterAchievers.setTextColor(resources.getColor(R.color.text_secondary, null))

        // Highlight active filter
        when (currentFilter) {
            UserFilter.ALL -> {
                binding.btnFilterAll.setTextColor(resources.getColor(R.color.error, null))
            }
            UserFilter.ASPIRANTS -> {
                binding.btnFilterAspirants.setTextColor(resources.getColor(R.color.aspirant_primary, null))
            }
            UserFilter.ACHIEVERS -> {
                binding.btnFilterAchievers.setTextColor(resources.getColor(R.color.achiever_primary, null))
            }
        }
    }

    private fun showSearchDialog() {
        val searchView = SearchView(requireContext())
        searchView.queryHint = "Search users by name or email"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.setSearchQuery(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText ?: "")
                return true
            }
        })

        AlertDialog.Builder(requireContext())
            .setTitle("Search Users")
            .setView(searchView)
            .setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showUserProfileDialog(user: User) {
        val message = buildString {
            append("Name: ${user.name}\n")
            append("Email: ${user.email}\n")
            append("Type: ${user.userType.name}\n")
            append("Sessions: ${user.totalSessions}\n")
            if (user.rating > 0) {
                append("Rating: ${String.format("%.1f", user.rating)}\n")
            }
            append("Bio: ${user.bio}")
        }

        AlertDialog.Builder(requireContext())
            .setTitle("User Profile")
            .setMessage(message)
            .setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showMessageDialog(user: User) {
        AlertDialog.Builder(requireContext())
            .setTitle("Send Message")
            .setMessage("Send a message to ${user.name}?")
            .setPositiveButton("Send") { dialog, _ ->
                // TODO: Implement messaging functionality
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showBanUserConfirmation(user: User) {
        AlertDialog.Builder(requireContext())
            .setTitle("Ban User")
            .setMessage("Are you sure you want to ban ${user.name}? This action cannot be undone.")
            .setPositiveButton("Ban") { dialog, _ ->
                viewModel.banUser(user)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}