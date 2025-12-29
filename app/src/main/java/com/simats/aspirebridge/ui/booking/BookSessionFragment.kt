package com.simats.aspirebridge.ui.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.simats.aspirebridge.R
import com.simats.aspirebridge.data.model.AvailabilitySlot
import com.simats.aspirebridge.databinding.FragmentBookSessionBinding
import com.simats.aspirebridge.di.DependencyContainer
import com.simats.aspirebridge.ui.ViewModelFactory
import com.simats.aspirebridge.ui.mentors.AvailabilityAdapter
import java.text.SimpleDateFormat
import java.util.*

class BookSessionFragment : Fragment() {

    private var _binding: FragmentBookSessionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: BookSessionViewModel
    private lateinit var slotsAdapter: AvailabilityAdapter

    private val args: BookSessionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupUI()
        setupRecyclerView()
        setupObservers()
        setupListeners()

        // Load mentor and slots
        viewModel.loadMentorAndSlots(args.mentorId)
    }

    private fun setupViewModel() {
        val dependencyContainer = DependencyContainer.getInstance(requireContext())
        val factory = ViewModelFactory(dependencyContainer)
        viewModel = ViewModelProvider(this, factory)[BookSessionViewModel::class.java]
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        slotsAdapter = AvailabilityAdapter { slot ->
            viewModel.selectSlot(slot)
        }

        binding.rvAvailableSlots.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = slotsAdapter
        }
    }

    private fun setupObservers() {
        viewModel.mentor.observe(viewLifecycleOwner) { mentor ->
            mentor?.let { bindMentorData(it) }
        }

        viewModel.availableSlots.observe(viewLifecycleOwner) { slots ->
            slotsAdapter.submitList(slots)
        }

        viewModel.selectedSlot.observe(viewLifecycleOwner) { slot ->
            updateSlotSummary(slot)
        }

        viewModel.selectedDuration.observe(viewLifecycleOwner) { duration ->
            updateDurationSummary(duration)
        }

        viewModel.totalAmount.observe(viewLifecycleOwner) { amount ->
            binding.tvTotalAmount.text = "₹$amount"
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.btnBookSession.isEnabled = !isLoading
            if (isLoading) {
                binding.btnBookSession.text = "Booking..."
            } else {
                binding.btnBookSession.text = "Book Session"
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }

        viewModel.bookingSuccess.observe(viewLifecycleOwner) { booking ->
            booking?.let {
                Toast.makeText(requireContext(), "Session booked successfully!", Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
                viewModel.clearBookingSuccess()
            }
        }
    }

    private fun setupListeners() {
        // Duration selection
        binding.chipGroupDuration.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val duration = when (checkedIds[0]) {
                    R.id.chip30min -> 30
                    R.id.chip60min -> 60
                    R.id.chip90min -> 90
                    R.id.chip120min -> 120
                    else -> 60
                }
                viewModel.selectDuration(duration)
            }
        }

        // Book session button
        binding.btnBookSession.setOnClickListener {
            val message = binding.etMessage.text?.toString() ?: ""
            viewModel.bookSession(message)
        }
    }

    private fun bindMentorData(mentor: com.simats.aspirebridge.data.model.Mentor) {
        binding.apply {
            tvMentorName.text = mentor.name
            tvMentorExam.text = "${mentor.examCategory} ${mentor.examYear} • Rank ${mentor.rank}"
            tvHourlyRate.text = "₹${mentor.hourlyRate}/hr"

            // Profile image (placeholder for now)
            ivMentorPhoto.setImageResource(R.drawable.ic_person)
        }
    }

    private fun updateSlotSummary(slot: AvailabilitySlot?) {
        if (slot != null) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val displayDateFormat = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
            
            try {
                val date = dateFormat.parse(slot.date)
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val startTime = timeFormat.parse(slot.startTime)
                
                val calendar = Calendar.getInstance()
                calendar.time = date ?: Date()
                calendar.set(Calendar.HOUR_OF_DAY, startTime?.hours ?: 0)
                calendar.set(Calendar.MINUTE, startTime?.minutes ?: 0)
                
                binding.tvSummarySlot.text = displayDateFormat.format(calendar.time)
            } catch (e: Exception) {
                binding.tvSummarySlot.text = "${slot.date} ${slot.startTime}"
            }
        } else {
            binding.tvSummarySlot.text = "Not selected"
        }
    }

    private fun updateDurationSummary(duration: Int) {
        val durationText = when (duration) {
            30 -> "30 minutes"
            60 -> "1 hour"
            90 -> "1.5 hours"
            120 -> "2 hours"
            else -> "$duration minutes"
        }
        binding.tvSummaryDuration.text = durationText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}