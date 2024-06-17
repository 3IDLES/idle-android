plugins {
    id("care.android.library")
    id("care.android.compose")
}

android {
    namespace = "com.idle.designsystem"
}

dependencies {
    implementation(libs.androidx.appcompat)
}