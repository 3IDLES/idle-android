plugins {
    id("care.android.feature-compose")
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "com.idle.worker.profile"
}

dependencies {
    implementation(projects.feature.postcode)

    implementation(libs.coil.compose)
}