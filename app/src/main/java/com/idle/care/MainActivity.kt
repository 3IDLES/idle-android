package com.idle.care

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.idle.care.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.main_FCV) as NavHostFragment
            navController = navHostFragment.navController
        }

        setDestinationListener()
    }

    private fun setDestinationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.mainBNV.isVisible = (destination.id !in hideBottomNavDestinationIds)
        }
    }

    private val hideBottomNavDestinationIds: Set<Int> by lazy {
        resources.obtainTypedArray(R.array.hideBottomNavDestinationIds).let { typedArray ->
            val destinationIds = mutableSetOf<Int>()

            for (index in 0 until typedArray.length()) {
                destinationIds.add(typedArray.getResourceId(index, 0))
            }

            typedArray.recycle()
            destinationIds
        }
    }
}