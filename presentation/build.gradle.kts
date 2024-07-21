plugins {
    id("care.android.feature-binding")
}

android {
    namespace = "com.idle.presentation"
}

dependencies {
    implementation(projects.feature.auth)
    implementation(projects.feature.signin)
    implementation(projects.feature.signup)
    implementation(projects.feature.center.home)
    implementation(projects.feature.center.profile)
    implementation(projects.feature.center.setting)
    implementation(projects.feature.worker.home)
    implementation(projects.feature.worker.jobDetail)
    implementation(libs.androidx.navigation.ui)
}