plugins {
    id("care.android.library")
}

android {
    namespace = "com.idle.common.ui"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime)
}