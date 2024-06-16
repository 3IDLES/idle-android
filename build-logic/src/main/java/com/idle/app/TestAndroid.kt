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
            add("androidTestImplementation",libs.findLibrary("junit4").get())
            add("androidTestImplementation",libs.findLibrary("androidx.test.ext").get())
        }
    }
}