package com.simats.aspirebridge.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.simats.aspirebridge.R
import com.simats.aspirebridge.databinding.FragmentUserTypeSelectionBinding

class UserTypeSelectionFragment : Fragment() {

    private var _binding: FragmentUserTypeSelectionBinding? = null
    private val binding get() = _binding!!

    private var selectedUserType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserTypeSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.cardAspirant.setOnClickListener {
            selectUserType("aspirant")
            navigateToRegister("aspirant")
        }

        binding.cardAchiever.setOnClickListener {
            selectUserType("achiever")
            navigateToRegister("achiever")
        }

        binding.textLogin.setOnClickListener {
            findNavController().navigate(R.id.action_user_type_selection_to_login)
        }
    }

    private fun selectUserType(userType: String) {
        selectedUserType = userType
        
        // Update card appearances
        when (userType) {
            "aspirant" -> {
                binding.cardAspirant.strokeWidth = 4
                binding.cardAspirant.strokeColor = ContextCompat.getColor(requireContext(), R.color.aspirant_primary)
                binding.cardAchiever.strokeWidth = 0
            }
            "achiever" -> {
                binding.cardAchiever.strokeWidth = 4
                binding.cardAchiever.strokeColor = ContextCompat.getColor(requireContext(), R.color.achiever_primary)
                binding.cardAspirant.strokeWidth = 0
            }
        }
    }

    private fun navigateToRegister(userType: String) {
        val action = when (userType) {
            "aspirant" -> R.id.action_user_type_selection_to_register_aspirant
            "achiever" -> R.id.action_user_type_selection_to_register_achiever
            else -> return
        }
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}