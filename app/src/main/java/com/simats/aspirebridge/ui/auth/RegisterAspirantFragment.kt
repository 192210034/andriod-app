package com.simats.aspirebridge.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.simats.aspirebridge.R
import com.simats.aspirebridge.databinding.FragmentRegisterAspirantBinding

class RegisterAspirantFragment : Fragment() {

    private var _binding: FragmentRegisterAspirantBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterAspirantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.textChange.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnContinue.setOnClickListener {
            if (validateForm()) {
                // Navigate to OTP verification
                findNavController().navigate(R.id.action_register_aspirant_to_otp_verification)
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        // Validate full name
        if (binding.editFullName.text.toString().trim().isEmpty()) {
            binding.editFullName.error = "Full name is required"
            isValid = false
        }

        // Validate email
        val email = binding.editEmail.text.toString().trim()
        if (email.isEmpty()) {
            binding.editEmail.error = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editEmail.error = "Please enter a valid email"
            isValid = false
        }

        // Validate phone
        if (binding.editPhone.text.toString().trim().isEmpty()) {
            binding.editPhone.error = "Phone number is required"
            isValid = false
        }

        // Validate password
        val password = binding.editPassword.text.toString()
        if (password.isEmpty()) {
            binding.editPassword.error = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            binding.editPassword.error = "Password must be at least 6 characters"
            isValid = false
        }

        // Validate terms checkbox
        if (!binding.checkboxTerms.isChecked) {
            // Show error message
            isValid = false
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}