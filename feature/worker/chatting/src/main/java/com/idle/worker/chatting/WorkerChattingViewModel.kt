package com.idle.worker.chatting

import com.idle.binding.NavigationHelper
import com.idle.binding.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkerChattingViewModel @Inject constructor(
    val navigationHelper: NavigationHelper,
) : BaseViewModel() {}