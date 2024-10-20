plugins {
    id("care.android.feature-compose")
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "com.idle.job.posting.detail"
}

dependencies {
    implementation(projects.feature.center.jobPostingEdit)

    implementation(libs.coil.compose)
}