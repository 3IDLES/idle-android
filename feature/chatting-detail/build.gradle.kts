plugins {
    id("care.android.feature-compose")
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "com.idle.chatting.detail"
}

dependencies {
    implementation(libs.coil.compose)
}