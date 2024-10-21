package com.idle.center.jobposting.complete

import com.idle.binding.NavigationRouter
import com.idle.binding.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpCompleteViewModel @Inject constructor(val navigationRouter: NavigationRouter) :
    BaseViewModel()
