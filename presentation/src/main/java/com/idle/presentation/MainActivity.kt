package com.idle.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.idle.binding.repeatOnStarted
import com.idle.presentation.databinding.ActivityMainBinding
import com.idle.presentation.forceupdate.ForceUpdateFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()
    lateinit var forceUpdateDialog: ForceUpdateFragment

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
        val splashScreen = installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repeatOnStarted {
            viewModel.navigationMenuType.collect { menuType -> setNavigationMenuType(menuType) }
        }

        repeatOnStarted {
            viewModel.forceUpdate.collect {
                it?.let { info ->
                    val nowVersion = packageManager.getPackageInfo(packageName, 0).versionName
                    val minAppVersion = info.minVersion
                    val shouldUpdate = checkShouldUpdate(nowVersion, minAppVersion)

                    if (shouldUpdate) {
                        forceUpdateDialog = ForceUpdateFragment(info).apply {
                            isCancelable = false
                        }
                        forceUpdateDialog.show(supportFragmentManager, forceUpdateDialog.tag)
                    }
                }
            }
        }

        binding.apply {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.main_FCV) as NavHostFragment
            navController = navHostFragment.navController

            mainBNVCenter.itemIconTintList = null
            mainBNVWorker.itemIconTintList = null
        }

        setDestinationListener()
    }

    private fun setDestinationListener() {
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            binding.apply {
                val navMenuType = if (destination.id in centerBottomNavDestinationIds) {
                    NavigationMenuType.Center
                } else if (destination.id in workerBottomNavDestinationIds) {
                    NavigationMenuType.Worker
                } else {
                    NavigationMenuType.Hide
                }

                viewModel.setNavigationMenuType(navMenuType)
            }
        }
    }

    private fun setNavigationMenuType(menuType: NavigationMenuType) = when (menuType) {
        NavigationMenuType.Center -> binding.apply {
            mainBNVWorker.visibility = View.INVISIBLE
            mainBNVCenter.visibility = View.VISIBLE
            mainBNVCenter.setupWithNavController(navController)
        }


        NavigationMenuType.Worker -> binding.apply {
            mainBNVCenter.visibility = View.INVISIBLE
            mainBNVWorker.visibility = View.VISIBLE
            mainBNVWorker.setupWithNavController(navController)
        }


        NavigationMenuType.Hide -> binding.apply {
            mainBNVCenter.visibility = View.GONE
            mainBNVWorker.visibility = View.GONE
        }
    }

    private fun checkShouldUpdate(currentVersion: String, minVersion: String): Boolean {
        val current = normalizeVersion(currentVersion)
        val min = normalizeVersion(minVersion)

        Log.d("test", "current : $current min : $min")

        // 버전 비교 (메이저, 마이너, 패치 순으로 비교)
        for (i in 0..2) {
            if (current[i] < min[i]) return true
            if (current[i] > min[i]) return false
        }
        return false
    }

    private fun normalizeVersion(version: String): List<Int> {
        return version.split('.').map { it.toIntOrNull() ?: 0 }.let {
            when (it.size) {
                2 -> it + listOf(0) // 1.0 -> 1.0.0 형태로 변환
                else -> it
            }
        }
    }
}
