plugins {
    id("care.android.feature-compose")
}

android {
    namespace = "com.idle.center.job.posting"
}

dependencies {
    implementation(projects.feature.postcode)
    implementation(libs.coil.compose)
}