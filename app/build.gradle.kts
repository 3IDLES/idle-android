import java.util.Properties

plugins {
    id("care.android.application")
    id("care.android.binding")
    id("com.google.firebase.crashlytics")
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "com.idle.care"

    defaultConfig {
        versionCode = 14
        versionName = "1.1.5"
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        localProperties.load(project.rootProject.file("local.properties").bufferedReader())
        manifestPlaceholders["NAVER_CLIENT_ID"] = localProperties["NAVER_CLIENT_ID"] as String

        buildConfigField(
            "String",
            "AMPLITUDE_API_KEY",
            "\"${localProperties["AMPLITUDE_API_KEY"]}\"",
        )
    }

    signingConfigs {
        create("release") {
            val keystoreProperties = Properties()
            keystoreProperties.load(project.rootProject.file("keystore.properties").bufferedReader()
            )

            storeFile = file(keystoreProperties["STORE_FILE_PATH"] as String)
            storePassword = keystoreProperties["STORE_PASSWORD"] as String
            keyAlias = keystoreProperties["KEY_ALIAS"] as String
            keyPassword = keystoreProperties["KEY_PASSWORD"] as String
        }
    }

    packaging { resources { excludes += "/META-INF/*" } }

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
    implementation(projects.core.analytics)

    implementation(libs.firebase.messaging)
}
