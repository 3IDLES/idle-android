package com.idle.app.configure

import com.idle.app.androidExtension
import org.gradle.api.Project

internal fun Project.configureAndroidBinding() {
    androidExtension.apply {
        buildFeatures.apply {
            viewBinding = true
            dataBinding.enable = true
        }
    }
}