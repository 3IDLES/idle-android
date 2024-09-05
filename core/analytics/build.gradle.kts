import java.util.Properties

plugins {
    id("care.android.library")
    id("care.android.compose")
    id("care.android.hilt")
}

android {
    namespace = "com.idle.analytics"

    defaultConfig {
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").bufferedReader())
        buildConfigField(
            "String",
            "AMPLITUDE_API_KEY",
            "\"${properties["AMPLITUDE_API_KEY"]}\"",
        )
    }

    buildTypes {
        debug {
            buildConfigField("String", "BUILD_TYPE", "\"DEBUG\"")
        }
        release {
            buildConfigField("String", "BUILD_TYPE", "\"RELEASE\"")
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.amplitude.analytics)
}
