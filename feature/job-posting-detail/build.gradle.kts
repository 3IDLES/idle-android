plugins {
    id("care.android.feature-compose")
}

android {
    namespace = "com.idle.chatting"
}

dependencies {
    implementation(libs.coil.compose)
}