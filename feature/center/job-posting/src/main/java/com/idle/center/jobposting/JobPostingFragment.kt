package com.idle.center.jobposting

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareButtonStrokeSmall
import com.idle.designsystem.compose.component.CareProgressBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopAppBar
import com.idle.domain.model.job.DayOfWeek
import com.idle.domain.model.job.PayType
import com.idle.post.code.PostCodeFragment
import com.idle.signup.center.step.TimePaymentScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class JobPostingFragment : BaseComposeFragment() {

    override val fragmentViewModel: JobPostingViewModel by viewModels()

    private val postCodeDialog: PostCodeFragment? by lazy {
        PostCodeFragment().apply {
            onDismissCallback = {
                findNavController().currentBackStackEntry?.savedStateHandle?.let {
                    val roadNameAddress = it.get<String>("roadNameAddress")
                    val lotNumberAddress = it.get<String>("lotNumberAddress")

                    fragmentViewModel.setRoadNameAddress(roadNameAddress ?: "")
                    fragmentViewModel.setLotNumberAddress(lotNumberAddress ?: "")
                }
            }
        }
    }

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val jobPostingStep by registerProcess.collectAsStateWithLifecycle()
            val weekDays by weekDays.collectAsStateWithLifecycle()
            val payType by payType.collectAsStateWithLifecycle()
            val payAmount by payAmount.collectAsStateWithLifecycle()

            JobPostingScreen(
                weekDays = weekDays,
                payType = payType,
                payAmount = payAmount,
                jobPostingStep = jobPostingStep,
                setWeekDays = ::setWeekDays,
                setPayType = ::setPayType,
                setPayAmount = ::setPayAmount,
                showPostCodeDialog = {
                    if (!(postCodeDialog?.isAdded == true || postCodeDialog?.isVisible == true)) {
                        postCodeDialog?.show(parentFragmentManager, "PostCodeFragment")
                    }
                },
                setJobPostingStep = ::setJobPostingStep,
            )
        }
    }
}

@Composable
internal fun JobPostingScreen(
    weekDays: Set<DayOfWeek>,
    payType: PayType,
    payAmount: String,
    jobPostingStep: JobPostingStep,
    setWeekDays: (DayOfWeek) -> Unit,
    setPayType: (PayType) -> Unit,
    setPayAmount: (String) -> Unit,
    showPostCodeDialog: () -> Unit,
    setJobPostingStep: (JobPostingStep) -> Unit,
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            CareSubtitleTopAppBar(
                title = if (jobPostingStep != JobPostingStep.SUMMARY) "공고 등록" else "",
                onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                leftComponent = {
                    if (jobPostingStep == JobPostingStep.SUMMARY) {
                        CareButtonStrokeSmall(
                            text = "공고 수정하기",
                            onClick = { },
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp)
            )
        },
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(paddingValue)
                .padding(start = 20.dp, end = 20.dp, top = 8.dp),
        ) {
            AnimatedVisibility(
                visible = jobPostingStep != JobPostingStep.SUMMARY,
                enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
            ) {
                CareProgressBar(
                    currentStep = jobPostingStep.step,
                    totalSteps = JobPostingStep.entries.size - 1,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            CareStateAnimator(
                targetState = jobPostingStep,
                label = "센터 정보 입력을 관리하는 애니메이션",
            ) { jobPostingStep ->
                when (jobPostingStep) {
                    JobPostingStep.TIMEPAYMENT -> TimePaymentScreen(
                        weekDays = weekDays,
                        payType = payType,
                        payAmount = payAmount,
                        setWeekDays = setWeekDays,
                        setPayType = setPayType,
                        setPayAmount = setPayAmount,
                        setJobPostingStep = setJobPostingStep,
                    )

                    JobPostingStep.ADDRESS -> {}
                    JobPostingStep.CUSTOMERINFORMATION -> {}
                    JobPostingStep.CUSTOMERREQUIREMENT -> {}
                    JobPostingStep.ADDITIONALINFO -> {}
                    JobPostingStep.SUMMARY -> {}
                }
            }
        }
    }
}