package com.simats.aspirebridge.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.simats.aspirebridge.R
import com.simats.aspirebridge.databinding.FragmentRegisterAchieverBinding

class RegisterAchieverFragment : Fragment() {

    private var _binding: FragmentRegisterAchieverBinding? = null
    private val binding get() = _binding!!

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Handle selected image
            result.data?.data?.let {
                // TODO: Process the selected image
                binding.btnUploadScorecard.text = "Score Card Uploaded ✓"
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterAchieverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        setupDropdowns()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.textChange.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnUploadScorecard.setOnClickListener {
            openImagePicker()
        }

        binding.btnContinue.setOnClickListener {
            if (validateForm()) {
                // Navigate to OTP verification
                findNavController().navigate(R.id.action_register_achiever_to_otp_verification)
            }
        }
    }

    private fun setupDropdowns() {
        // Setup exam name dropdown
        val examNames = arrayOf(
            "JEE Main", "JEE Advanced", "NEET", "GATE", "CAT", "UPSC", "SSC", "Banking", "Other"
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, examNames)
        binding.dropdownExamName.setAdapter(adapter)
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }

    private fun validateForm(): Boolean {
        var isValid = true

        // Validate basic details
        if (binding.editFullName.text.toString().trim().isEmpty()) {
            binding.editFullName.error = "Full name is required"
            isValid = false
        }

        val email = binding.editEmail.text.toString().trim()
        if (email.isEmpty()) {
            binding.editEmail.error = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editEmail.error = "Please enter a valid email"
            isValid = false
        }

        if (binding.editPhone.text.toString().trim().isEmpty()) {
            binding.editPhone.error = "Phone number is required"
            isValid = false
        }

        val password = binding.editPassword.text.toString()
        if (password.isEmpty()) {
            binding.editPassword.error = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            binding.editPassword.error = "Password must be at least 6 characters"
            isValid = false
        }

        // Validate achievement details
        if (binding.dropdownExamName.text.toString().trim().isEmpty()) {
            binding.dropdownExamName.error = "Exam name is required"
            isValid = false
        }

        if (binding.editSubExam.text.toString().trim().isEmpty()) {
            binding.editSubExam.error = "Sub exam name is required"
            isValid = false
        }

        if (binding.editYear.text.toString().trim().isEmpty()) {
            binding.editYear.error = "Year is required"
            isValid = false
        }

        if (binding.editRollNumber.text.toString().trim().isEmpty()) {
            binding.editRollNumber.error = "Roll number is required"
            isValid = false
        }

        if (binding.editMarks.text.toString().trim().isEmpty()) {
            binding.editMarks.error = "Marks/Percentage is required"
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