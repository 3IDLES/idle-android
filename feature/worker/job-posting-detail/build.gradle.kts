plugins {
    id("care.android.feature-compose")
}

android {
    namespace = "com.idle.worker.job.posting.detail"
}

dependencies {
    implementation(libs.coil.compose)
}