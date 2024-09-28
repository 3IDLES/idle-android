plugins {
    id("care.android.feature-compose")
}

android {
    namespace = "com.idle.notification"
}

dependencies {
    implementation(libs.coil.compose)
}