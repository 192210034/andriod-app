package com.simats.aspirebridge.ui.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.simats.aspirebridge.R
import com.simats.aspirebridge.databinding.FragmentCentralGovernmentJobsBinding

class CentralGovernmentJobsFragment : Fragment() {

    private var _binding: FragmentCentralGovernmentJobsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCentralGovernmentJobsBinding.inflate(inflater, container, false)
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

        // UPSC Services
        binding.cardUpscServices.setOnClickListener {
            navigateToJobCategory("UPSC Group A Services", "upsc")
        }

        // SSC Jobs
        binding.cardSscJobs.setOnClickListener {
            navigateToJobCategory("SSC (Staff Selection Commission)", "ssc")
        }

        // Railways
        binding.cardRailways.setOnClickListener {
            navigateToJobCategory("Indian Railways", "railways")
        }

        // Banking
        binding.cardBanking.setOnClickListener {
            navigateToJobCategory("Banking Sector", "banking")
        }

        // Defence
        binding.cardDefence.setOnClickListener {
            navigateToJobCategory("Defence Services", "defence")
        }

        // PSUs
        binding.cardPsus.setOnClickListener {
            navigateToJobCategory("Public Sector Units (PSUs)", "psus")
        }
    }

    private fun navigateToJobCategory(categoryName: String, categoryId: String) {
        // TODO: Navigate to specific job category with jobs list
        // For now, navigate to job details as placeholder
        findNavController().navigate(R.id.action_central_jobs_to_job_details)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}