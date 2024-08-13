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
    val params: Map<String, String> = emptyMap(),
    val setAnimation: Boolean = false,
) {
    data object Auth : DeepLinkDestination(R.string.auth_deeplink_url)
    data object Postcode : DeepLinkDestination(R.string.postcode_deeplink_url)
    data object NewPassword : DeepLinkDestination(
        addressRes = R.string.new_password_deeplink_url,
        setAnimation = true,
    )

    data object CenterSignIn :
        DeepLinkDestination(
            addressRes = R.string.center_signin_deeplink_url,
            setAnimation = true,
        )

    data object CenterSignUp : DeepLinkDestination(
        addressRes = R.string.center_signup_deeplink_url,
        setAnimation = true,
    )

    data object CenterHome : DeepLinkDestination(addressRes = R.string.center_home_deeplink_url)

    data object CenterProfile : DeepLinkDestination(
        R.string.center_profile_deeplink_url,
        setAnimation = true,
    )

    data object CenterSetting : DeepLinkDestination(
        R.string.center_setting_deeplink_url,
        setAnimation = true,
    )

    data object CenterRegister : DeepLinkDestination(
        addressRes = R.string.center_register_info_deeplink_url,
        setAnimation = true,
    )

    data object CenterRegisterComplete :
        DeepLinkDestination(
            addressRes = R.string.center_register_info_complete_deeplink_url,
            setAnimation = true,
        )

    data class CenterApplicantInquiry(val jobPostingId: String) :
        DeepLinkDestination(
            addressRes = R.string.center_applicant_inquiry_deeplink_url,
            params = mapOf("jobPostingId" to jobPostingId),
            setAnimation = true,
        )

    data object CenterJobPosting : DeepLinkDestination(
        addressRes = R.string.center_job_posting_deeplink_url,
        setAnimation = true,
    )

    data object CenterJobPostingComplete :
        DeepLinkDestination(
            addressRes = R.string.center_job_posting_complete_deeplink_url,
            setAnimation = true,
        )

    data object WorkerHome : DeepLinkDestination(R.string.worker_home_deeplink_url)

    data object WorkerProfile : DeepLinkDestination(
        R.string.worker_profile_deeplink_url,
        setAnimation = true,
    )

    data object WorkerJobDetail :
        DeepLinkDestination(
            addressRes = R.string.worker_recruitment_detail_deeplink_url,
            setAnimation = true,
        )

    data object WorkerSignUp : DeepLinkDestination(
        addressRes = R.string.worker_signup_deeplink_url,
        setAnimation = true,
    )
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

    if (deepLinkDestination.setAnimation) {
        builder.setEnterAnim(R.anim.anim_slide_in_horizontally)
            .setExitAnim(R.anim.anim_slide_out_horizontally)
            .setPopEnterAnim(R.anim.anim_pop_slide_in_horizontally)
            .setPopExitAnim(R.anim.anim_pop_slide_out_horizontally)
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