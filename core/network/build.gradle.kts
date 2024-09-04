import java.util.Properties

plugins {
    id("care.android.library")
    id("care.android.hilt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.idle.network"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").bufferedReader())
        buildConfigField(
            "String",
            "CARE_BASE_URL",
            "\"${properties["CARE_BASE_URL"]}\"",
        )
    }
}

dependencies {
    implementation(projects.core.domain)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
}
