plugins {
    id("care.android.library")
    id("care.android.binding")
}

android {
    namespace = "com.idle.common.ui.binding"
}

dependencies {
    implementation(projects.core.designresource)
    implementation(projects.core.domain)
    implementation(projects.core.analytics)

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.navigation.fragment)
}
