plugins {
    id("care.android.feature-binding")
}

android {
    namespace = "com.idle.presentation"
}

dependencies {
    implementation(projects.feature.auth)
    implementation(projects.feature.postcode)
    implementation(projects.feature.signin)
    implementation(projects.feature.signup)
    implementation(projects.feature.center.home)
    implementation(projects.feature.center.applicantInquiry)
    implementation(projects.feature.center.profile)
    implementation(projects.feature.center.setting)
    implementation(projects.feature.center.registerInfo)
    implementation(projects.feature.center.jobPosting)
    implementation(projects.feature.worker.home)
    implementation(projects.feature.worker.profile)
    implementation(projects.feature.worker.jobPostingDetail)
    implementation(libs.androidx.navigation.ui)
}