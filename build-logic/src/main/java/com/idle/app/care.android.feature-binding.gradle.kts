import com.idle.app.configureHiltAndroid
import com.idle.app.libs

plugins {
    id("care.android.library")
    id("care.android.binding")
}

android {
    packaging {
        resources {
            excludes.add("META-INF/**")
        }
    }
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

configureHiltAndroid()

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:designsystem:binding"))
    implementation(project(":core:domain"))

    val libs = project.extensions.libs
    implementation(libs.findLibrary("androidx.appcompat").get())
    implementation(libs.findLibrary("androidx.fragment").get())
}