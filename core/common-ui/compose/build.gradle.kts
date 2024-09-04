plugins {
    id("care.android.library")
    id("care.android.compose")
}

android {
    namespace = "com.idle.common.ui.compose"
}

dependencies {
    implementation(projects.core.designresource)
    implementation(projects.core.commonUi.binding)
    implementation(libs.androidx.navigation.fragment)
}
