plugins {
    id("care.android.library")
    id("care.android.hilt")
}

android {
    namespace = "com.idle.datastore"
}

dependencies {
    implementation(libs.androidx.datastore)
}
