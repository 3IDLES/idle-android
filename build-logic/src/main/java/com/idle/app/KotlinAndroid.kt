package com.idle.app

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid() {
    with(plugins) {
        apply("com.google.gms.google-services")
    }

    pluginManager.apply("org.jetbrains.kotlin.android")

    androidExtension.apply {
        compileSdk = 34

        defaultConfig {
            minSdk = 26
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        testOptions {
            unitTests {
                isIncludeAndroidResources = true
            }
        }

        val libs = extensions.libs

        dependencies {
            val bom = libs.findLibrary("firebase-bom").get()
            add("implementation", platform(bom))
            add("implementation", libs.findLibrary("firebase-analytics").get())
            add("implementation", libs.findLibrary("firebase-crashlytics").get())
        }
    }

    configureKotlin()
}

internal fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)

            val warningsAsErrors: String? by project
            allWarningsAsErrors.set(warningsAsErrors.toBoolean())
            freeCompilerArgs.set(
                freeCompilerArgs.get() + listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                )
            )
        }
    }
}