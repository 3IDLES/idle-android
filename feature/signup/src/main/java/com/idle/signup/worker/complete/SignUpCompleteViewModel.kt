package com.idle.center.jobposting.complete

import com.idle.binding.base.BaseViewModel
import com.idle.binding.base.EventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpCompleteViewModel @Inject constructor(
    val eventHandler: EventHandler,
) : BaseViewModel()
