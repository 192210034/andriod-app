package com.simats.aspirebridge.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.simats.aspirebridge.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Home fragment showing dashboard and recent activity
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }
    
    private fun setupUI() {
        // TODO: Implement home screen UI
        binding.textWelcome.text = "Welcome to AspireBridge!"
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}