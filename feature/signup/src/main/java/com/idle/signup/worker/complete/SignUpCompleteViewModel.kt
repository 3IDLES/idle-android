package com.idle.center.jobposting.complete

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpCompleteViewModel @Inject constructor(val navigationHelper: com.idle.navigation.NavigationHelper) :
    ViewModel()
