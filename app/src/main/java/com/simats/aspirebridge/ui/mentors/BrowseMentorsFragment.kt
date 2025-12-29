package com.simats.aspirebridge.ui.mentors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.simats.aspirebridge.R
import com.simats.aspirebridge.databinding.FragmentBrowseMentorsBinding
import com.simats.aspirebridge.di.DependencyContainer
import com.simats.aspirebridge.ui.ViewModelFactory

class BrowseMentorsFragment : Fragment() {

    private var _binding: FragmentBrowseMentorsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: BrowseMentorsViewModel
    private lateinit var mentorsAdapter: MentorsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrowseMentorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupUI()
        setupRecyclerView()
        setupObservers()
        setupListeners()

        // Load initial data
        viewModel.loadMentors()
    }

    private fun setupViewModel() {
        val dependencyContainer = DependencyContainer.getInstance(requireContext())
        val factory = ViewModelFactory(dependencyContainer)
        viewModel = ViewModelProvider(this, factory)[BrowseMentorsViewModel::class.java]
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        mentorsAdapter = MentorsAdapter(
            onMentorClick = { mentor ->
                // Navigate to mentor profile
                val action = BrowseMentorsFragmentDirections
                    .actionBrowseMentorsToMentorProfile(mentor.id)
                findNavController().navigate(action)
            },
            onBookSessionClick = { mentor ->
                // Navigate to booking flow
                val action = BrowseMentorsFragmentDirections
                    .actionBrowseMentorsToBookSession(mentor.id)
                findNavController().navigate(action)
            },
            onFollowClick = { mentor ->
                viewModel.toggleFollow(mentor.id)
            }
        )

        binding.rvMentors.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mentorsAdapter
        }
    }

    private fun setupObservers() {
        viewModel.mentors.observe(viewLifecycleOwner) { mentors ->
            mentorsAdapter.submitList(mentors)
            updateResultsCount(mentors.size)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.layoutLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.rvMentors.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.isEmpty.observe(viewLifecycleOwner) { isEmpty ->
            binding.layoutEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
            binding.rvMentors.visibility = if (isEmpty) View.GONE else View.VISIBLE
        }

        viewModel.selectedFilters.observe(viewLifecycleOwner) { filters ->
            updateFilterChips(filters)
        }
    }

    private fun setupListeners() {
        // Search functionality
        binding.etSearch.addTextChangedListener { text ->
            viewModel.updateSearchQuery(text.toString())
        }

        // Filter chips
        binding.chipGroupFilters.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedFilters = mutableListOf<String>()
            checkedIds.forEach { chipId ->
                val chip = group.findViewById<Chip>(chipId)
                selectedFilters.add(chip.text.toString())
            }
            viewModel.updateFilters(selectedFilters)
        }

        // Sort buttons
        binding.btnSortRating.setOnClickListener {
            viewModel.sortBy("rating")
        }

        binding.btnSortPrice.setOnClickListener {
            viewModel.sortBy("price")
        }

        binding.btnSortExperience.setOnClickListener {
            viewModel.sortBy("experience")
        }

        // Advanced filters FAB
        binding.fabFilters.setOnClickListener {
            showAdvancedFiltersDialog()
        }
    }

    private fun updateResultsCount(count: Int) {
        binding.tvResultsCount.text = when (count) {
            0 -> "No mentors found"
            1 -> "Found 1 mentor"
            else -> "Found $count mentors"
        }
    }

    private fun updateFilterChips(filters: List<String>) {
        // Update chip selection state based on active filters
        binding.chipGroupFilters.clearCheck()
        
        if (filters.isEmpty() || filters.contains("All")) {
            binding.chipAll.isChecked = true
        } else {
            filters.forEach { filter ->
                when (filter) {
                    "UPSC" -> binding.chipUPSC.isChecked = true
                    "SSC" -> binding.chipSSC.isChecked = true
                    "Railways" -> binding.chipRailways.isChecked = true
                    "Banking" -> binding.chipBanking.isChecked = true
                    "Defence" -> binding.chipDefence.isChecked = true
                }
            }
        }
    }

    private fun showAdvancedFiltersDialog() {
        // TODO: Implement advanced filters dialog
        // This would include price range, rating range, experience range, etc.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}