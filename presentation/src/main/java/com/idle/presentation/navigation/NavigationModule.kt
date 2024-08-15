package com.idle.presentation.navigation

import androidx.fragment.app.Fragment
import com.idle.setting.navigation.SettingNavigation
import com.idle.withdrawal.navigation.WithdrawalNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object NavigationModule {

    @Provides
    @FragmentScoped
    fun provideWithdrawalNavigation(
        fragment: Fragment
    ): WithdrawalNavigation {
        return WithdrawalNavigationImpl(fragment)
    }

    @Provides
    @FragmentScoped
    fun provideSettingNavigation(
        fragment: Fragment
    ): SettingNavigation {
        return SettingNavigationImpl(fragment)
    }
}