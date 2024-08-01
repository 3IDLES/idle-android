plugins {
    id("care.android.feature-compose")
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.idle.center.job.edit"
}

dependencies {
    implementation(projects.feature.postcode)
}