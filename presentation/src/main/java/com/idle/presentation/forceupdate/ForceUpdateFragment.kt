package com.idle.presentation.forceupdate

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.idle.analytics.helper.AnalyticsHelper
import com.idle.binding.repeatOnStarted
import com.idle.domain.model.config.ForceUpdate
import com.idle.presentation.databinding.FragmentForceUpdateBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForceUpdateFragment(private val forceUpdate: ForceUpdate) : DialogFragment() {
    private val fragmentViewModel: ForceUpdateViewModel by viewModels()

    private var _binding: FragmentForceUpdateBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForceUpdateBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = this@ForceUpdateFragment.viewLifecycleOwner
            viewModel = fragmentViewModel.apply {
                setForceUpdate(this@ForceUpdateFragment.forceUpdate)

                viewLifecycleOwner.repeatOnStarted {
                    forceUpdateEvent.collect { event ->
                        when (event) {
                            ForceUpdateEvent.Dismiss -> requireActivity().finish()
                            ForceUpdateEvent.Update -> {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    this@ForceUpdateFragment.forceUpdate.marketUrl.toUri()
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                activity?.startActivity(intent)
                            }
                        }
                    }
                }
            }
        }

        analyticsHelper.logScreenView("force_update_screen")
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        resizeDialog(1f, 0.24f)
    }

    private fun resizeDialog(width: Float, height: Float) {
        val windowManager =
            requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30) {
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)

            val window = this.dialog?.window

            val x = (size.x * width).toInt()
            val y = (size.y * height).toInt()
            window?.setLayout(x, y)
        } else {
            val rect = windowManager.currentWindowMetrics.bounds

            val window = this.dialog?.window

            val x = (rect.width() * width).toInt()
            val y = (rect.height() * height).toInt()
            window?.setLayout(x, y)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}