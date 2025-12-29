package com.simats.aspirebridge.ui.stories

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
import com.simats.aspirebridge.data.model.SuccessStory
import com.simats.aspirebridge.data.model.SuccessStoryFilter
import com.simats.aspirebridge.databinding.FragmentSuccessStoriesBinding
import com.simats.aspirebridge.ui.ViewModelFactory
import kotlinx.coroutines.launch

/**
 * Fragment displaying success stories with category filtering
 */
class SuccessStoriesFragment : Fragment() {
    
    private var _binding: FragmentSuccessStoriesBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: SuccessStoriesViewModel
    private lateinit var storiesAdapter: SuccessStoriesAdapter
    
    private var examCategories: List<ExamCategory> = emptyList()
    private var currentFilter = SuccessStoryFilter()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuccessStoriesBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize ViewModel with factory
        val dependencyContainer = (requireActivity().application as MentorshipApplication).container
        val factory = ViewModelFactory(dependencyContainer)
        viewModel = ViewModelProvider(this, factory)[SuccessStoriesViewModel::class.java]
        
        setupUI()
        setupClickListeners()
        observeViewModel()
        loadData()
    }
    
    private fun setupUI() {
        // Setup RecyclerView
        storiesAdapter = SuccessStoriesAdapter(
            onStoryClick = { story ->
                // TODO: Navigate to story detail
                // val bundle = Bundle().apply {
                //     putString("storyId", story.id)
                // }
                // findNavController().navigate(R.id.action_success_stories_to_story_detail, bundle)
            },
            onLikeClick = { story ->
                viewModel.likeStory(story.id)
            },
            onCommentClick = { story ->
                // TODO: Navigate to story detail
                // val bundle = Bundle().apply {
                //     putString("storyId", story.id)
                // }
                // findNavController().navigate(R.id.action_success_stories_to_story_detail, bundle)
            },
            onShareClick = { story ->
                viewModel.shareStory(story.id)
                // TODO: Implement share functionality
            },
            onBookmarkClick = { story ->
                viewModel.bookmarkStory(story.id)
            },
            onAuthorClick = { authorId ->
                // Navigate to author profile
                // TODO: Implement navigation to user profile
            }
        )
        
        binding.recyclerStories.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = storiesAdapter
        }
        
        // Setup SwipeRefreshLayout
        binding.swipeRefresh.setOnRefreshListener {
            refreshData()
        }
        
        // Setup search functionality
        binding.editSearch.setOnEditorActionListener { _, _, _ ->
            performSearch()
            true
        }
    }
    
    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.btnSearch.setOnClickListener {
            toggleSearchBar()
        }
        
        binding.btnFilter.setOnClickListener {
            // TODO: Show filter dialog
        }
        
        binding.fabAddStory.setOnClickListener {
            // TODO: Navigate to create story screen
            // findNavController().navigate(R.id.action_success_stories_to_create_story)
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
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stories.collect { stories ->
                updateStoriesList(stories)
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
                binding.fabAddStory.visibility = if (isAchiever) View.VISIBLE else View.GONE
            }
        }
    }
    
    private fun loadData() {
        viewModel.loadExamCategories()
        viewModel.loadSuccessStories(currentFilter)
    }
    
    private fun refreshData() {
        viewModel.loadSuccessStories(currentFilter, forceRefresh = true)
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
    
    private fun updateStoriesList(stories: List<SuccessStory>) {
        storiesAdapter.submitList(stories)
        
        // Show/hide empty state
        if (stories.isEmpty()) {
            binding.layoutEmptyState.visibility = View.VISIBLE
            binding.recyclerStories.visibility = View.GONE
        } else {
            binding.layoutEmptyState.visibility = View.GONE
            binding.recyclerStories.visibility = View.VISIBLE
        }
    }
    
    private fun toggleSearchBar() {
        if (binding.searchCard.visibility == View.VISIBLE) {
            binding.searchCard.visibility = View.GONE
            binding.editSearch.text?.clear()
            currentFilter = currentFilter.copy(searchQuery = null)
            applyFilter()
        } else {
            binding.searchCard.visibility = View.VISIBLE
            binding.editSearch.requestFocus()
        }
    }
    
    private fun performSearch() {
        val query = binding.editSearch.text?.toString()?.trim()
        currentFilter = currentFilter.copy(searchQuery = query?.takeIf { it.isNotEmpty() })
        applyFilter()
    }
    
    private fun applyFilter() {
        viewModel.loadSuccessStories(currentFilter)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}