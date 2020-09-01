package com.jshvarts.healthreads

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.jshvarts.healthreads.databinding.ActivityMainBinding
import com.jshvarts.healthreads.databinding.ActivityMainBinding.inflate

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = inflate(layoutInflater)
    setContentView(binding.root)

    val navController = findNavController(R.id.nav_host_fragment)
    val appBarConfiguration = AppBarConfiguration(navController.graph)

    setSupportActionBar(binding.toolbar)

    binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    setupActionBarWithNavController(navController, appBarConfiguration)
  }
}
