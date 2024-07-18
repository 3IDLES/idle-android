package com.idle.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.idle.binding.repeatOnStarted
import com.idle.presentation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

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

    private val centerBottomNavDestinationIds: Set<Int> by lazy {
        resources.obtainTypedArray(R.array.centerNavDestinationIds).let { typedArray ->
            val destinationIds = mutableSetOf<Int>()

            for (index in 0 until typedArray.length()) {
                destinationIds.add(typedArray.getResourceId(index, 0))
            }

            typedArray.recycle()
            destinationIds
        }
    }

    private val workerBottomNavDestinationIds: Set<Int> by lazy {
        resources.obtainTypedArray(R.array.workerNavDestinationIds).let { typedArray ->
            val destinationIds = mutableSetOf<Int>()

            for (index in 0 until typedArray.length()) {
                destinationIds.add(typedArray.getResourceId(index, 0))
            }

            typedArray.recycle()
            destinationIds
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repeatOnStarted {
            viewModel.navigationMenuType.collect { menuType -> setNavigationMenuType(menuType) }
        }

        binding.apply {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.main_FCV) as NavHostFragment
            navController = navHostFragment.navController
        }

        setDestinationListener()
    }

    private fun setNavigationMenuType(menuType: NavigationMenuType) {
        when (menuType) {
            NavigationMenuType.CENTER -> {
                binding.apply {
                    mainBNVWorker.visibility = View.INVISIBLE
                    mainBNVCenter.setupWithNavController(navController)
                }
            }

            NavigationMenuType.WORKER -> {
                binding.apply {
                    mainBNVCenter.visibility = View.INVISIBLE
                    mainBNVWorker.setupWithNavController(navController)
                }
            }

            else -> {}
        }
    }

    private fun setDestinationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.apply {
                mainBNVCenter.isVisible = (destination.id !in hideBottomNavDestinationIds)
                mainBNVWorker.isVisible = (destination.id !in hideBottomNavDestinationIds)

                val navMenuType = if (destination.id in centerBottomNavDestinationIds) {
                    NavigationMenuType.CENTER
                } else if (destination.id in workerBottomNavDestinationIds) {
                    NavigationMenuType.WORKER
                } else {
                    NavigationMenuType.NONE
                }

                viewModel.setNavigationMenuType(navMenuType)
            }
        }
    }
}
