package com.simats.aspirebridge.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simats.aspirebridge.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity for authentication flow
 */
@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAuthBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}