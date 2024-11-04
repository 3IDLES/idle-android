plugins {
    id("care.android.library")
}

android {
    namespace = "com.idle.navigation"
}

dependencies {
    implementation(projects.core.domain)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.fragment)
}
