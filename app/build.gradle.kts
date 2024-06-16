plugins {
    id("care.android.application")
    id("care.android.compose")
}

android {
    namespace = "com.idle.care"

    defaultConfig {
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.data)

    implementation(projects.feature.auth)

    implementation(libs.androidx.activity.compose)
}