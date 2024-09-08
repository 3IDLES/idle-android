package com.idle.worker.job.posting.detail.worker.screen

import android.content.Intent
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.idle.analytics.helper.LocalAnalyticsHelper
import com.idle.analytics.helper.TrackScreenViewEvent
import com.idle.binding.DeepLinkDestination
import com.idle.binding.DeepLinkDestination.CenterProfile
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareBottomSheetLayout
import com.idle.designsystem.compose.component.CareButtonLine
import com.idle.designsystem.compose.component.CareCard
import com.idle.designsystem.compose.component.CareDialog
import com.idle.designsystem.compose.component.CareMap
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.component.CareTag
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.jobposting.ApplyMethod
import com.idle.domain.model.jobposting.JobPostingType
import com.idle.domain.model.jobposting.WorkerJobPostingDetail
import com.idle.domain.model.profile.WorkerProfile
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun WorkerJobPostingDetailScreen(
    snackbarHostState: SnackbarHostState,
    profile: WorkerProfile?,
    jobPostingDetail: WorkerJobPostingDetail,
    showPlaceDetail: (Boolean) -> Unit,
    addFavoriteJobPosting: (String, JobPostingType) -> Unit,
    removeFavoriteJobPosting: (String, JobPostingType) -> Unit,
    applyJobPosting: (String, ApplyMethod) -> Unit,
    navigateTo: (DeepLinkDestination) -> Unit,
) {
    val onBackPressedDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var applyMethod by rememberSaveable { mutableStateOf(ApplyMethod.APP) }

    val dialResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        applyMethod = ApplyMethod.CALLING
        showDialog = true
    }

    if (showDialog) {
        CareDialog(
            title = "이 공고에 지원하시겠습니까?",
            leftButtonText = stringResource(id = R.string.cancel),
            rightButtonText = stringResource(id = R.string.recruit),
            leftButtonTextColor = CareTheme.colors.gray300,
            leftButtonColor = CareTheme.colors.white000,
            leftButtonBorder = BorderStroke(1.dp, CareTheme.colors.gray100),
            rightButtonTextColor = CareTheme.colors.white000,
            rightButtonColor = CareTheme.colors.orange500,
            onDismissRequest = { showDialog = false },
            onLeftButtonClick = { showDialog = false },
            onRightButtonClick = {
                applyJobPosting(jobPostingDetail.id, applyMethod)
                showDialog = false
                coroutineScope.launch {
                    sheetState.hide()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )
    }

    CareBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(id = R.string.inquiry),
                    style = CareTheme.typography.heading3,
                    color = CareTheme.colors.gray900,
                    modifier = Modifier.padding(bottom = 20.dp),
                )


                CareCard(
                    title = stringResource(id = R.string.inquiry_by_call),
                    description = "${jobPostingDetail.centerName} | 010-1234-5678",
                    titleLeftComponent = {
                        Image(
                            painter = painterResource(R.drawable.ic_call),
                            contentDescription = null,
                        )
                    },
                    onClick = {
                        val number = "tel:010-1234-5678"
                        val dialIntent = Intent(Intent.ACTION_DIAL, number.toUri())
                        dialResultLauncher.launch(dialIntent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
    ) {
        Scaffold(
            containerColor = CareTheme.colors.white000,
            topBar = {
                CareSubtitleTopBar(
                    title = stringResource(id = R.string.job_posting_detail),
                    modifier = Modifier.padding(
                        start = 12.dp,
                        top = 48.dp,
                        end = 20.dp,
                        bottom = 12.dp
                    ),
                    onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = { data ->
                        CareSnackBar(
                            data = data,
                            modifier = Modifier.padding(bottom = 116.dp)
                        )
                    }
                )
            },
        ) { paddingValue ->
            val starTintColor by animateColorAsState(
                targetValue = if (jobPostingDetail.isFavorite) CareTheme.colors.orange300
                else CareTheme.colors.gray200,
                label = "즐겨찾기 별의 색상을 관리하는 애니메이션"
            )

            Column(modifier = Modifier.fillMaxSize()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier
                        .padding(paddingValue)
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(scrollState)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            if (!jobPostingDetail.isExperiencePreferred) {
                                CareTag(
                                    text = stringResource(id = R.string.beginner_possible),
                                    textColor = CareTheme.colors.orange500,
                                    backgroundColor = CareTheme.colors.orange100,
                                )
                            }

                            if (jobPostingDetail.calculateDeadline() <= 14) {
                                CareTag(
                                    text = "D-${jobPostingDetail.calculateDeadline()}",
                                    textColor = CareTheme.colors.gray300,
                                    backgroundColor = CareTheme.colors.gray050,
                                )
                            }

                            CareTag(
                                text = "도보 ${jobPostingDetail.getDistanceInMinutes()}",
                                textColor = CareTheme.colors.gray300,
                                backgroundColor = CareTheme.colors.gray050,
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Image(
                                painter = painterResource(R.drawable.ic_star_gray),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(starTintColor),
                                modifier = Modifier.clickable {
                                    if (!jobPostingDetail.isFavorite) {
                                        addFavoriteJobPosting(
                                            jobPostingDetail.id,
                                            jobPostingDetail.jobPostingType,
                                        )
                                    } else {
                                        removeFavoriteJobPosting(
                                            jobPostingDetail.id,
                                            jobPostingDetail.jobPostingType,
                                        )
                                    }
                                }
                            )
                        }

                        Text(
                            text = try {
                                jobPostingDetail.lotNumberAddress.split(" ").subList(0, 3)
                                    .joinToString(" ")
                            } catch (e: IndexOutOfBoundsException) {
                                ""
                            },
                            style = CareTheme.typography.subtitle1,
                            color = CareTheme.colors.gray900,
                            overflow = TextOverflow.Clip,
                            maxLines = 1,
                            modifier = Modifier.padding(bottom = 2.dp),
                        )

                        Text(
                            text = "${jobPostingDetail.careLevel}등급 ${jobPostingDetail.age}세 ${jobPostingDetail.gender.displayName}",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray900,
                            modifier = Modifier.padding(end = 8.dp, bottom = 4.dp),
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 2.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_clock),
                                contentDescription = null,
                            )

                            Text(
                                text = "${
                                    jobPostingDetail.weekdays.toList().sortedBy { it.ordinal }
                                        .joinToString(",") { it.displayName }
                                } | ${jobPostingDetail.startTime} - ${jobPostingDetail.endTime}",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray500,
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_money),
                                contentDescription = null,
                            )

                            Text(
                                text = "${jobPostingDetail.payType.displayName} ${jobPostingDetail.payAmount}원",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray500,
                            )
                        }
                    }

                    HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.work_address),
                            style = CareTheme.typography.subtitle1,
                            color = CareTheme.colors.gray900,
                            modifier = Modifier.padding(bottom = 20.dp),
                        )

                        Text(
                            text = "거주지에서 ${jobPostingDetail.lotNumberAddress} 까지",
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray500,
                            modifier = Modifier.padding(bottom = 4.dp),
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp),
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_walk),
                                contentDescription = null,
                            )

                            Text(
                                text = "걸어서 ${jobPostingDetail.getDistanceInMinutes()} 소요",
                                style = CareTheme.typography.subtitle2,
                                color = CareTheme.colors.gray900,
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(224.dp)
                                .clip(RoundedCornerShape(8.dp)),
                        ) {
                            if (profile != null) {
                                CareMap(
                                    homeLatLng = profile.latitude.toDouble() to profile.longitude.toDouble(),
                                    workspaceLatLng = jobPostingDetail.latitude.toDouble() to jobPostingDetail.longitude.toDouble(),
                                    onMapClick = { showPlaceDetail(true) },
                                    modifier = Modifier.fillMaxSize(),
                                )

                                Image(
                                    painter = painterResource(id = R.drawable.ic_map_expand),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(12.dp),
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_map_loading),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                        }
                    }

                    HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.work_conditions),
                            style = CareTheme.typography.subtitle1,
                            color = CareTheme.colors.gray900,
                            modifier = Modifier.padding(bottom = 20.dp),
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(32.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = stringResource(id = R.string.work_days),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray300,
                                )

                                Text(
                                    text = stringResource(id = R.string.work_hours),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray300,
                                )

                                Text(
                                    text = stringResource(id = R.string.pay),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray300,
                                )

                                Text(
                                    text = stringResource(id = R.string.work_address),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray300,
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = jobPostingDetail.weekdays.toList()
                                        .sortedBy { it.ordinal }
                                        .joinToString(",") { it.displayName },
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray900,
                                )

                                Text(
                                    text = "${jobPostingDetail.startTime} - ${jobPostingDetail.endTime}",
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray900,
                                )

                                Text(
                                    text = "${jobPostingDetail.payType.displayName} ${jobPostingDetail.payAmount} 원",
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray900,
                                )

                                Text(
                                    text = jobPostingDetail.lotNumberAddress,
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray900,
                                )
                            }
                        }
                    }

                    HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.customer_info),
                            style = CareTheme.typography.subtitle1,
                            color = CareTheme.colors.gray900,
                            modifier = Modifier.padding(bottom = 20.dp),
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(32.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.width(60.dp),
                            ) {
                                Text(
                                    text = stringResource(id = R.string.gender),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray300,
                                )

                                Text(
                                    text = stringResource(id = R.string.age),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray300,
                                )

                                Text(
                                    text = stringResource(id = R.string.weight),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray300,
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = jobPostingDetail.gender.displayName,
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray900,
                                )

                                Text(
                                    text = "${jobPostingDetail.age}세",
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray900,
                                )

                                Text(
                                    text = if (jobPostingDetail.weight != null) "${jobPostingDetail.weight}kg"
                                    else "-",
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray900,
                                )
                            }
                        }

                        HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(32.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = stringResource(id = R.string.care_level),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray300,
                                )

                                Text(
                                    text = stringResource(id = R.string.mental_status),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray300,
                                )

                                Text(
                                    text = stringResource(id = R.string.disease),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray300,
                                )
                            }

                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text(
                                    text = "${jobPostingDetail.careLevel}등급",
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray900,
                                )

                                Text(
                                    text = jobPostingDetail.mentalStatus.displayName,
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray900,
                                )

                                Text(
                                    text = jobPostingDetail.disease ?: "-",
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray900,
                                )
                            }
                        }

                        HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(32.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.width(60.dp),
                            ) {
                                Text(
                                    text = stringResource(id = R.string.meal_assistance),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray300,
                                )

                                Text(
                                    text = stringResource(id = R.string.bowel_assistance),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray300,
                                )

                                Text(
                                    text = stringResource(id = R.string.walking_assistance),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray300,
                                )

                                Text(
                                    text = stringResource(id = R.string.life_assistance),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray300,
                                )
                            }

                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text(
                                    text = if (jobPostingDetail.isMealAssistance) stringResource(
                                        id = R.string.necessary
                                    )
                                    else stringResource(id = R.string.unnecessary),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray900,
                                )

                                Text(
                                    text = if (jobPostingDetail.isBowelAssistance) stringResource(
                                        id = R.string.necessary
                                    )
                                    else stringResource(id = R.string.unnecessary),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray900,
                                )

                                Text(
                                    text = if (jobPostingDetail.isWalkingAssistance) stringResource(
                                        id = R.string.necessary
                                    )
                                    else stringResource(id = R.string.unnecessary),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray900,
                                )

                                Text(
                                    text = jobPostingDetail.lifeAssistance.toList()
                                        .sortedBy { it.ordinal }
                                        .joinToString(",") { it.displayName },
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray900,
                                )
                            }
                        }

                        HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)

                        Text(
                            text = stringResource(id = R.string.speciality),
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.gray300,
                            modifier = Modifier.padding(top = 16.dp, bottom = 6.dp),
                        )

                        CareTextFieldLong(
                            value = jobPostingDetail.extraRequirement ?: "-",
                            enabled = false,
                            onValueChanged = {},
                        )
                    }

                    HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.additional_info),
                            style = CareTheme.typography.subtitle1,
                            color = CareTheme.colors.gray900,
                            modifier = Modifier.padding(bottom = 20.dp),
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(32.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = stringResource(id = R.string.experience_preference),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray300,
                                )

                                Text(
                                    text = stringResource(id = R.string.apply_method),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray300,
                                )

                                Text(
                                    text = stringResource(id = R.string.apply_deadline),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray300,
                                )
                            }

                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text(
                                    text = if (jobPostingDetail.isExperiencePreferred) stringResource(
                                        id = R.string.experience_preferred
                                    )
                                    else stringResource(id = R.string.beginner_possible),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray900,
                                )

                                Text(
                                    text = jobPostingDetail.applyMethod.toList()
                                        .sortedBy { it.ordinal }
                                        .joinToString(",") { it.displayName },
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray900,
                                )

                                Text(
                                    text = jobPostingDetail.applyDeadline.toString(),
                                    style = CareTheme.typography.body2,
                                    color = CareTheme.colors.gray900,
                                )
                            }
                        }
                    }

                    HorizontalDivider(thickness = 8.dp, color = CareTheme.colors.gray050)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = 48.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.center_info),
                            style = CareTheme.typography.subtitle1,
                            color = CareTheme.colors.gray900,
                            modifier = Modifier.padding(bottom = 12.dp),
                        )

                        CareCard(
                            title = jobPostingDetail.centerName,
                            description = jobPostingDetail.centerRoadNameAddress,
                            onClick = { navigateTo(CenterProfile(jobPostingDetail.centerId)) }
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CareTheme.colors.white000)
                        .padding(top = 12.dp, bottom = 28.dp, start = 20.dp, end = 20.dp),
                ) {
                    CareButtonLine(
                        text = stringResource(id = R.string.inquiry),
                        onClick = {
                            coroutineScope.launch {
                                sheetState.show()
                            }
                        },
                        borderColor = CareTheme.colors.orange400,
                        textColor = CareTheme.colors.orange500,
                        modifier = Modifier.weight(1f),
                    )

                    CareButtonLine(
                        text = stringResource(id = R.string.recruit),
                        onClick = {
                            applyMethod = ApplyMethod.CALLING
                            showDialog = true
                        },
                        containerColor = CareTheme.colors.orange500,
                        borderColor = CareTheme.colors.orange500,
                        textColor = CareTheme.colors.white000,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }

    TrackScreenViewEvent(screenName = "carer_caremeet_job_posting_detail_screen")
}