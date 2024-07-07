plugins {
    id("care.android.library")
    id("care.android.hilt")
}

android {
    namespace = "com.idle.data"
}

dependencies {
    implementation(projects.core.network)
}