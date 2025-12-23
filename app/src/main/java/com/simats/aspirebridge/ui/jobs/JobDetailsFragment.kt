package com.simats.aspirebridge.ui.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.simats.aspirebridge.databinding.FragmentJobDetailsBinding

class JobDetailsFragment : Fragment() {

    private var _binding: FragmentJobDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupClickListeners()
        loadJobDetails()
    }

    private fun setupUI() {
        // Setup any additional UI components if needed
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnBookmark.setOnClickListener {
            // TODO: Implement bookmark functionality
        }

        binding.btnShare.setOnClickListener {
            // TODO: Implement share functionality
        }

        binding.btnApplyNow.setOnClickListener {
            // TODO: Implement apply functionality
        }

        binding.btnGetUpdates.setOnClickListener {
            // TODO: Implement get updates functionality
        }
    }

    private fun loadJobDetails() {
        // TODO: Load job details from arguments or API
        // For now, using placeholder data
        binding.apply {
            jobTitle.text = "Assistant Section Officer (ASO)"
            departmentName.text = "Central Secretariat Service"
            salaryRange.text = "₹25,500 - 81,100"
            vacancyCount.text = "500 Posts"
            educationRequirement.text = "Graduate"
            
            jobResponsibilities.text = """
                • Assist in administrative functions of the department
                • Handle correspondence and file management
                • Coordinate with various sections and departments
                • Prepare reports and maintain records
                • Support senior officers in policy implementation
            """.trimIndent()
            
            educationDetails.text = "Bachelor's degree from recognized university"
            ageLimit.text = "18-27 years (Relaxation as per rules)"
            experienceRequired.text = "Fresher can apply"
            
            departmentLineage.text = "Central Government → Ministry of Personnel → Department of Personnel & Training → Central Secretariat Service"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}