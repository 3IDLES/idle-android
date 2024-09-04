plugins {
    id("care.android.feature-compose")
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "com.idle.center.profile"
}

dependencies {
    implementation(libs.coil.compose)
}