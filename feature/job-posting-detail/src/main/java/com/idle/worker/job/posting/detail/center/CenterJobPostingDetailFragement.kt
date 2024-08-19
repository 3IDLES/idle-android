package com.idle.worker.job.posting.detail.center

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.navArgs
import com.idle.binding.DeepLinkDestination
import com.idle.binding.base.CareBaseEvent
import com.idle.center.job.edit.JobEditScreen
import com.idle.compose.base.BaseComposeFragment
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareButtonRound
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.jobposting.CenterJobPostingDetail
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.ZoneId

@AndroidEntryPoint
internal class CenterJobPostingDetailFragment : BaseComposeFragment() {
    private val args: CenterJobPostingDetailFragmentArgs by navArgs()
    override val fragmentViewModel: CenterJobPostingDetailViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val jobPostingId by rememberSaveable { mutableStateOf(args.jobPostingId) }
            getCenterJobPostingDetail(jobPostingId)

            val jobPostingDetail by jobPostingDetail.collectAsStateWithLifecycle()
            val isEditState by isEditState.collectAsStateWithLifecycle()

            CareStateAnimator(
                targetState = isEditState,
                transitionCondition = isEditState
            ) { state ->
                if (state) {
                    jobPostingDetail?.let {
                        JobEditScreen(
                            fragmentManager = parentFragmentManager,
                            weekDays = it.weekdays,
                            workStartTime = it.startTime,
                            workEndTime = it.endTime,
                            payType = it.payType,
                            payAmount = it.payAmount.toString(),
                            roadNameAddress = it.roadNameAddress,
                            lotNumberAddress = it.lotNumberAddress,
                            clientName = it.clientName,
                            gender = it.gender,
                            birthYear = (LocalDate.now(ZoneId.of("Asia/Seoul")).year - it.age).toString(),
                            weight = it.weight.toString(),
                            careLevel = it.careLevel.toString(),
                            mentalStatus = it.mentalStatus,
                            disease = it.disease ?: "",
                            isMealAssistance = it.isMealAssistance,
                            isBowelAssistance = it.isBowelAssistance,
                            isWalkingAssistance = it.isWalkingAssistance,
                            lifeAssistance = it.lifeAssistance,
                            extraRequirement = it.extraRequirement,
                            isExperiencePreferred = it.isExperiencePreferred,
                            applyMethod = it.applyMethod,
                            applyDeadline = it.applyDeadline,
                            applyDeadlineType = it.applyDeadlineType,
                            updateJobPosting = ::updateJobPosting,
                            setEditState = ::setEditState,
                        )
                    }
                } else {
                    CenterJobPostingDetailScreen(
                        jobPostingId = jobPostingId,
                        jobPostingDetail = jobPostingDetail,
                        navigateTo = { baseEvent(CareBaseEvent.NavigateTo(it)) },
                        setEditState = ::setEditState,
                    )
                }
            }
        }
    }

    override fun handleError(message: String) {
        TODO("Not yet implemented")
    }
}

@Composable
internal fun CenterJobPostingDetailScreen(
    jobPostingId: String,
    jobPostingDetail: CenterJobPostingDetail?,
    navigateTo: (DeepLinkDestination) -> Unit,
    setEditState: (Boolean) -> Unit,
) {
    val onBackPressedDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    jobPostingDetail?.let {
        Scaffold(
            topBar = {
                CareSubtitleTopBar(
                    title = stringResource(id = R.string.manage_job_posting),
                    onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                    leftComponent = {
                        CareButtonRound(
                            text = stringResource(id = R.string.edit_job_posting_button),
                            onClick = { setEditState(true) },
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            top = 48.dp,
                            end = 20.dp,
                            bottom = 12.dp
                        ),
                )
            },
            containerColor = CareTheme.colors.white000,
        ) { paddingValue ->
            SummaryScreen(
                weekDays = it.weekdays,
                workStartTime = it.startTime,
                workEndTime = it.endTime,
                payType = it.payType,
                payAmount = it.payAmount.toString(),
                roadNameAddress = it.roadNameAddress,
                lotNumberAddress = it.lotNumberAddress,
                clientName = it.clientName,
                gender = it.gender,
                birthYear = (LocalDate.now(ZoneId.of("Asia/Seoul")).year - it.age).toString(),
                weight = it.weight.toString(),
                careLevel = it.careLevel.toString(),
                mentalStatus = it.mentalStatus,
                disease = it.disease ?: "",
                isMealAssistance = it.isMealAssistance,
                isBowelAssistance = it.isBowelAssistance,
                isWalkingAssistance = it.isWalkingAssistance,
                lifeAssistance = it.lifeAssistance,
                extraRequirement = it.extraRequirement,
                isExperiencePreferred = it.isExperiencePreferred,
                applyMethod = it.applyMethod,
                applyDeadline = it.applyDeadline,
                onBackPressed = { onBackPressedDispatcher?.onBackPressed() },
                bottomComponent = {
                    CareButtonLarge(
                        text = "지원자 2명 조회",
                        onClick = {
                            navigateTo(
                                DeepLinkDestination.CenterApplicantInquiry(jobPostingId)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = 32.dp),
                    )
                },
                modifier = Modifier
                    .padding(paddingValue)
                    .padding(top = 24.dp),
            )
        }
    }
}
