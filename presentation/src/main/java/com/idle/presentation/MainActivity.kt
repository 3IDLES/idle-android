package com.idle.presentation

import android.Manifest
import android.animation.Animator
import android.animation.ValueAnimator
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_WIFI_SETTINGS
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.appsflyer.AppsFlyerLib
import com.appsflyer.deeplink.DeepLinkResult
import com.idle.analytics.AnalyticsEvent
import com.idle.analytics.AnalyticsHelper
import com.idle.auth.AuthFragmentDirections
import com.idle.binding.MainEvent
import com.idle.binding.ShareJobPostingInfo
import com.idle.binding.repeatOnStarted
import com.idle.designsystem.binding.component.dismissToast
import com.idle.designsystem.binding.component.showToast
import com.idle.domain.model.config.ForceUpdate
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.jobposting.JobPostingType
import com.idle.domain.model.jobposting.SharedJobPostingInfo
import com.idle.navigation.NavigationEvent.To
import com.idle.navigation.NavigationEvent.NavigateToAuthWithClearBackStack
import com.idle.navigation.deepLinkNavigateTo
import com.idle.presentation.databinding.ActivityMainBinding
import com.idle.presentation.forceupdate.ForceUpdateFragment
import com.idle.presentation.network.NetworkMonitor
import com.idle.presentation.network.NetworkState
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.ItemContent
import com.kakao.sdk.template.model.ItemInfo
import com.kakao.sdk.template.model.Link
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    lateinit var errorHelper: ErrorHelper

    private lateinit var forceUpdateFragment: ForceUpdateFragment
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var networkDialog: AlertDialog? = null
    private val viewModel: MainViewModel by viewModels()

    private val centerBottomNavDestinationIds: Set<Int> by lazy {
        resources.obtainTypedArray(R.array.centerNavDestinationIds).let { typedArray ->
            val destinationIds = mutableSetOf<Int>()

            for (index in 0 until typedArray.length()) {
                destinationIds.add(typedArray.getResourceId(index, 0))
            }

            typedArray.recycle()
            destinationIds
        }
    }

    private val workerBottomNavDestinationIds: Set<Int> by lazy {
        resources.obtainTypedArray(R.array.workerNavDestinationIds).let { typedArray ->
            val destinationIds = mutableSetOf<Int>()

            for (index in 0 until typedArray.length()) {
                destinationIds.add(typedArray.getResourceId(index, 0))
            }

            typedArray.recycle()
            destinationIds
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigationController()
        askNotificationPermission()
        setDestinationListener()
        observeViewModel()
        handleDeepLinking()

        viewModel.apply {
            navigationHelper.handleFCMNavigate(
                isColdStart = true,
                extras = intent?.extras ?: run {
                    initializeUserSession()
                    return
                },
                onInit = ::initializeUserSession,
                readNotification = ::readNotification,
            )
        }
    }

    override fun onStart() {
        super.onStart()
        if (networkMonitor.networkState.value != NetworkState.NotConnected) {
            viewModel.connectWebSocket()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.disconnectWebSocket()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        viewModel.navigationHelper.handleFCMNavigate(
            isColdStart = false,
            extras = intent?.extras ?: return,
            onInit = viewModel::initializeUserSession,
            readNotification = viewModel::readNotification,
        )
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setupNavigationController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_FCV) as NavHostFragment
        navController = navHostFragment.navController
        binding.mainBNVCenter.itemIconTintList = null
        binding.mainBNVWorker.itemIconTintList = null
        setDestinationListener()
    }

    private fun observeViewModel() {
        viewModel.apply {
            repeatOnStarted {
                networkMonitor.networkState.collect { handleNetworkState(it) }
            }
            repeatOnStarted {
                forceUpdate.collect { it?.let { showForceUpdateDialog(it) } }
            }
            repeatOnStarted {
                navigationMenuType.collect { this@MainActivity.setNavigationMenuType(it) }
            }
            repeatOnStarted {
                eventFlow.collect { handleMainEvent(it) }
            }
            repeatOnStarted {
                navigationHelper.navigationFlow.collect { handleNavigationEvent(it) }
            }
        }
    }

    private fun handleNetworkState(state: NetworkState) {
        if (state == NetworkState.NotConnected) {
            showNetworkDialog()
        } else {
            dismissNetworkDialog()
            viewModel.getForceUpdateInfo()
        }
    }

    private fun showForceUpdateDialog(info: ForceUpdate) {
        val currentVersion = packageManager.getPackageInfo(packageName, 0).versionName
        if (checkShouldUpdate(currentVersion, info.minVersion)) {
            forceUpdateFragment = ForceUpdateFragment(info).apply { isCancelable = false }
            forceUpdateFragment.show(supportFragmentManager, forceUpdateFragment.tag)
        }
    }

    private fun handleMainEvent(event: MainEvent) {
        when (event) {
            is MainEvent.ShareJobPosting -> shareJobPosting(event.shareJobPostingInfo)
            is MainEvent.DismissToast -> dismissToast()
            is MainEvent.ShowToast -> showToast(
                context = this,
                msg = event.msg,
                toastType = event.toastType,
                paddingBottom = calculateSnackBarBottomPadding()
            )
        }
    }

    private fun handleNavigationEvent(navigationEvent: com.idle.navigation.NavigationEvent) {
        when (navigationEvent) {
            is To -> navController.deepLinkNavigateTo(
                context = this,
                deepLinkDestination = navigationEvent.destination,
                popUpTo = navigationEvent.popUpTo
            )

            is NavigateToAuthWithClearBackStack -> navController.navigate(
                AuthFragmentDirections.actionGlobalNavAuth(
                    toastMsg = navigationEvent.toastMsg,
                    toastType = navigationEvent.toastType
                )
            )
        }
        dismissToast()
    }

    private fun handleDeepLinking() {
        AppsFlyerLib.getInstance().subscribeForDeepLink { deepLinkResult ->
            when (deepLinkResult.status) {
                DeepLinkResult.Status.FOUND -> {
                    val sharedJobPostingId =
                        deepLinkResult.deepLink.getStringValue("deep_link_value")
                    val sharedJobPostingType =
                        deepLinkResult.deepLink.getStringValue("deep_link_sub1")

                    handleDeepLink(sharedJobPostingId, sharedJobPostingType)
                }

                DeepLinkResult.Status.NOT_FOUND -> errorHelper.logError(Exception("AppsFlyer User Not Found"))
                else -> errorHelper.logError(Exception(deepLinkResult.error.toString()))
            }
        }
    }

    private fun handleDeepLink(sharedJobPostingId: String?, sharedJobPostingType: String?) {
        viewModel.setSharedJobPostingInfo(
            SharedJobPostingInfo(
                jobPostingId = sharedJobPostingId ?: return,
                jobPostingType = JobPostingType.create(sharedJobPostingType ?: return)
            )
        )
    }

    private fun showNetworkDialog() {
        if (networkDialog == null) {
            networkDialog = AlertDialog.Builder(this).apply {
                setTitle("인터넷이 연결되어 있지 않아요")
                setMessage("Wi-Fi 또는 데이터 연결을 확인한 후 다시 시도해 주세요.")
                setPositiveButton("설정") { _, _ -> startActivity(Intent(ACTION_WIFI_SETTINGS)) }
                setNegativeButton("종료") { _, _ -> finish() }
                setCancelable(false)
            }.create()
        }
        networkDialog?.show()
    }

    private fun dismissNetworkDialog() {
        networkDialog?.dismiss()
        networkDialog = null
    }

    private fun setDestinationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val navMenuType = when (destination.id) {
                in centerBottomNavDestinationIds -> NavigationMenuType.CENTER
                in workerBottomNavDestinationIds -> NavigationMenuType.WORKER
                else -> NavigationMenuType.HIDE
            }
            viewModel.setNavigationMenuType(navMenuType)
        }
    }

    private fun setNavigationMenuType(menuType: NavigationMenuType) {
        binding.apply {
            when (menuType) {
                NavigationMenuType.CENTER -> {
                    if (mainBNVWorker.isVisible) slideDown(mainBNVWorker)
                    if (!mainBNVCenter.isVisible) slideUp(mainBNVCenter)
                    mainBNVCenter.setupWithNavController(navController)
                }

                NavigationMenuType.WORKER -> {
                    if (mainBNVCenter.isVisible) slideDown(mainBNVCenter)
                    if (!mainBNVWorker.isVisible) slideUp(mainBNVWorker)
                    mainBNVWorker.setupWithNavController(navController)
                }

                NavigationMenuType.HIDE -> {
                    if (mainBNVCenter.isVisible) slideDown(mainBNVCenter)
                    if (mainBNVWorker.isVisible) slideDown(mainBNVWorker)
                }
            }
        }
    }

    private fun calculateSnackBarBottomPadding() = when (navController.currentDestination?.id) {
        // Padding 104dp
        com.idle.auth.R.id.authFragment,
        com.idle.signup.R.id.centerSignUpFragment,
        com.idle.signup.R.id.workerSignUpFragment,
        com.idle.signin.R.id.newPasswordFragment,
        com.idle.center.pending.R.id.centerPendingFragment,
        com.idle.center.register.info.R.id.registerCenterInfoFragment,
        com.idle.center.register.info.R.id.registerCenterInfoCompleteFragment,
        com.idle.center.job.posting.post.R.id.jobPostingPostFragment,
        com.idle.center.job.posting.post.R.id.jobPostingPostCompleteFragment,
        com.idle.job.posting.detail.R.id.centerJobPostingDetailFragment,
        com.idle.job.posting.detail.R.id.workerJobPostingDetailFragment -> 104

        // Padding 84dp
        com.idle.center.home.R.id.centerHomeFragment,
        com.idle.setting.R.id.centerSettingFragment,
        com.idle.worker.home.R.id.workerHomeFragment,
        com.idle.setting.R.id.workerSettingFragment,
        com.idle.worker.job.posting.R.id.workerJobPostingFragment -> 84

        // Padding 20dp
        com.idle.center.applicant.inquiry.R.id.applicantInquiryFragment,
        com.idle.center.profile.R.id.centerProfileFragment,
        com.idle.worker.profile.R.id.workerProfileFragment,
        com.idle.notification.R.id.notificationFragment -> 20

        // Padding 140dp
        com.idle.signin.R.id.centerSignInFragment,
        com.idle.withdrawal.R.id.withdrawalFragment -> 140

        // Default padding
        else -> 20
    }

    private fun checkShouldUpdate(currentVersion: String, minVersion: String): Boolean {
        val current = normalizeVersion(currentVersion)
        val min = normalizeVersion(minVersion)
        return (0..2).any { current[it] < min[it] }
    }

    private fun normalizeVersion(version: String): List<Int> =
        version.split('.').map { it.toIntOrNull() ?: 0 }.let {
            if (it.size == 2) it + 0 else it
        }


    private fun slideUp(view: View) {
        view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        animateViewHeight(view, 0, view.measuredHeight)
    }

    private fun slideDown(view: View) {
        animateViewHeight(view, view.measuredHeight, 0) {
            view.visibility = View.GONE
            view.isClickable = false
        }
    }

    private fun animateViewHeight(
        view: View,
        startHeight: Int,
        endHeight: Int,
        onEnd: (() -> Unit)? = null
    ) {
        view.layoutParams.height = startHeight
        ValueAnimator.ofInt(startHeight, endHeight).apply {
            addUpdateListener {
                view.layoutParams.height = it.animatedValue as Int
                view.requestLayout()
            }
            duration = 300
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    if (endHeight > startHeight) view.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator) {
                    onEnd?.invoke()
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
        }.start()
    }

    private fun shareJobPosting(sharedJobPostingInfo: ShareJobPostingInfo) {
        val oneLinkUrl =
            "https://caremeet.onelink.me/dXPO/edg5vvwt?deep_link_value=${sharedJobPostingInfo.id}&deep_link_sub1=${sharedJobPostingInfo.type}"

        val jobPostingFeed = FeedTemplate(
            content = Content(
                title = sharedJobPostingInfo.centerName,
                description = sharedJobPostingInfo.centerOfficeNumber,
                imageUrl = "https://idle-prod-bucket.s3.ap-northeast-2.amazonaws.com/assets/caremeet-share.png",
                link = Link(webUrl = oneLinkUrl, mobileWebUrl = oneLinkUrl)
            ),
            itemContent = ItemContent(
                profileText = "케어밋에서 아래의 일자리에 지원해요!",
                titleImageText = sharedJobPostingInfo.title,
                titleImageCategory = "요양 일자리",
                items = listOf(
                    ItemInfo(item = "근무 요일", itemOp = sharedJobPostingInfo.weekdays),
                    ItemInfo(item = "근무 시간", itemOp = sharedJobPostingInfo.workTime),
                    ItemInfo(item = "급여", itemOp = sharedJobPostingInfo.payAmount),
                    ItemInfo(item = "근무 주소", itemOp = sharedJobPostingInfo.roadNameAddress)
                )
            ),
            buttons = listOf(
                Button(
                    title = "앱에서 확인하기",
                    link = Link(webUrl = oneLinkUrl, mobileWebUrl = oneLinkUrl)
                )
            )
        )

        analyticsHelper.logEvent(
            AnalyticsEvent(
                type = AnalyticsEvent.Types.ACTION,
                properties = mutableMapOf("JobPostingType" to sharedJobPostingInfo.type)
            )
        )

        if (ShareClient.instance.isKakaoTalkSharingAvailable(this)) {
            ShareClient.instance.shareDefault(this, jobPostingFeed) { result, error ->
                if (error != null) errorHelper.logError(Exception(error))
                else result?.let { startActivity(it.intent) }
            }
        } else {
            openWebSharer(WebSharerClient.instance.makeDefaultUrl(jobPostingFeed))
        }
    }

    private fun openWebSharer(url: Uri) {
        try {
            KakaoCustomTabsClient.openWithDefault(this, url)
        } catch (e: UnsupportedOperationException) {
            try {
                KakaoCustomTabsClient.open(this, url)
            } catch (e: ActivityNotFoundException) {
                errorHelper.logError(Exception(e))
            }
        }
    }
}
