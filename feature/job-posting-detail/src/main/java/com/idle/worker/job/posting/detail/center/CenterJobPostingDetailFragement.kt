package com.idle.worker.job.posting.detail.center

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.navArgs
import com.idle.analytics.helper.TrackScreenViewEvent
import com.idle.binding.DeepLinkDestination
import com.idle.binding.MainEvent
import com.idle.binding.NavigationEvent
import com.idle.center.job.edit.JobEditScreen
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareBottomSheetLayout
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareCard
import com.idle.designsystem.compose.component.CareDialog
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.jobposting.CenterJobPostingDetail
import com.idle.domain.model.jobposting.JobPostingStatus
import com.idle.worker.job.posting.detail.LoadingJobPostingDetailScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
            val jobPostingDetail by jobPostingDetail.collectAsStateWithLifecycle()
            val applicantsCount by applicantsCount.collectAsStateWithLifecycle()
            val jobPostingDetailState by jobPostingState.collectAsStateWithLifecycle()
            val profile by profile.collectAsStateWithLifecycle()

            LaunchedEffect(true) {
                if (args.isEditState) {
                    setJobPostingState(JobPostingDetailState.EDIT)
                }
                getCenterJobPostingDetail(jobPostingId)
                launch { getApplicantsCount(jobPostingId) }
            }
            jobPostingDetail?.let {
                CareStateAnimator(
                    targetState = jobPostingDetailState,
                ) { state ->
                    when (state) {
                        JobPostingDetailState.EDIT -> JobEditScreen(
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
                            birthYear = (LocalDate.now(ZoneId.of("Asia/Seoul")).year - it.age +1).toString(),
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
                            setEditState = {
                                if (it) setJobPostingState(JobPostingDetailState.EDIT)
                                else setJobPostingState(JobPostingDetailState.SUMMARY)
                            },
                            showSnackBar = {
                                eventHandlerHelper.sendEvent(
                                    MainEvent.ShowToast(it)
                                )
                            }
                        )

                        JobPostingDetailState.PREVIEW -> JobPostingPreviewScreen(
                            weekdays = it.weekdays,
                            workStartTime = it.startTime,
                            workEndTime = it.endTime,
                            payType = it.payType,
                            payAmount = it.payAmount.toString(),
                            lotNumberAddress = it.lotNumberAddress,
                            gender = it.gender,
                            birthYear = (LocalDate.now(ZoneId.of("Asia/Seoul")).year - it.age + 1).toString(),
                            weight = it.weight.toString(),
                            careLevel = it.careLevel.toString(),
                            mentalStatus = it.mentalStatus,
                            disease = it.disease,
                            isMealAssistance = it.isMealAssistance,
                            isBowelAssistance = it.isBowelAssistance,
                            isWalkingAssistance = it.isWalkingAssistance,
                            lifeAssistance = it.lifeAssistance,
                            extraRequirement = it.extraRequirement.toString(),
                            isExperiencePreferred = it.isExperiencePreferred,
                            applyMethod = it.applyMethod,
                            applyDeadline = it.applyDeadline,
                            centerProfile = profile,
                            onBackPressed = { setJobPostingState(JobPostingDetailState.SUMMARY) },
                        )

                        else -> {
                            CenterJobPostingDetailScreen(
                                jobPostingId = jobPostingId,
                                jobPostingDetail = jobPostingDetail,
                                applicantsCount = applicantsCount,
                                endJobPosting = ::endJobPosting,
                                deleteJobPosting = ::deleteJobPosting,
                                navigateTo = {
                                    navigationHelper.navigateTo(NavigationEvent.NavigateTo(it))
                                },
                                setJobPostingDetailState = ::setJobPostingState,
                            )
                        }
                    }
                }
            } ?: LoadingJobPostingDetailScreen()
        }
    }
}

@Composable
internal fun CenterJobPostingDetailScreen(
    jobPostingId: String,
    jobPostingDetail: CenterJobPostingDetail?,
    applicantsCount: Int,
    endJobPosting: (String) -> Unit,
    deleteJobPosting: (String) -> Unit,
    navigateTo: (DeepLinkDestination) -> Unit,
    setJobPostingDetailState: (JobPostingDetailState) -> Unit,
) {
    val onBackPressedDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    val coroutineScope = rememberCoroutineScope()
    var showEndJobPostingDialog by remember { mutableStateOf(false) }
    var showDeleteJobPostingDialog by remember { mutableStateOf(false) }

    if (showEndJobPostingDialog) {
        CareDialog(
            title = stringResource(id = R.string.end_job_posting_title),
            description = stringResource(id = R.string.end_job_posting_description),
            leftButtonText = stringResource(id = R.string.cancel),
            rightButtonText = stringResource(id = R.string.end),
            leftButtonTextColor = CareTheme.colors.gray300,
            leftButtonColor = CareTheme.colors.white000,
            leftButtonBorder = BorderStroke(1.dp, CareTheme.colors.gray100),
            rightButtonTextColor = CareTheme.colors.white000,
            rightButtonColor = CareTheme.colors.red,
            onDismissRequest = { showEndJobPostingDialog = false },
            onLeftButtonClick = { showEndJobPostingDialog = false },
            onRightButtonClick = {
                coroutineScope.launch {
                    showEndJobPostingDialog = false
                    sheetState.hide()
                    endJobPosting(jobPostingId)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )
    }

    if (showDeleteJobPostingDialog) {
        CareDialog(
            title = stringResource(id = R.string.delete_job_posting_title),
            description = stringResource(id = R.string.delete_job_posting_description),
            leftButtonText = stringResource(id = R.string.cancel),
            rightButtonText = stringResource(id = R.string.end),
            leftButtonTextColor = CareTheme.colors.gray300,
            leftButtonColor = CareTheme.colors.white000,
            leftButtonBorder = BorderStroke(1.dp, CareTheme.colors.gray100),
            rightButtonTextColor = CareTheme.colors.white000,
            rightButtonColor = CareTheme.colors.red,
            onDismissRequest = { showDeleteJobPostingDialog = false },
            onLeftButtonClick = { showDeleteJobPostingDialog = false },
            onRightButtonClick = {
                coroutineScope.launch {
                    showDeleteJobPostingDialog = false
                    sheetState.hide()
                    deleteJobPosting(jobPostingId)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )
    }

    jobPostingDetail?.let {
        CareBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(id = R.string.job_posting_edit),
                        style = CareTheme.typography.heading3,
                        color = CareTheme.colors.black,
                        modifier = Modifier.padding(bottom = 20.dp),
                    )

                    if (jobPostingDetail.jobPostingStatus == JobPostingStatus.IN_PROGRESS) {
                        CareCard(
                            title = stringResource(id = R.string.edit_job_posting_button),
                            titleLeftComponent = {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_edit_pencil_non_background),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(CareTheme.colors.gray500),
                                )
                            },
                            onClick = {
                                coroutineScope.launch {
                                    sheetState.hide()
                                    setJobPostingDetailState(JobPostingDetailState.EDIT)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )

                        CareCard(
                            title = stringResource(id = R.string.end_recruiting),
                            titleTextColor = CareTheme.colors.red,
                            titleLeftComponent = {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_red_check),
                                    contentDescription = null,
                                )
                            },
                            onClick = { showEndJobPostingDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    if (jobPostingDetail.jobPostingStatus == JobPostingStatus.COMPLETED) {
                        CareCard(
                            title = stringResource(id = R.string.delete_job_posting),
                            titleTextColor = CareTheme.colors.red,
                            titleLeftComponent = {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_red_check),
                                    contentDescription = null,
                                )
                            },
                            onClick = { showDeleteJobPostingDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            },
        ) {
            Scaffold(
                topBar = {
                    CareSubtitleTopBar(
                        title = stringResource(id = R.string.manage_job_posting),
                        onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                        leftComponent = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_3dots_horizontal),
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    coroutineScope.launch { sheetState.show() }
                                }
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
                    age = (LocalDate.now().year - it.age + 1).toString(),
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
                    onClickPreview = { setJobPostingDetailState(JobPostingDetailState.PREVIEW) },
                    bottomComponent = {
                        CareButtonLarge(
                            text = "지원자 ${applicantsCount}명 조회",
                            enable = applicantsCount != 0 && it.jobPostingStatus == JobPostingStatus.IN_PROGRESS,
                            onClick = {
                                navigateTo(
                                    DeepLinkDestination.CenterApplicantInquiry(jobPostingId)
                                )
                            },
                            disabledContainerColor = if (it.jobPostingStatus == JobPostingStatus.IN_PROGRESS) CareTheme.colors.gray200
                            else CareTheme.colors.white000,
                            border = if (it.jobPostingStatus == JobPostingStatus.IN_PROGRESS) null
                            else BorderStroke(
                                width = 1.dp,
                                color = CareTheme.colors.gray200
                            ),
                            textColor = if (it.jobPostingStatus == JobPostingStatus.IN_PROGRESS) CareTheme.colors.white000
                            else CareTheme.colors.gray300,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CareTheme.colors.white000)
                                .align(Alignment.BottomCenter)
                                .padding(
                                    top = 12.dp,
                                    start = 20.dp,
                                    end = 20.dp,
                                    bottom = 28.dp
                                ),
                        )
                    },
                    modifier = Modifier
                        .padding(paddingValue)
                        .padding(top = 24.dp),
                )
            }
        }
    }

    TrackScreenViewEvent(screenName = "center_job_posting_detail_screen")
}
