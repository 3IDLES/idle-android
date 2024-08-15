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
    implementation(projects.feature.setting)
    implementation(projects.feature.withdrawal)
    implementation(projects.feature.jobPostingDetail)
    implementation(projects.feature.center.home)
    implementation(projects.feature.center.applicantInquiry)
    implementation(projects.feature.center.profile)
    implementation(projects.feature.center.registerInfo)
    implementation(projects.feature.center.jobPostingPost)
    implementation(projects.feature.center.jobPostingEdit)
    implementation(projects.feature.worker.home)
    implementation(projects.feature.worker.profile)
    implementation(libs.androidx.navigation.ui)
}