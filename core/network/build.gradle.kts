import java.util.Properties

plugins {
    id("care.android.library")
    id("care.android.hilt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.idle.network"

    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").bufferedReader())

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "CARE_BASE_URL",
                "\"${properties["CARE_DEV_BASE_URL"]}\"",
            )
        }
        release {
            buildConfigField(
                "String",
                "CARE_BASE_URL",
                "\"${properties["CARE_PROD_BASE_URL"]}\"",
            )

        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.domain)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.firebase.config)
    implementation(libs.firebase.messaging)
}
