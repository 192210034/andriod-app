package com.simats.aspirebridge.ui.mentors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.simats.aspirebridge.R
import com.simats.aspirebridge.databinding.FragmentMentorProfileBinding
import com.simats.aspirebridge.di.DependencyContainer
import com.simats.aspirebridge.ui.ViewModelFactory

class MentorProfileFragment : Fragment() {

    private var _binding: FragmentMentorProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MentorProfileViewModel
    private lateinit var availabilityAdapter: AvailabilityAdapter
    private lateinit var reviewsAdapter: ReviewsAdapter

    private val args: MentorProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMentorProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupUI()
        setupRecyclerViews()
        setupObservers()
        setupListeners()

        // Load mentor data
        viewModel.loadMentor(args.mentorId)
    }

    private fun setupViewModel() {
        val dependencyContainer = DependencyContainer.getInstance(requireContext())
        val factory = ViewModelFactory(dependencyContainer)
        viewModel = ViewModelProvider(this, factory)[MentorProfileViewModel::class.java]
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            handleMenuItemClick(menuItem)
        }
    }

    private fun setupRecyclerViews() {
        // Availability slots
        availabilityAdapter = AvailabilityAdapter { slot ->
            // Navigate to booking with selected slot
            val action = MentorProfileFragmentDirections
                .actionMentorProfileToBookSession(args.mentorId)
            findNavController().navigate(action)
        }

        binding.rvAvailableSlots.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = availabilityAdapter
        }

        // Reviews
        reviewsAdapter = ReviewsAdapter()
        binding.rvReviews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reviewsAdapter
        }
    }

    private fun setupObservers() {
        viewModel.mentor.observe(viewLifecycleOwner) { mentor ->
            mentor?.let { bindMentorData(it) }
        }

        viewModel.availableSlots.observe(viewLifecycleOwner) { slots ->
            availabilityAdapter.submitList(slots.take(5)) // Show only first 5 slots
        }

        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            reviewsAdapter.submitList(reviews.take(3)) // Show only first 3 reviews
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Handle loading state
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            // Handle error state
        }
    }

    private fun setupListeners() {
        // Follow button
        binding.btnFollow.setOnClickListener {
            viewModel.toggleFollow()
        }

        // Message button
        binding.btnMessage.setOnClickListener {
            // TODO: Navigate to chat
        }

        // Book session buttons
        binding.btnBookSession.setOnClickListener {
            navigateToBooking()
        }

        binding.btnBookNow.setOnClickListener {
            navigateToBooking()
        }

        // View all links
        binding.tvViewAllSlots.setOnClickListener {
            // TODO: Navigate to full availability view
        }

        binding.tvViewAllReviews.setOnClickListener {
            // TODO: Navigate to all reviews
        }
    }

    private fun bindMentorData(mentor: com.simats.aspirebridge.data.model.Mentor) {
        binding.apply {
            // Basic info
            tvMentorName.text = mentor.name
            tvExamRank.text = "${mentor.examCategory} ${mentor.examYear} • Rank ${mentor.rank}"
            tvBio.text = mentor.bio
            tvRating.text = mentor.rating.toString()
            tvReviewCount.text = "(${mentor.reviewsCount} reviews)"

            // Stats
            tvSessionsCompleted.text = mentor.sessionsCompleted.toString()
            tvStudentsHelped.text = mentor.studentsHelped.toString()
            tvExperience.text = "${mentor.experienceYears}+"
            tvResponseTime.text = "< 2h" // Mock data

            // Pricing
            tvPrice.text = "${mentor.hourlyRate}/hr"
            tvBottomPrice.text = "${mentor.hourlyRate}/hr"

            // Verification badge
            ivVerified.visibility = if (mentor.isVerified) View.VISIBLE else View.GONE

            // Follow button state
            updateFollowButton(mentor.isFollowing)

            // Expertise chips
            setupExpertiseChips(mentor.expertise)

            // Profile image (placeholder for now)
            // TODO: Load actual image using Glide
            ivMentorPhoto.setImageResource(R.drawable.ic_person)
        }
    }

    private fun updateFollowButton(isFollowing: Boolean) {
        binding.btnFollow.apply {
            if (isFollowing) {
                text = "Following"
                setIconResource(R.drawable.ic_check)
                setTextColor(context.getColor(R.color.success))
                iconTint = context.getColorStateList(R.color.success)
            } else {
                text = "Follow"
                setIconResource(R.drawable.ic_add)
                setTextColor(context.getColor(R.color.primary))
                iconTint = context.getColorStateList(R.color.primary)
            }
        }
    }

    private fun setupExpertiseChips(expertise: List<String>) {
        binding.chipGroupExpertise.removeAllViews()
        expertise.forEach { expertiseArea ->
            val chip = Chip(requireContext()).apply {
                text = expertiseArea
                isClickable = false
                isFocusable = false
            }
            binding.chipGroupExpertise.addView(chip)
        }
    }

    private fun navigateToBooking() {
        val action = MentorProfileFragmentDirections
            .actionMentorProfileToBookSession(args.mentorId)
        findNavController().navigate(action)
    }

    private fun handleMenuItemClick(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_share -> {
                // TODO: Share mentor profile
                true
            }
            R.id.action_report -> {
                // TODO: Report mentor
                true
            }
            R.id.action_block -> {
                // TODO: Block mentor
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}