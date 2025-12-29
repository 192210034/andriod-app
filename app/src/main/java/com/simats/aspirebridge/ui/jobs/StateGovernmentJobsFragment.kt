package com.simats.aspirebridge.ui.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.simats.aspirebridge.R
import com.simats.aspirebridge.databinding.FragmentStateGovernmentJobsBinding

class StateGovernmentJobsFragment : Fragment() {

    private var _binding: FragmentStateGovernmentJobsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStateGovernmentJobsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        // Setup any additional UI components if needed
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnFilter.setOnClickListener {
            // TODO: Implement filter functionality
        }

        // Police Department
        binding.cardPoliceJobs.setOnClickListener {
            navigateToJobCategory("Police Department", "police")
        }

        // Health Department
        binding.cardHealthJobs.setOnClickListener {
            navigateToJobCategory("Health & Medical Services", "health")
        }

        // Education Department
        binding.cardEducationJobs.setOnClickListener {
            navigateToJobCategory("Education Department", "education")
        }

        // Courts & Judiciary
        binding.cardCourtsJobs.setOnClickListener {
            navigateToJobCategory("Courts & Judiciary", "courts")
        }

        // Sachivalayam
        binding.cardSachivalayamJobs.setOnClickListener {
            navigateToJobCategory("Sachivalayam (Village/Ward)", "sachivalayam")
        }

        // Revenue Department
        binding.cardRevenueJobs.setOnClickListener {
            navigateToJobCategory("Revenue Department", "revenue")
        }

        // Agriculture Department
        binding.cardAgricultureJobs.setOnClickListener {
            navigateToJobCategory("Agriculture Department", "agriculture")
        }

        // Forest Department
        binding.cardForestJobs.setOnClickListener {
            navigateToJobCategory("Forest Department", "forest")
        }

        // Electricity Department
        binding.cardElectricityJobs.setOnClickListener {
            navigateToJobCategory("Electricity Department", "electricity")
        }

        // RTC
        binding.cardRtcJobs.setOnClickListener {
            navigateToJobCategory("RTC (Road Transport)", "rtc")
        }
    }

    private fun navigateToJobCategory(@Suppress("UNUSED_PARAMETER") categoryName: String, @Suppress("UNUSED_PARAMETER") categoryId: String) {
        // TODO: Navigate to specific job category with jobs list
        // For now, navigate to job details as placeholder
        // Parameters will be used when implementing job category navigation
        findNavController().navigate(R.id.action_state_jobs_to_job_details)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}