package com.simats.aspirebridge.ui.resources

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
import com.simats.aspirebridge.data.model.ExamCategory
import com.simats.aspirebridge.data.model.Resource
import com.simats.aspirebridge.data.model.ResourceFilter
import com.simats.aspirebridge.data.model.ResourceType
import com.simats.aspirebridge.databinding.FragmentResourceHubBinding
import com.simats.aspirebridge.ui.ViewModelFactory
import kotlinx.coroutines.launch

/**
 * Fragment displaying resource hub with category and type filtering
 */
class ResourceHubFragment : Fragment() {
    
    private var _binding: FragmentResourceHubBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: ResourceHubViewModel
    private lateinit var resourcesAdapter: ResourcesAdapter
    
    private var examCategories: List<ExamCategory> = emptyList()
    private var currentFilter = ResourceFilter()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResourceHubBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize ViewModel with factory
        val dependencyContainer = (requireActivity().application as MentorshipApplication).container
        val factory = ViewModelFactory(dependencyContainer)
        viewModel = ViewModelProvider(this, factory)[ResourceHubViewModel::class.java]
        
        setupUI()
        setupClickListeners()
        observeViewModel()
        loadData()
    }
    
    private fun setupUI() {
        // Setup RecyclerView
        resourcesAdapter = ResourcesAdapter(
            onResourceClick = { resource ->
                // TODO: Navigate to resource detail
                // val bundle = Bundle().apply {
                //     putString("resourceId", resource.id)
                // }
                // findNavController().navigate(R.id.action_resource_hub_to_resource_detail, bundle)
            },
            onDownloadClick = { resource ->
                viewModel.downloadResource(resource.id)
            },
            onBookmarkClick = { resource ->
                viewModel.bookmarkResource(resource.id)
            },
            onUploaderClick = { uploaderId ->
                // Navigate to uploader profile
                // TODO: Implement navigation to user profile
            }
        )
        
        binding.recyclerResources.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = resourcesAdapter
        }
        
        // Setup SwipeRefreshLayout
        binding.swipeRefresh.setOnRefreshListener {
            refreshData()
        }
        
        // Setup filter chips
        setupFilterChips()
    }
    
    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.btnSearch.setOnClickListener {
            // TODO: Implement search functionality
        }
        
        binding.fabUploadResource.setOnClickListener {
            // TODO: Navigate to upload resource screen
            // findNavController().navigate(R.id.action_resource_hub_to_upload_resource)
        }
        
        // Setup tab selection listener
        binding.tabLayoutCategories.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val category = if (it.position == 0) null else examCategories[it.position - 1]
                    currentFilter = currentFilter.copy(examCategory = category)
                    applyFilter()
                }
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    
    private fun setupFilterChips() {
        binding.chipPdf.setOnCheckedChangeListener { _, isChecked ->
            updateResourceTypeFilter(ResourceType.PDF, isChecked)
        }
        
        binding.chipVideo.setOnCheckedChangeListener { _, isChecked ->
            updateResourceTypeFilter(ResourceType.VIDEO, isChecked)
        }
        
        binding.chipNotes.setOnCheckedChangeListener { _, isChecked ->
            updateResourceTypeFilter(ResourceType.NOTES, isChecked)
        }
        
        binding.chipPracticeTest.setOnCheckedChangeListener { _, isChecked ->
            updateResourceTypeFilter(ResourceType.PRACTICE_TEST, isChecked)
        }
        
        binding.chipStrategy.setOnCheckedChangeListener { _, isChecked ->
            updateResourceTypeFilter(ResourceType.STRATEGY, isChecked)
        }
    }
    
    private fun updateResourceTypeFilter(type: ResourceType, isSelected: Boolean) {
        currentFilter = if (isSelected) {
            currentFilter.copy(resourceType = type)
        } else {
            currentFilter.copy(resourceType = null)
        }
        applyFilter()
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.resources.collect { resources ->
                updateResourcesList(resources)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.examCategories.collect { categories ->
                examCategories = categories
                setupCategoryTabs(categories)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.swipeRefresh.isRefreshing = isLoading
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    // TODO: Show error message
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isAchiever.collect { isAchiever ->
                binding.fabUploadResource.visibility = if (isAchiever) View.VISIBLE else View.GONE
            }
        }
    }
    
    private fun loadData() {
        viewModel.loadExamCategories()
        viewModel.loadResources(currentFilter)
    }
    
    private fun refreshData() {
        viewModel.loadResources(currentFilter, forceRefresh = true)
    }
    
    private fun setupCategoryTabs(categories: List<ExamCategory>) {
        binding.tabLayoutCategories.removeAllTabs()
        
        // Add "All" tab
        binding.tabLayoutCategories.addTab(
            binding.tabLayoutCategories.newTab().setText("All")
        )
        
        // Add category tabs
        categories.forEach { category ->
            binding.tabLayoutCategories.addTab(
                binding.tabLayoutCategories.newTab().setText(category.name)
            )
        }
    }
    
    private fun updateResourcesList(resources: List<Resource>) {
        resourcesAdapter.submitList(resources)
        
        // Show/hide empty state
        if (resources.isEmpty()) {
            binding.layoutEmptyState.visibility = View.VISIBLE
            binding.recyclerResources.visibility = View.GONE
        } else {
            binding.layoutEmptyState.visibility = View.GONE
            binding.recyclerResources.visibility = View.VISIBLE
        }
    }
    
    private fun applyFilter() {
        viewModel.loadResources(currentFilter)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}