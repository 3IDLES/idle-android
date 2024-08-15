package com.idle.setting

sealed class SettingEvent {
    data object Profile : SettingEvent()
    data object FAQ : SettingEvent()
    data object Inquiry : SettingEvent()
    data object TermsAndPolicies : SettingEvent()
    data object PrivacyPolicy : SettingEvent()
    data object Logout : SettingEvent()
    data object Withdrawal : SettingEvent()
    data object LogoutSuccess : SettingEvent()
}