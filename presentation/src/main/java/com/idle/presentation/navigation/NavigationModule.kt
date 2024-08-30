package com.idle.presentation.navigation

import androidx.fragment.app.Fragment
import com.idle.binding.base.navigation.BaseNavigation
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
    fun provideBaseNavigation(
        fragment: Fragment
    ): BaseNavigation {
        return BaseNavigationImpl(fragment)
    }
}