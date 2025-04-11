plugins {
    id("care.android.feature-binding")
}

android {
    namespace = "com.idle.presentation"
}

dependencies {
    implementation(projects.core.analytics)
    implementation(projects.core.navigation)
    implementation(projects.core.commonUi.compose)
    implementation(projects.feature.auth)
    implementation(projects.feature.postcode)
    implementation(projects.feature.notification)
    implementation(projects.feature.signin)
    implementation(projects.feature.signup)
    implementation(projects.feature.setting)
    implementation(projects.feature.withdrawal)
    implementation(projects.feature.jobPostingDetail)
    implementation(projects.feature.chattingDetail)

    implementation(projects.feature.centerHome)
    implementation(projects.feature.centerPending)
    implementation(projects.feature.centerApplicantInquiry)
    implementation(projects.feature.centerProfile)
    implementation(projects.feature.centerRegisterInfo)
    implementation(projects.feature.centerJobPostingPost)
    implementation(projects.feature.centerJobPostingEdit)
    implementation(projects.feature.centerChatting)
    implementation(projects.feature.workerHome)
    implementation(projects.feature.workerProfile)
    implementation(projects.feature.workerJobPosting)
    implementation(projects.feature.workerChatting)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.kakao.talk)
    implementation(libs.kakao.share)
    implementation(libs.appsFlyer)
}
