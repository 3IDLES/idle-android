@file:OptIn(ExperimentalMaterialApi::class)

package com.idle.worker.job.posting.detail

import android.content.Intent
import android.util.Log
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.idle.binding.repeatOnStarted
import com.idle.compose.base.BaseComposeFragment
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareBottomSheetLayout
import com.idle.designsystem.compose.component.CareButtonLine
import com.idle.designsystem.compose.component.CareCard
import com.idle.designsystem.compose.component.CareMap
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.component.CareTag
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.worker.job.posting.detail.map.PlaceDetailScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class WorkerJobPostingDetailFragment : BaseComposeFragment() {
    override val fragmentViewModel: WorkerJobPostingDetailViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val (showPlaceDetail, setShowPlaceDetail) = rememberSaveable { mutableStateOf(false) }

            repeatOnStarted {
                eventFlow.collect { handleJobPostingEvent(it) }
            }

            CareStateAnimator(
                targetState = showPlaceDetail,
                transitionCondition = showPlaceDetail,
                modifier = Modifier.fillMaxSize(),
            ) { state ->
                if (state) {
                    PlaceDetailScreen(
                        callback = { setShowPlaceDetail(false) },
                        homeLatLng = 37.5670135 to 126.9883740,
                        workspaceLatLng = 37.5690135 to 126.9783740,
                    )
                } else {
                    WorkerJobPostingDetailScreen(
                        showPlaceDetail = setShowPlaceDetail,
                        requestEvent = ::workerJobPostingDetailEvent,
                    )
                }
            }
        }
    }

    private fun handleJobPostingEvent(event: WorkerJobPostingDetailEvent) {
        when (event) {
            is WorkerJobPostingDetailEvent.CallInquiry -> {
                val number = "tel:" + event.number
                startActivity(Intent(Intent.ACTION_DIAL, number.toUri()))
            }
        }
    }
}

@Composable
internal fun WorkerJobPostingDetailScreen(
    showPlaceDetail: (Boolean) -> Unit,
    requestEvent: (WorkerJobPostingDetailEvent) -> Unit,
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )

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
                    description = "네 얼간이 요양보호센터 | 010-1234-5678",
                    titleLeftComponent = {
                        Image(
                            painter = painterResource(R.drawable.ic_call),
                            contentDescription = null,
                        )
                    },
                    onClick = { requestEvent(WorkerJobPostingDetailEvent.CallInquiry("01012345678")) },
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
            }
        ) { paddingValue ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .padding(paddingValue)
                    .padding(top = 24.dp)
                    .fillMaxWidth()
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
                        CareTag(
                            text = "초보가능",
                            textColor = CareTheme.colors.orange500,
                            backgroundColor = CareTheme.colors.orange100,
                        )

                        CareTag(
                            text = "D-10",
                            textColor = CareTheme.colors.gray300,
                            backgroundColor = CareTheme.colors.gray050,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Image(
                            painter = painterResource(R.drawable.ic_star_gray),
                            contentDescription = null,
                        )
                    }

                    Text(
                        text = "서울특별시 강남구 신사동",
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.gray900,
                        overflow = TextOverflow.Clip,
                        maxLines = 1,
                        modifier = Modifier.padding(bottom = 2.dp),
                    )

                    Text(
                        text = "1등급 78세 여성",
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
                            text = "월, 화, 수, 목, 금 | 09:00 - 15:00",
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
                            text = "시급 12,500 원",
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
                        text = "거주지에서 서울특별시 중 순화동 151까지",
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
                            text = "걸어서 5분 ~ 10 소요",
                            style = CareTheme.typography.subtitle2,
                            color = CareTheme.colors.gray500,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(224.dp)
                            .clip(RoundedCornerShape(8.dp)),
                    ) {
                        CareMap(
                            homeLatLng = 37.5670135 to 126.9883740,
                            workspaceLatLng = 37.5690135 to 126.9783740,
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
                                text = "월, 화, 수, 목, 금",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = "09:00 - 15:00",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = "12,500원",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = "서울특별시 중구 순화동 151",
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
                                text = "남성",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = "74세",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = "71kg",
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
                                text = "2등급",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = "치매 초기",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = "어쩌고저쩌고",
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
                                text = stringResource(id = R.string.unnecessary),
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = "필요",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = "필요",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = "청소, 말벗",
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
                        value = "5~60대 남성 선생님을 선호합니다.",
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
                                text = "초보 가능",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = "전화",
                                style = CareTheme.typography.body2,
                                color = CareTheme.colors.gray900,
                            )

                            Text(
                                text = "2024. 11. 12",
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
                        text = stringResource(id = R.string.center_info),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.gray900,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )

                    CareCard(
                        title = "네얼간이 요양보호소",
                        description = "용인시 어쩌고 저쩌고",
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 30.dp, start = 20.dp, end = 20.dp),
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
                        onClick = {},
                        containerColor = CareTheme.colors.orange500,
                        borderColor = CareTheme.colors.orange500,
                        textColor = CareTheme.colors.white000,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}