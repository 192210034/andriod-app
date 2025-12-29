package com.simats.aspirebridge.ui.auth

import android.content.Intent
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
import com.simats.aspirebridge.databinding.FragmentLoginBinding
import com.simats.aspirebridge.ui.main.MainActivity

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var userSessionManager: UserSessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        userSessionManager = UserSessionManager(requireContext())
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()
            
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Mock login logic - In real app, this would call API
            when {
                email == "admin@aspirebridge.com" && password == "admin123" -> {
                    // Admin login
                    userSessionManager.setMockUser(UserType.ADMIN)
                    navigateToMainApp()
                }
                email.contains("achiever") || password == "achiever123" -> {
                    // Mock achiever login
                    userSessionManager.setMockUser(UserType.ACHIEVER)
                    navigateToMainApp()
                }
                else -> {
                    // Default to aspirant login
                    userSessionManager.setMockUser(UserType.ASPIRANT)
                    navigateToMainApp()
                }
            }
        }

        binding.textSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_user_type_selection)
        }

        binding.textForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forgot_password)
        }
        
        binding.btnAdminAccess.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_admin_login)
        }
    }
    
    private fun navigateToMainApp() {
        // Navigate to MainActivity
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}