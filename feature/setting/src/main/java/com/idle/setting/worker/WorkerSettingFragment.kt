package com.idle.setting.worker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.idle.binding.DeepLinkDestination.Withdrawal
import com.idle.binding.DeepLinkDestination.WorkerProfile
import com.idle.binding.NavigationEvent
import com.idle.binding.base.BaseBindingFragment
import com.idle.binding.repeatOnStarted
import com.idle.domain.model.auth.UserType
import com.idle.setting.FAQ_URL
import com.idle.setting.INQUIRY
import com.idle.setting.PRIVACY_POLICY_URL
import com.idle.setting.R
import com.idle.setting.SettingEvent
import com.idle.setting.TERMS_AND_POLICES_URL
import com.idle.setting.databinding.FragmentWorkerSettingBinding
import com.idle.setting.dialog.LogoutDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerSettingFragment :
    BaseBindingFragment<FragmentWorkerSettingBinding, WorkerSettingViewModel>(
        FragmentWorkerSettingBinding::inflate
    ) {
    override val fragmentViewModel: WorkerSettingViewModel by viewModels()

    private val logoutDialog: LogoutDialogFragment? by lazy {
        LogoutDialogFragment().apply {
            onDismissCallback = {
                findNavController().currentBackStackEntry?.savedStateHandle?.let {
                    val result = it.get<Boolean>("logout")
                    if (result == true) fragmentViewModel.logout()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = fragmentViewModel
            alarmRow.setOnSwitchClickListener {
                val intent = Intent().apply {
                    action = android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    putExtra(
                        android.provider.Settings.EXTRA_APP_PACKAGE,
                        requireContext().packageName
                    )
                }
                startActivity(intent)
            }
        }

        fragmentViewModel.apply {
            viewLifecycleOwner.repeatOnStarted {
                workerSettingEvent.collect {
                    handleSettingEvent(it)
                }
            }
        }

        analyticsHelper.logScreenView(screenName = "center_setting_screen")
    }

    override fun onResume() {
        super.onResume()
        val notificationEnable = NotificationManagerCompat.from(requireContext())
            .areNotificationsEnabled()
        binding.alarmRow.setSwitchState(notificationEnable)
    }

    private fun handleSettingEvent(event: SettingEvent) {
        when (event) {
            SettingEvent.Profile -> fragmentViewModel.navigationHelper.navigateTo(
                NavigationEvent.NavigateTo(WorkerProfile())
            )

            SettingEvent.FAQ -> navigateToUri(FAQ_URL)
            SettingEvent.PrivacyPolicy -> navigateToUri(PRIVACY_POLICY_URL)
            SettingEvent.TermsAndPolicies -> navigateToUri(TERMS_AND_POLICES_URL)
            SettingEvent.Inquiry -> navigateToUri(INQUIRY)
            SettingEvent.Withdrawal -> fragmentViewModel.navigationHelper.navigateTo(
                NavigationEvent.NavigateTo(
                    destination = Withdrawal(UserType.WORKER),
                    popUpTo = R.id.workerSettingFragment
                )
            )

            SettingEvent.Logout -> {
                if (!(logoutDialog?.isAdded == true || logoutDialog?.isVisible == true)) {
                    logoutDialog?.show(parentFragmentManager, "LogoutDialogFragment")
                }
            }
        }
    }

    private fun navigateToUri(url: String) =
        startActivity(Intent(Intent.ACTION_VIEW, url.toUri()), null)
}