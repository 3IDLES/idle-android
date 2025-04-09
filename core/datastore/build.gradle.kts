plugins {
    id("care.android.library")
    id("care.android.hilt")
}

android {
    namespace = "com.idle.datastore"
}

dependencies {
    implementation(projects.core.domain)

    implementation(libs.androidx.datastore)
}
