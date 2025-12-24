package com.simats.aspirebridge.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.simats.aspirebridge.R
import com.simats.aspirebridge.databinding.FragmentProfileSetupBinding

/**
 * Fragment for initial profile setup after registration
 */
class ProfileSetupFragment : Fragment() {
    
    private var _binding: FragmentProfileSetupBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileSetupBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        binding.btnSkip.setOnClickListener {
            findNavController().navigate(R.id.action_profile_setup_to_main)
        }
        
        binding.btnComplete.setOnClickListener {
            // TODO: Save profile data
            findNavController().navigate(R.id.action_profile_setup_to_main)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}