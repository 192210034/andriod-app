package com.simats.aspirebridge.ui.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.aspirebridge.R
import com.simats.aspirebridge.databinding.FragmentExploreJobsBinding

class ExploreJobsFragment : Fragment() {

    private var _binding: FragmentExploreJobsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreJobsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        // Setup popular categories RecyclerView
        binding.recyclerPopularCategories.layoutManager = LinearLayoutManager(context)
        // TODO: Setup adapter for popular categories
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSearch.setOnClickListener {
            // TODO: Implement search functionality
        }

        // Central Government Jobs
        binding.cardCentralJobs.setOnClickListener {
            findNavController().navigate(R.id.action_explore_jobs_to_central_government_jobs)
        }

        // State Government Jobs
        binding.cardStateJobs.setOnClickListener {
            findNavController().navigate(R.id.action_explore_jobs_to_state_government_jobs)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}