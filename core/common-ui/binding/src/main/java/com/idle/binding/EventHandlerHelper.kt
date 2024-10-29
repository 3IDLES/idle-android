package com.idle.binding

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventHandlerHelper @Inject constructor() {
    private val _eventFlow = Channel<MainEvent>(BUFFERED)
    val eventFlow = _eventFlow.receiveAsFlow()

    fun sendEvent(event: MainEvent) {
        _eventFlow.trySend(event)
    }
}

sealed class MainEvent {
    data class ShowToast(val msg: String, val toastType: ToastType = ToastType.ERROR) :
        MainEvent()

    data object DismissToast : MainEvent()
}

enum class ToastType {
    ERROR, SUCCESS;

    companion object {
        fun create(type: String): ToastType {
            return ToastType.entries.firstOrNull { it.name == type } ?: ERROR
        }
    }
}
