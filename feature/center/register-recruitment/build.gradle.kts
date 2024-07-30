plugins {
    id("care.android.feature-compose")
}

android {
    namespace = "com.idle.center.register.recruitment"
}

dependencies {
    implementation(projects.feature.postcode)
    implementation(libs.coil.compose)
}