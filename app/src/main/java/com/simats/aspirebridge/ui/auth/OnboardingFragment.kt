package com.simats.aspirebridge.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.simats.aspirebridge.R
import com.simats.aspirebridge.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnSkip.setOnClickListener {
            navigateToMain()
        }

        binding.btnNext.setOnClickListener {
            // For now, navigate to main screen
            // In a full implementation, this would show the next onboarding screen
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        findNavController().navigate(R.id.action_onboarding_to_main)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}