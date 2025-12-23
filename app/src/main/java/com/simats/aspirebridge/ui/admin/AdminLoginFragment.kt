package com.simats.aspirebridge.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.simats.aspirebridge.R
import com.simats.aspirebridge.data.manager.UserSessionManager
import com.simats.aspirebridge.data.model.UserType
import com.simats.aspirebridge.databinding.FragmentAdminLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Admin login fragment for platform administration
 */
@AndroidEntryPoint
class AdminLoginFragment : Fragment() {
    
    @Inject
    lateinit var userSessionManager: UserSessionManager
    
    private var _binding: FragmentAdminLoginBinding? = null
    private val binding get() = _binding!!
    
    // Admin credentials - In production, this should be handled securely
    private val adminEmail = "admin@aspirebridge.com"
    private val adminPassword = "admin123"
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        binding.btnAdminLogin.setOnClickListener {
            performAdminLogin()
        }
        
        binding.btnBackToLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    
    private fun performAdminLogin() {
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        
        // Validate input
        if (email.isEmpty()) {
            binding.inputLayoutEmail.error = "Email is required"
            return
        }
        
        if (password.isEmpty()) {
            binding.inputLayoutPassword.error = "Password is required"
            return
        }
        
        // Clear previous errors
        binding.inputLayoutEmail.error = null
        binding.inputLayoutPassword.error = null
        
        // Show loading
        binding.progressLoading.visibility = View.VISIBLE
        binding.btnAdminLogin.isEnabled = false
        
        // Simulate login delay
        binding.root.postDelayed({
            if (email == adminEmail && password == adminPassword) {
                // Login successful
                userSessionManager.setMockUser(UserType.ADMIN)
                Toast.makeText(requireContext(), "Admin login successful", Toast.LENGTH_SHORT).show()
                
                // Navigate to admin dashboard
                findNavController().navigate(R.id.action_admin_login_to_admin_dashboard)
            } else {
                // Login failed
                Toast.makeText(requireContext(), "Invalid admin credentials", Toast.LENGTH_SHORT).show()
                binding.inputLayoutPassword.error = "Invalid credentials"
            }
            
            // Hide loading
            binding.progressLoading.visibility = View.GONE
            binding.btnAdminLogin.isEnabled = true
        }, 1500)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}