package com.idle.center.chatting

import com.idle.binding.EventHandlerHelper
import com.idle.binding.NavigationHelper
import com.idle.binding.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CenterChattingViewModel @Inject constructor(
    val navigationHelper: NavigationHelper,
    val eventHelper: EventHandlerHelper,
) : BaseViewModel() {}