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
    implementation(projects.feature.center.register)
    implementation(projects.feature.worker.home)
    implementation(projects.feature.worker.profile)
    implementation(projects.feature.worker.recruitmentDetail)
    implementation(libs.androidx.navigation.ui)
}