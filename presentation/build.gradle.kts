plugins {
    id("care.android.feature-binding")
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "com.idle.presentation"
}

dependencies {
    implementation(projects.core.analytics)
    implementation(projects.core.commonUi.compose)
    implementation(projects.feature.auth)
    implementation(projects.feature.postcode)
    implementation(projects.feature.notification)
    implementation(projects.feature.signin)
    implementation(projects.feature.signup)
    implementation(projects.feature.setting)
    implementation(projects.feature.withdrawal)
    implementation(projects.feature.jobPostingDetail)
    implementation(projects.feature.center.home)
    implementation(projects.feature.center.pending)
    implementation(projects.feature.center.applicantInquiry)
    implementation(projects.feature.center.profile)
    implementation(projects.feature.center.registerInfo)
    implementation(projects.feature.center.jobPostingPost)
    implementation(projects.feature.center.jobPostingEdit)
    implementation(projects.feature.center.chatting)
    implementation(projects.feature.worker.home)
    implementation(projects.feature.worker.profile)
    implementation(projects.feature.worker.jobPosting)
    implementation(projects.feature.worker.chatting)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.ui)
}