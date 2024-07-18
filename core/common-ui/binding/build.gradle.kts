plugins {
    id("care.android.library")
    id("care.android.binding")
}

android {
    namespace = "com.idle.common.ui.binding"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.navigation.fragment)
}