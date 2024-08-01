plugins {
    id("care.android.feature-compose")
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.idle.center.job.posting"
}

dependencies {
    implementation(projects.feature.postcode)
    implementation(libs.coil.compose)
}