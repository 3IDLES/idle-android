package com.idle.worker.job.posting.detail.worker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.navArgs
import com.idle.binding.MainEvent
import com.idle.binding.ShareJobPostingInfo
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.domain.model.jobposting.CrawlingJobPostingDetail
import com.idle.domain.model.jobposting.JobPostingType
import com.idle.domain.model.jobposting.WorkerJobPostingDetail
import com.idle.worker.job.posting.detail.LoadingJobPostingDetailScreen
import com.idle.worker.job.posting.detail.worker.map.PlaceDetailScreen
import com.idle.worker.job.posting.detail.worker.screen.CrawlingJobPostingDetailScreen
import com.idle.worker.job.posting.detail.worker.screen.WorkerJobPostingDetailScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class WorkerJobPostingDetailFragment : BaseComposeFragment() {
    private val args: WorkerJobPostingDetailFragmentArgs by navArgs()
    override val fragmentViewModel: WorkerJobPostingDetailViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val (showPlaceDetail, setShowPlaceDetail) = rememberSaveable { mutableStateOf(false) }
            val profile by profile.collectAsStateWithLifecycle()
            val jobPostingDetail by workerJobPostingDetail.collectAsStateWithLifecycle()

            LaunchedEffect(true) {
                getJobPostingDetail(
                    jobPostingId = args.jobPostingId,
                    jobPostingType = args.jobPostingType,
                )
                launch { getMyProfile() }
            }

            jobPostingDetail?.let { jobPosting ->
                CareStateAnimator(
                    targetState = showPlaceDetail,
                    transitionCondition = showPlaceDetail,
                    modifier = Modifier.fillMaxSize(),
                ) { state ->
                    if (state) {
                        profile?.let { profile ->
                            val homeLatLng =
                                profile.latitude.toDouble() to profile.longitude.toDouble()

                            val workspaceLatLng = when (jobPosting) {
                                is WorkerJobPostingDetail -> jobPosting.latitude.toDouble() to jobPosting.longitude.toDouble()
                                is CrawlingJobPostingDetail -> jobPosting.latitude.toDouble() to jobPosting.longitude.toDouble()
                                else -> homeLatLng
                            }
                            val lotNumberAddress = when (jobPosting) {
                                is WorkerJobPostingDetail -> jobPosting.lotNumberAddress
                                is CrawlingJobPostingDetail -> jobPosting.clientAddress
                                else -> ""
                            }

                            PlaceDetailScreen(
                                callback = { setShowPlaceDetail(false) },
                                homeLatLng = homeLatLng,
                                workspaceLatLng = workspaceLatLng,
                                lotNumberAddress = lotNumberAddress,
                            )
                        }
                    } else {
                        if (jobPosting.jobPostingType == JobPostingType.CAREMEET) {
                            WorkerJobPostingDetailScreen(
                                profile = profile,
                                jobPostingDetail = jobPosting as WorkerJobPostingDetail,
                                showPlaceDetail = setShowPlaceDetail,
                                addFavoriteJobPosting = ::addFavoriteJobPosting,
                                removeFavoriteJobPosting = ::removeFavoriteJobPosting,
                                applyJobPosting = ::applyJobPosting,
                                navigateTo = {
                                    navigationHelper.navigateTo(
                                        com.idle.navigation.NavigationEvent.To(it)
                                    )
                                },
                                shareJobPosting = {
                                    eventHelper.sendEvent(
                                        MainEvent.ShareJobPosting(
                                            ShareJobPostingInfo(
                                                id = jobPosting.id,
                                                title = try {
                                                    jobPosting.lotNumberAddress.split(" ")
                                                        .subList(0, 3)
                                                        .joinToString(" ")
                                                } catch (e: IndexOutOfBoundsException) {
                                                    ""
                                                },
                                                weekdays = jobPosting.weekdays.toList()
                                                    .sortedBy { it.ordinal }
                                                    .joinToString(",") { it.displayName },
                                                workTime = "${jobPosting.startTime} - ${jobPosting.endTime}",
                                                payAmount = "${jobPosting.payType.displayName} ${jobPosting.payAmount}원",
                                                roadNameAddress = jobPosting.roadNameAddress,
                                                gender = jobPosting.gender.displayName,
                                                centerName = jobPosting.centerName,
                                                centerOfficeNumber = jobPosting.centerOfficeNumber,
                                                type = jobPosting.jobPostingType.name,
                                            )
                                        )
                                    )
                                },
                                onChatInquiryClick = ::generateChatRoom,
                            )
                        } else {
                            CrawlingJobPostingDetailScreen(
                                profile = profile,
                                jobPostingDetail = jobPosting as CrawlingJobPostingDetail,
                                showPlaceDetail = setShowPlaceDetail,
                                addFavoriteJobPosting = ::addFavoriteJobPosting,
                                removeFavoriteJobPosting = ::removeFavoriteJobPosting,
                                shareJobPosting = {
                                    eventHelper.sendEvent(
                                        MainEvent.ShareJobPosting(
                                            ShareJobPostingInfo(
                                                id = jobPosting.id,
                                                title = jobPosting.title,
                                                weekdays = jobPosting.workingSchedule,
                                                workTime = jobPosting.workingTime,
                                                payAmount = jobPosting.payInfo,
                                                roadNameAddress = jobPosting.clientAddress,
                                                gender = "-",
                                                centerName = jobPosting.centerName,
                                                centerOfficeNumber = jobPosting.centerAddress,
                                                type = jobPosting.jobPostingType.name,
                                            )
                                        )
                                    )
                                },
                            )
                        }
                    }
                }
            } ?: LoadingJobPostingDetailScreen()
        }
    }
}
