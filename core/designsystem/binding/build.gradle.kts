plugins {
    id("care.android.library")
    id("care.android.binding")
}

android {
    namespace = "com.idle.designsystem.binding"
}

dependencies {
    implementation(libs.androidx.appcompat)
}