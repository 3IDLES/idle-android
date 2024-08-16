plugins {
    id("care.android.feature-compose")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "com.idle.center.job.posting.post"
}

dependencies {
    implementation(projects.feature.postcode)
    implementation(projects.feature.jobPostingDetail)
    implementation(projects.feature.center.jobPostingEdit)
    implementation(project(":feature:job-posting-detail"))
}