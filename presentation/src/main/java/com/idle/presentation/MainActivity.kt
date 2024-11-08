package com.idle.presentation

import android.Manifest
import android.animation.Animator
import android.animation.ValueAnimator
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_WIFI_SETTINGS
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.idle.auth.AuthFragmentDirections
import com.idle.binding.MainEvent
import com.idle.binding.repeatOnStarted
import com.idle.designsystem.binding.component.dismissToast
import com.idle.designsystem.binding.component.showToast
import com.idle.navigation.deepLinkNavigateTo
import com.idle.presentation.databinding.ActivityMainBinding
import com.idle.presentation.forceupdate.ForceUpdateFragment
import com.idle.presentation.network.NetworkObserver
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
import com.kakao.sdk.template.model.Social
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var networkObserver: NetworkObserver
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
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.apply {
            repeatOnStarted {
                networkObserver.networkState.collect { state ->
                    if (state == NetworkState.NOT_CONNECTED) {
                        showNetworkDialog()
                    } else {
                        dismissNetworkDialog()
                        getForceUpdateInfo()
                    }
                }
            }

            repeatOnStarted {
                forceUpdate.collect {
                    it?.let { info ->
                        val nowVersion = packageManager.getPackageInfo(packageName, 0).versionName
                        val minAppVersion = info.minVersion
                        val shouldUpdate = checkShouldUpdate(nowVersion, minAppVersion)

                        if (shouldUpdate) {
                            forceUpdateFragment = ForceUpdateFragment(info).apply {
                                isCancelable = false
                            }
                            forceUpdateFragment.show(
                                supportFragmentManager,
                                forceUpdateFragment.tag
                            )
                        }
                    }
                }
            }

            askNotificationPermission()

            repeatOnStarted {
                navigationMenuType.collect { menuType ->
                    this@MainActivity.setNavigationMenuType(menuType)
                    delay(310L)
                }
            }

            repeatOnStarted {
                eventFlow.collect {
                    when (it) {
                        is MainEvent.ShareJobPosting -> shareJobPosting()
                        is MainEvent.DismissToast -> dismissToast()
                        is MainEvent.ShowToast -> showToast(
                            context = this@MainActivity,
                            msg = it.msg,
                            toastType = it.toastType,
                            paddingBottom = calculateSnackBarBottomPadding(),
                        )
                    }
                }
            }

            repeatOnStarted {
                navigationHelper.navigationFlow.collect { navigationEvent ->
                    when (navigationEvent) {
                        is com.idle.navigation.NavigationEvent.NavigateTo -> navController.deepLinkNavigateTo(
                            context = this@MainActivity,
                            deepLinkDestination = navigationEvent.destination,
                            popUpTo = navigationEvent.popUpTo,
                        )

                        is com.idle.navigation.NavigationEvent.NavigateToAuthWithClearBackStack -> navController.navigate(
                            AuthFragmentDirections.actionGlobalNavAuth(
                                navigationEvent.toastMsg,
                                navigationEvent.toastType
                            )
                        )
                    }

                    dismissToast()
                }
            }

            binding.apply {
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.main_FCV) as NavHostFragment
                navController = navHostFragment.navController

                mainBNVCenter.itemIconTintList = null
                mainBNVWorker.itemIconTintList = null
            }

            setDestinationListener()

            navigationHelper.handleFCMNavigate(
                isColdStart = true,
                extras = intent?.extras ?: run {
                    initializeUserSession()
                    return
                },
                onInit = ::initializeUserSession,
            )
        }
    }

    override fun onResume() {
        super.onResume()
        networkObserver.checkNetworkState()
        if (networkObserver.networkState.value == NetworkState.CONNECTED) {
            viewModel.connectWebSocket()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        viewModel.navigationHelper.handleFCMNavigate(
            isColdStart = false,
            extras = intent?.extras ?: return,
            onInit = viewModel::initializeUserSession,
        )
    }

    override fun onPause() {
        super.onPause()
        networkObserver.unsubscribeNetworkCallback()
        if (networkObserver.networkState.value == NetworkState.CONNECTED) {
            viewModel.disconnectWebSocket()
        }
    }

    private fun showNetworkDialog() {
        networkDialog?.show() ?: run {
            networkDialog = AlertDialog.Builder(this@MainActivity).apply {
                setTitle("인터넷이 연결되어 있지 않아요")
                setMessage("Wi-Fi 또는 데이터 연결을 확인한 후 다시 시도해 주세요.")
                setPositiveButton("설정") { _, _ ->
                    startActivity(Intent(ACTION_WIFI_SETTINGS))
                }
                setNegativeButton("종료") { _, _ ->
                    finish()
                }
                setCancelable(false)
            }.show()
        }
    }

    private fun dismissNetworkDialog() {
        networkDialog?.dismiss()
        networkDialog = null
    }

    private fun setDestinationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.apply {
                val navMenuType = when (destination.id) {
                    in centerBottomNavDestinationIds -> NavigationMenuType.CENTER
                    in workerBottomNavDestinationIds -> NavigationMenuType.WORKER
                    else -> NavigationMenuType.HIDE
                }

                viewModel.setNavigationMenuType(navMenuType)
            }
        }
    }

    private fun setNavigationMenuType(menuType: NavigationMenuType) {
        when (menuType) {
            NavigationMenuType.CENTER -> binding.apply {
                if (mainBNVWorker.visibility == View.VISIBLE) {
                    slideDown(mainBNVWorker)
                }
                if (mainBNVCenter.visibility != View.VISIBLE) {
                    slideUp(mainBNVCenter)
                }
                mainBNVCenter.setupWithNavController(navController)
            }

            NavigationMenuType.WORKER -> binding.apply {
                if (mainBNVCenter.visibility == View.VISIBLE) {
                    slideDown(mainBNVCenter)
                }
                if (mainBNVWorker.visibility != View.VISIBLE) {
                    slideUp(mainBNVWorker)
                }
                mainBNVWorker.setupWithNavController(navController)
            }

            NavigationMenuType.HIDE -> binding.apply {
                if (mainBNVCenter.visibility == View.VISIBLE) {
                    slideDown(mainBNVCenter)
                }
                if (mainBNVWorker.visibility == View.VISIBLE) {
                    slideDown(mainBNVWorker)
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

        // 버전 비교 (메이저, 마이너, 패치 순으로 비교)
        for (i in 0..2) {
            if (current[i] < min[i]) return true
            if (current[i] > min[i]) return false
        }
        return false
    }

    private fun normalizeVersion(version: String): List<Int> =
        version.split('.')
            .map { it.toIntOrNull() ?: 0 }
            .let {
                when (it.size) {
                    2 -> it + listOf(0) // 1.0 -> 1.0.0 형태로 변환
                    else -> it
                }
            }

    private fun slideUp(view: View) {
        view.measure(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val targetHeight = view.measuredHeight
        view.layoutParams.height = 0

        val slide = ValueAnimator.ofInt(0, targetHeight)
        slide.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams
            layoutParams.height = animatedValue
            view.layoutParams = layoutParams
        }
        slide.duration = 300
        slide.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                view.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {
                view.isClickable = true
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        slide.start()
    }

    private fun slideDown(view: View) {
        val initialHeight = view.measuredHeight

        val slide = ValueAnimator.ofInt(initialHeight, 0)
        slide.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams
            layoutParams.height = animatedValue
            view.layoutParams = layoutParams
        }
        slide.duration = 300
        slide.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                view.visibility = View.GONE
                view.isClickable = false
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        slide.start()
    }

    private fun shareJobPosting() {
        val defaultFeed = FeedTemplate(
            content = Content(
                title = "오늘의 디저트",
                description = "#케익 #딸기 #삼평동 #카페 #분위기 #소개팅",
                imageUrl = "https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
                link = Link(
                    webUrl = "https://developers.kakao.com",
                    mobileWebUrl = "https://developers.kakao.com"
                )
            ),
            itemContent = ItemContent(
                profileText = "Kakao",
                profileImageUrl = "https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
                titleImageUrl = "https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
                titleImageText = "Cheese cake",
                titleImageCategory = "Cake",
                items = listOf(
                    ItemInfo(item = "cake1", itemOp = "1000원"),
                    ItemInfo(item = "cake2", itemOp = "2000원"),
                    ItemInfo(item = "cake3", itemOp = "3000원"),
                    ItemInfo(item = "cake4", itemOp = "4000원"),
                    ItemInfo(item = "cake5", itemOp = "5000원")
                ),
                sum = "Total",
                sumOp = "15000원"
            ),
            social = Social(
                likeCount = 286,
                commentCount = 45,
                sharedCount = 845
            ),
            buttons = listOf(
                Button(
                    "웹으로 보기",
                    Link(
                        webUrl = "https://developers.kakao.com",
                        mobileWebUrl = "https://developers.kakao.com"
                    )
                ),
                Button(
                    "앱으로 보기",
                    Link(
                        androidExecutionParams = mapOf("key1" to "value1", "key2" to "value2"),
                        iosExecutionParams = mapOf("key1" to "value1", "key2" to "value2")
                    )
                )
            )
        )

        // 카카오톡 설치여부 확인
        if (ShareClient.instance.isKakaoTalkSharingAvailable(this)) {
            // 카카오톡으로 카카오톡 공유 가능
            ShareClient.instance.shareDefault(this, defaultFeed) { sharingResult, error ->
                if (error != null) {
                    Log.e("test", "카카오톡 공유 실패", error)
                } else if (sharingResult != null) {
                    Log.d("test", "카카오톡 공유 성공 ${sharingResult.intent}")
                    startActivity(sharingResult.intent)

                    // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    Log.w("test", "Warning Msg: ${sharingResult.warningMsg}")
                    Log.w("test", "Argument Msg: ${sharingResult.argumentMsg}")
                }
            }
        } else {
            // 카카오톡 미설치: 웹 공유 사용 권장
            // 웹 공유 예시 코드
            val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultFeed)

            // CustomTabs으로 웹 브라우저 열기

            // 1. CustomTabsServiceConnection 지원 브라우저 열기
            // ex) Chrome, 삼성 인터넷, FireFox, 웨일 등
            try {
                KakaoCustomTabsClient.openWithDefault(this, sharerUrl)
            } catch (e: UnsupportedOperationException) {
                // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
            }

            // 2. CustomTabsServiceConnection 미지원 브라우저 열기
            // ex) 다음, 네이버 등
            try {
                KakaoCustomTabsClient.open(this, sharerUrl)
            } catch (e: ActivityNotFoundException) {
                // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
            }
        }
    }
}
