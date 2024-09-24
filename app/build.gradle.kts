import java.util.Properties

plugins {
    id("care.android.application")
    id("care.android.binding")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.idle.care"

    defaultConfig {
        versionCode = 6
        versionName = "1.0.5"
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

    signingConfigs {
        create("release") {
            storeFile = file(project.findProperty("STORE_FILE") as String)
            storePassword = project.findProperty("STORE_PASSWORD") as String
            keyAlias = project.findProperty("KEY_ALIAS") as String
            keyPassword = project.findProperty("KEY_PASSWORD") as String
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
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

    implementation(libs.firebase.messaging)
}
