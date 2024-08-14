package com.idle.binding

import android.content.Context
import androidx.annotation.IdRes
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import com.idle.designresource.R
import com.idle.domain.model.auth.UserRole

sealed class DeepLinkDestination(
    val addressRes: Int,
    val params: Map<String, String> = emptyMap(),
    val setDefaultAnimation: Boolean = true,
) {
    object Auth : DeepLinkDestination(
        addressRes = R.string.auth_deeplink_url,
        setDefaultAnimation = false,
    )

    object Postcode : DeepLinkDestination(
        addressRes = R.string.postcode_deeplink_url,
        setDefaultAnimation = false,
    )

    object NewPassword : DeepLinkDestination(
        addressRes = R.string.new_password_deeplink_url
    )

    object CenterSignIn : DeepLinkDestination(
        addressRes = R.string.center_signin_deeplink_url
    )

    object CenterSignUp : DeepLinkDestination(
        addressRes = R.string.center_signup_deeplink_url
    )

    object CenterHome : DeepLinkDestination(
        addressRes = R.string.center_home_deeplink_url,
        setDefaultAnimation = false,
    )

    object CenterProfile : DeepLinkDestination(
        addressRes = R.string.center_profile_deeplink_url
    )

    object CenterSetting : DeepLinkDestination(
        addressRes = R.string.center_setting_deeplink_url
    )

    object CenterRegister : DeepLinkDestination(
        addressRes = R.string.center_register_info_deeplink_url
    )

    object CenterRegisterComplete : DeepLinkDestination(
        addressRes = R.string.center_register_info_complete_deeplink_url
    )

    data class CenterApplicantInquiry(val jobPostingId: String) : DeepLinkDestination(
        addressRes = R.string.center_applicant_inquiry_deeplink_url,
        params = mapOf("jobPostingId" to jobPostingId)
    )

    object CenterJobPosting : DeepLinkDestination(
        addressRes = R.string.center_job_posting_deeplink_url
    )

    object CenterJobPostingComplete : DeepLinkDestination(
        addressRes = R.string.center_job_posting_complete_deeplink_url
    )

    object WorkerHome : DeepLinkDestination(
        addressRes = R.string.worker_home_deeplink_url,
        setDefaultAnimation = false
    )

    object WorkerProfile : DeepLinkDestination(
        addressRes = R.string.worker_profile_deeplink_url
    )

    data class WorkerJobDetail(
        val jobPostingId: String,
        val userRole: UserRole,
    ) : DeepLinkDestination(
        addressRes = R.string.worker_recruitment_detail_deeplink_url,
        params = mapOf(
            "jobPostingId" to jobPostingId,
            "userRole" to userRole.name
        )
    )

    object WorkerSignUp : DeepLinkDestination(
        addressRes = R.string.worker_signup_deeplink_url
    )
}

private fun DeepLinkDestination.getDeepLink(context: Context): String {
    val baseUrl = context.getString(this.addressRes)

    return params.entries.fold(baseUrl) { acc, param ->
        acc.replace("{${param.key}}", param.value)
    }
}

fun NavController.deepLinkNavigateTo(
    context: Context,
    deepLinkDestination: DeepLinkDestination,
    @IdRes popUpTo: Int? = null,
) {
    val navOptions = buildNavOptions(deepLinkDestination, popUpTo)
    val deepLinkRequest = buildDeepLinkRequest(context, deepLinkDestination)

    navigate(deepLinkRequest, navOptions)
}

private fun buildNavOptions(destination: DeepLinkDestination, @IdRes popUpTo: Int?): NavOptions {
    return NavOptions.Builder().apply {
        popUpTo?.let { setPopUpTo(it, true) }

        if (destination.setDefaultAnimation) {
            setEnterAnim(R.anim.anim_slide_in_horizontally)
            setExitAnim(R.anim.anim_slide_out_horizontally)
            setPopEnterAnim(R.anim.anim_pop_slide_in_horizontally)
            setPopExitAnim(R.anim.anim_pop_slide_out_horizontally)
        }

        if (destination == DeepLinkDestination.Auth){
            setEnterAnim(R.anim.anim_pop_slide_in_horizontally)
            setExitAnim(R.anim.anim_slide_out_horizontally)
            setPopEnterAnim(R.anim.anim_pop_slide_in_horizontally)
            setPopExitAnim(R.anim.anim_pop_slide_out_horizontally)
        }
    }.build()
}

private fun buildDeepLinkRequest(context: Context, destination: DeepLinkDestination): NavDeepLinkRequest {
    return NavDeepLinkRequest.Builder
        .fromUri(destination.getDeepLink(context).toUri())
        .build()
}