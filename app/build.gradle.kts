import java.util.Properties

plugins {
    id("care.android.application")
    id("care.android.binding")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.idle.care"

    defaultConfig {
        versionCode = 5
        versionName = "1.0.4"
        targetSdk = 34

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").bufferedReader())
        manifestPlaceholders["NAVER_CLIENT_ID"] = properties["NAVER_CLIENT_ID"] as String

        buildConfigField(
            "String",
            "AMPLITUDE_API_KEY",
            "\"${properties["AMPLITUDE_API_KEY"]}\"",
        )
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.presentation)
}
