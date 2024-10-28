package com.idle.app

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

internal fun Project.configureTest() {
    configureJUnit()
    val libs = extensions.libs
    dependencies {
        "testImplementation"(libs.findLibrary("junit4").get())
        "testImplementation"(libs.findLibrary("junit-jupiter").get())
        "testImplementation"(libs.findLibrary("coroutines-test").get())
        "testImplementation"(libs.findLibrary("mockk").get())
    }
}

internal fun Project.configureJUnit() {
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}