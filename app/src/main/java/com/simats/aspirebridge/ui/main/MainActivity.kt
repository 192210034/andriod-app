package com.simats.aspirebridge.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.simats.aspirebridge.R
import com.simats.aspirebridge.data.manager.UserSessionManager
import com.simats.aspirebridge.databinding.ActivityMainBinding
import com.simats.aspirebridge.ui.auth.AuthActivity

/**
 * Main activity that hosts the navigation fragments
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var userSessionManager: UserSessionManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize user session manager
        userSessionManager = UserSessionManager(this)
        
        // Check if user is logged in
        if (!userSessionManager.isLoggedIn()) {
            // User not logged in, redirect to auth flow
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupNavigation()
    }
    
    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        binding.bottomNavigation.setupWithNavController(navController)
    }
}