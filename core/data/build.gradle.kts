plugins {
    id("care.android.library")
    id("care.android.hilt")
}

android {
    namespace = "com.idle.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.network)
    implementation(projects.core.database)
}