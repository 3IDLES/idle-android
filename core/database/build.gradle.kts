plugins {
    id("care.android.library")
    id("care.android.hilt")
}

android {
    namespace = "com.idle.database"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.common)

    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}
