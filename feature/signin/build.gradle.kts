plugins {
    id("care.android.feature-compose")
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "com.idle.signin"
}

dependencies{
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.androidx.junit)
}
