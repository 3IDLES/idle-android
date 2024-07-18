plugins {
    id("care.android.library")
    id("care.android.binding")
}

android {
    namespace = "com.idle.designsystem.binding"
}

dependencies {
    implementation(projects.core.commonUi.binding)
    implementation(projects.core.designresource)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
}