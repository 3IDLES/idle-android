plugins {
    id("care.android.feature-compose")
}

android {
    namespace = "com.idle.signup"
}

dependencies {
    implementation(projects.feature.postcode)
}