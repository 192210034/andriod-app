package com.simats.aspirebridge.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.aspirebridge.databinding.FragmentAdminContentManagementBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Admin content management fragment for managing success stories and resources
 */
@AndroidEntryPoint
class AdminContentManagementFragment : Fragment() {
    
    private var _binding: FragmentAdminContentManagementBinding? = null
    private val binding get() = _binding!!
    
    private val args: AdminContentManagementFragmentArgs by navArgs()
    private val viewModel: AdminContentManagementViewModel by viewModels()
    private lateinit var contentAdapter: AdminContentAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminContentManagementBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
        loadData()
    }
    
    private fun setupUI() {
        val contentType = args.contentType
        binding.textTitle.text = when (contentType) {
            "STORIES" -> "Manage Success Stories"
            "RESOURCES" -> "Manage Resources"
            else -> "Content Management"
        }
    }
    
    private fun setupRecyclerView() {
        contentAdapter = AdminContentAdapter(
            onViewClick = { content ->
                // TODO: Navigate to content detail view
            },
            onEditClick = { content ->
                // TODO: Navigate to content edit screen
            },
            onDeleteClick = { content ->
                viewModel.deleteContent(content.id, args.contentType)
            }
        )
        
        binding.recyclerContent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = contentAdapter
        }
    }
    
    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.btnSearch.setOnClickListener {
            // TODO: Implement search functionality
        }
        
        binding.btnFilterAll.setOnClickListener {
            updateFilterButtons("ALL")
            viewModel.filterContent("ALL")
        }
        
        binding.btnFilterReported.setOnClickListener {
            updateFilterButtons("REPORTED")
            viewModel.filterContent("REPORTED")
        }
        
        binding.btnFilterRecent.setOnClickListener {
            updateFilterButtons("RECENT")
            viewModel.filterContent("RECENT")
        }
    }
    
    private fun updateFilterButtons(selectedFilter: String) {
        // Reset all button colors
        binding.btnFilterAll.setTextColor(resources.getColor(android.R.color.darker_gray, null))
        binding.btnFilterReported.setTextColor(resources.getColor(android.R.color.darker_gray, null))
        binding.btnFilterRecent.setTextColor(resources.getColor(android.R.color.darker_gray, null))
        
        // Highlight selected button
        val primaryColor = resources.getColor(com.simats.aspirebridge.R.color.error, null)
        when (selectedFilter) {
            "ALL" -> binding.btnFilterAll.setTextColor(primaryColor)
            "REPORTED" -> binding.btnFilterReported.setTextColor(primaryColor)
            "RECENT" -> binding.btnFilterRecent.setTextColor(primaryColor)
        }
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contentList.collect { contentList ->
                contentAdapter.submitList(contentList)
                
                // Show/hide empty state
                if (contentList.isEmpty()) {
                    binding.layoutEmptyState.visibility = View.VISIBLE
                    binding.recyclerContent.visibility = View.GONE
                } else {
                    binding.layoutEmptyState.visibility = View.GONE
                    binding.recyclerContent.visibility = View.VISIBLE
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
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
        viewModel.loadContent(args.contentType)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}