package com.idle.app

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureTestAndroid() {
    configureTest()
    configureJUnitAndroid()
}

@Suppress("UnstableApiUsage")
internal fun Project.configureJUnitAndroid() {
    androidExtension.apply {
        testOptions {
            unitTests.all { it.useJUnitPlatform() }
        }

        val libs = extensions.libs
        dependencies {
            "androidTestImplementation"(libs.findLibrary("androidx.test.ext").get())
            "androidTestImplementation"(libs.findLibrary("androidx.runner").get())
            "androidTestImplementation"(libs.findLibrary("androidx.junit").get())
            "androidTestImplementation"(libs.findLibrary("mockk-android").get())
            "androidTestImplementation"(libs.findLibrary("mockk-agent").get())
            "androidTestImplementation"(libs.findLibrary("coroutines-test").get())
        }
    }
}