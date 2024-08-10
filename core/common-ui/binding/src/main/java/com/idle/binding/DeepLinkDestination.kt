package com.idle.binding

import android.content.Context
import androidx.annotation.IdRes
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import com.idle.designresource.R

sealed class DeepLinkDestination(
    val addressRes: Int,
    val params: Map<String, String> = emptyMap()
) {
    data object Auth : DeepLinkDestination(R.string.auth_deeplink_url)
    data object Postcode : DeepLinkDestination(R.string.postcode_deeplink_url)
    data object NewPassword : DeepLinkDestination(R.string.new_password_deeplink_url)

    data object CenterAuth : DeepLinkDestination(R.string.center_auth_deeplink_url)
    data object CenterSignIn : DeepLinkDestination(R.string.center_signin_deeplink_url)
    data object CenterSignUp : DeepLinkDestination(R.string.center_signup_deeplink_url)
    data object CenterHome : DeepLinkDestination(R.string.center_home_deeplink_url)
    data object CenterProfile : DeepLinkDestination(R.string.center_profile_deeplink_url)
    data object CenterSetting : DeepLinkDestination(R.string.center_setting_deeplink_url)
    data object CenterRegister : DeepLinkDestination(R.string.center_register_info_deeplink_url)
    data object CenterRegisterComplete :
        DeepLinkDestination(R.string.center_register_info_complete_deeplink_url)

    data class CenterApplicantInquiry(val jobPostingId: String) :
        DeepLinkDestination(
            addressRes = R.string.center_applicant_inquiry_deeplink_url,
            params = mapOf("jobPostingId" to jobPostingId),
        )

    data object CenterJobPosting : DeepLinkDestination(R.string.center_job_posting_deeplink_url)
    data object CenterJobPostingComplete :
        DeepLinkDestination(R.string.center_job_posting_complete_deeplink_url)

    data object WorkerHome : DeepLinkDestination(R.string.worker_home_deeplink_url)
    data object WorkerProfile : DeepLinkDestination(R.string.worker_profile_deeplink_url)
    data object WorkerJobDetail :
        DeepLinkDestination(R.string.worker_recruitment_detail_deeplink_url)

    data object WorkerAuth : DeepLinkDestination(R.string.worker_auth_deeplink_url)
    data object WorkerSignUp : DeepLinkDestination(R.string.worker_signup_deeplink_url)
}

fun DeepLinkDestination.getDeepLink(context: Context): String {
    val baseUrl = context.getString(this.addressRes)

    return if (params.isNotEmpty()) {
        params.entries.fold(baseUrl) { acc, param ->
            acc.replace("{${param.key}}", param.value)
        }
    } else {
        baseUrl
    }
}

fun NavController.deepLinkNavigateTo(
    context: Context,
    deepLinkDestination: DeepLinkDestination,
    @IdRes popUpTo: Int? = null,
) {
    val builder = NavOptions.Builder()

    popUpTo?.let {
        builder.setPopUpTo(it, true)
    }

    if (deepLinkDestination == DeepLinkDestination.CenterJobPostingComplete) {
        builder.setEnterAnim(R.anim.anim_slide_in_horizontally)
            .setExitAnim(R.anim.anim_slide_out_horizontally)
    }

    navigate(
        buildDeppLink(context, deepLinkDestination),
        builder.build()
    )
}

private fun buildDeppLink(context: Context, destination: DeepLinkDestination) =
    NavDeepLinkRequest.Builder
        .fromUri(destination.getDeepLink(context).toUri())
        .build()