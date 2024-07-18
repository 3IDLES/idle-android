plugins {
    id("care.android.library")
    id("care.android.compose")
}

android {
    namespace = "com.idle.designsystem.compose"
}

dependencies {
    implementation(projects.core.commonUi.compose)
    implementation(platform(libs.androidx.compose.bom))
}