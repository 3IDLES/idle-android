package com.idle.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationHelper @Inject constructor() {
    private val _navigationFlow = Channel<NavigationEvent>(BUFFERED)
    val navigationFlow = _navigationFlow.receiveAsFlow()

    fun navigateTo(navigationEvent: NavigationEvent) {
        _navigationFlow.trySend(navigationEvent)
    }
}

sealed class NavigationEvent {
    data class To(val destination: DeepLinkDestination, val popUpTo: Int? = null) :
        NavigationEvent()

    data class ToAuthWithClearBackStack(
        val toastMsg: String,
        val toastType: String = "ERROR",
    ) : NavigationEvent()
}
