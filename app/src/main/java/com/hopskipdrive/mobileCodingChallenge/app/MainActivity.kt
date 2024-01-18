package com.hopskipdrive.mobileCodingChallenge.app

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.hopskipdrive.mobileCodingChallenge.R
import com.hopskipdrive.mobileCodingChallenge.databinding.LayoutMainActivityBinding


class MainActivity : AppCompatActivity(), SetTitleInterface {
    private lateinit var binding: LayoutMainActivityBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

    }

    override fun setTitle(title: String) {
        binding.title.text = title
    }
}