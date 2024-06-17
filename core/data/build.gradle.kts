plugins {
    id("care.android.library")
    id("care.android.hilt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.idle.data"
}

dependencies {
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
}