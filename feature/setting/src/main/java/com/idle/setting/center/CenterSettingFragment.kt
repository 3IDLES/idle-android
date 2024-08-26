package com.idle.setting.center

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.idle.binding.DeepLinkDestination.CenterProfile
import com.idle.binding.DeepLinkDestination.Withdrawal
import com.idle.binding.base.BaseBindingFragment
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.binding.repeatOnStarted
import com.idle.domain.model.auth.UserType
import com.idle.setting.FAQ_URL
import com.idle.setting.PRIVACY_POLICY_URL
import com.idle.setting.R
import com.idle.setting.SettingEvent
import com.idle.setting.TERMS_AND_POLICES_URL
import com.idle.setting.databinding.FragmentCenterSettingBinding
import com.idle.setting.dialog.LogoutDialogFragment
import com.idle.setting.navigation.SettingNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class CenterSettingFragment :
    BaseBindingFragment<FragmentCenterSettingBinding, CenterSettingViewModel>(
        FragmentCenterSettingBinding::inflate
    ) {
    override val fragmentViewModel: CenterSettingViewModel by viewModels()

    @Inject
    lateinit var settingNavigation: SettingNavigation

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

        binding.viewModel = fragmentViewModel
        fragmentViewModel.apply {
            viewLifecycleOwner.repeatOnStarted {
                centerSettingEvent.collect {
                    handleSettingEvent(it)
                }
            }
        }
    }

    private fun handleSettingEvent(event: SettingEvent) {
        when (event) {
            SettingEvent.Profile -> fragmentViewModel.baseEvent(NavigateTo(CenterProfile))
            SettingEvent.FAQ -> navigateToUri(FAQ_URL)
            SettingEvent.PrivacyPolicy -> navigateToUri(PRIVACY_POLICY_URL)
            SettingEvent.TermsAndPolicies -> navigateToUri(TERMS_AND_POLICES_URL)
            SettingEvent.Inquiry -> {}
            SettingEvent.Withdrawal -> fragmentViewModel.baseEvent(
                NavigateTo(
                    destination = Withdrawal(UserType.CENTER),
                    popUpTo = R.id.centerSettingFragment
                )
            )

            SettingEvent.Logout -> {
                if (!(logoutDialog?.isAdded == true || logoutDialog?.isVisible == true)) {
                    logoutDialog?.show(parentFragmentManager, "LogoutDialogFragment")
                }
            }

            SettingEvent.LogoutSuccess -> settingNavigation.navigateToAuth()
        }
    }

    private fun navigateToUri(url: String) =
        startActivity(Intent(Intent.ACTION_VIEW, url.toUri()), null)
}