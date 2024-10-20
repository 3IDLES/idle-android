plugins {
    id("care.android.feature-compose")
}

android {
    namespace = "com.idle.worker.chatting"
}

dependencies {
    implementation(libs.coil.compose)
}