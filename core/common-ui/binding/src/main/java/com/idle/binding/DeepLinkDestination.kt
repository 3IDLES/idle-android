package com.idle.binding

import android.content.Context
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import com.idle.common.ui.binding.R

// DeepLinkDestination.kt
sealed class DeepLinkDestination(val addressRes: Int) {
    data object Auth : DeepLinkDestination(R.string.auth_deeplink_url)

    data object CenterAuth : DeepLinkDestination(R.string.center_auth_deeplink_url)
    data object CenterSignIn : DeepLinkDestination(R.string.center_signin_deeplink_url)
    data object CenterSignUp : DeepLinkDestination(R.string.center_signup_deeplink_url)
    data object NewPassword : DeepLinkDestination(R.string.new_password_deeplink_url)

    data object WorkerAuth : DeepLinkDestination(R.string.worker_auth_deeplink_url)
    data object WorkerSignIn : DeepLinkDestination(R.string.worker_signin_deeplink_url)
    data object WorkerSignUp : DeepLinkDestination(R.string.worker_signup_deeplink_url)

    data object CenterHome: DeepLinkDestination(R.string.center_home_deeplink_url)
    data object CenterProfile: DeepLinkDestination(R.string.center_profile_deeplink_url)
}

fun DeepLinkDestination.getDeepLink(context: Context) = context.getString(this.addressRes)

fun NavController.deepLinkNavigateTo(
    context: Context,
    deepLinkDestination: DeepLinkDestination,
    popUpTo: Boolean = false,
) {
    val builder = NavOptions.Builder()

    if (popUpTo) {
        builder.setPopUpTo(graph.startDestinationId, true)
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