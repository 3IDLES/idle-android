plugins {
    id("care.android.feature-compose")
}

android {
    namespace = "com.idle.worker.profile"
}

dependencies {
    implementation(libs.coil.compose)
}