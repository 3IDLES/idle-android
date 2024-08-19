package com.idle.worker.profile

import android.net.Uri
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonRound
import com.idle.designsystem.compose.component.CareClickableTextField
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.component.CareTag
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.profile.JobSearchStatus.NO
import com.idle.domain.model.profile.JobSearchStatus.UNKNOWN
import com.idle.domain.model.profile.JobSearchStatus.YES
import com.idle.domain.model.profile.WorkerProfile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerProfileFragment : BaseComposeFragment() {
    override val fragmentViewModel: WorkerProfileViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val workerProfile by workerProfile.collectAsStateWithLifecycle()
            val workerIntroduce by workerIntroduce.collectAsStateWithLifecycle()
            val specialty by specialty.collectAsStateWithLifecycle()
            val gender by gender.collectAsStateWithLifecycle()
            val isEditState by isEditState.collectAsStateWithLifecycle()
            val profileImageUri by profileImageUri.collectAsStateWithLifecycle()
            val experienceYear by experienceYear.collectAsStateWithLifecycle()

            val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = { uri -> setProfileImageUrl(uri) }
            )

            WorkerProfileScreen(
                isEditState = isEditState,
                workerProfile = workerProfile,
                workerIntroduce = workerIntroduce,
                specialty = specialty,
                gender = gender,
                experienceYear = experienceYear,
                profileImageUri = profileImageUri,
                singlePhotoPickerLauncher = singlePhotoPickerLauncher,
                onSpecialtyChanged = ::setSpecialty,
                onWorkerIntroduceChanged = ::setWorkerIntroduce,
                updateWorkerProfile = ::updateWorkerProfile,
                setEditState = ::setEditState,
            )
        }
    }
}

@Composable
internal fun WorkerProfileScreen(
    workerProfile: WorkerProfile,
    workerIntroduce: String,
    specialty: String,
    profileImageUri: Uri?,
    experienceYear: Int?,
    gender: Gender,
    isEditState: Boolean,
    singlePhotoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    setEditState: (Boolean) -> Unit,
    onSpecialtyChanged: (String) -> Unit,
    onWorkerIntroduceChanged: (String) -> Unit,
    updateWorkerProfile: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier.background(
                    if (!isEditState) CareTheme.colors.gray050
                    else CareTheme.colors.white000
                ),
            ) {
                CareSubtitleTopBar(
                    title = stringResource(id = R.string.my_profile),
                    onNavigationClick = {
                        if (isEditState) setEditState(false)
                        else onBackPressedDispatcher?.onBackPressed()
                    },
                    leftComponent = {
                        if (isEditState) {
                            Text(
                                text = stringResource(id = R.string.save),
                                style = CareTheme.typography.subtitle2,
                                color = CareTheme.colors.orange500,
                                modifier = Modifier.clickable {
                                    updateWorkerProfile()
                                }
                            )
                        } else {
                            CareButtonRound(
                                text = stringResource(id = R.string.edit),
                                onClick = { setEditState(true) },
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, top = 48.dp, end = 20.dp, bottom = 12.dp),
                )
            }
        },
        containerColor = CareTheme.colors.white000,
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
        ) {
            if (!isEditState) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .background(CareTheme.colors.gray050)
                        .height(92.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 44.dp)
                    .align(Alignment.TopCenter)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .clickable(enabled = isEditState) {
                                singlePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_worker_photo_default),
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center),
                        )

                        if (isEditState) {
                            Image(
                                painter = painterResource(com.idle.designresource.R.drawable.ic_edit_pencil_big),
                                contentDescription = null,
                                modifier = Modifier.align(Alignment.BottomEnd)
                            )
                        }
                    }
                }

                if (!isEditState) {
                    when (workerProfile.jobSearchStatus) {
                        YES -> CareTag(
                            text = workerProfile.jobSearchStatus.displayName,
                            textColor = CareTheme.colors.orange500,
                            backgroundColor = CareTheme.colors.orange100,
                        )

                        NO -> CareTag(
                            text = workerProfile.jobSearchStatus.displayName,
                            textColor = CareTheme.colors.gray300,
                            backgroundColor = CareTheme.colors.gray050,
                        )

                        UNKNOWN -> CareTag(
                            text = workerProfile.jobSearchStatus.displayName,
                            textColor = CareTheme.colors.gray300,
                            backgroundColor = CareTheme.colors.gray100,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(bottom = 4.dp),
                    ) {
                        Text(
                            text = workerProfile.workerName,
                            style = CareTheme.typography.heading2,
                            color = CareTheme.colors.gray900,
                        )

                        Text(
                            text = stringResource(id = R.string.worker),
                            style = CareTheme.typography.subtitle3,
                            color = CareTheme.colors.gray900,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.height(IntrinsicSize.Min),
                    ) {
                        Text(
                            text = stringResource(id = R.string.age),
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray500,
                            modifier = Modifier.padding(end = 4.dp),
                        )

                        Text(
                            text = "${workerProfile.age}세",
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray900,
                        )

                        VerticalDivider(
                            thickness = 1.dp,
                            color = CareTheme.colors.gray100,
                            modifier = Modifier.padding(horizontal = 8.dp),
                        )

                        Text(
                            text = stringResource(id = R.string.gender),
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray500,
                            modifier = Modifier.padding(end = 4.dp),
                        )

                        Text(
                            text = workerProfile.gender.displayName,
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray900,
                        )

                        if (experienceYear != null) {
                            VerticalDivider(
                                thickness = 1.dp,
                                color = CareTheme.colors.gray100,
                                modifier = Modifier.padding(horizontal = 8.dp),
                            )

                            Text(
                                text = stringResource(id = R.string.experience),
                                style = CareTheme.typography.body3,
                                color = CareTheme.colors.gray500,
                                modifier = Modifier.padding(end = 4.dp),
                            )

                            Text(
                                text = "${workerProfile.experienceYear}년차",
                                style = CareTheme.typography.body3,
                                color = CareTheme.colors.gray900,
                            )
                        }
                    }
                } else {
                    Text(
                        text = workerProfile.workerName.ifBlank { "홍길동" },
                        style = CareTheme.typography.heading2,
                        color = CareTheme.colors.gray900,
                        modifier = Modifier.padding(bottom = 6.dp),
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.height(IntrinsicSize.Min),
                    ) {
                        Text(
                            text = stringResource(id = R.string.age),
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray500,
                            modifier = Modifier.padding(end = 4.dp),
                        )

                        Text(
                            text = "${workerProfile.age}세",
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray900,
                        )

                        VerticalDivider(
                            thickness = 1.dp,
                            color = CareTheme.colors.gray100,
                            modifier = Modifier.padding(horizontal = 8.dp),
                        )

                        Text(
                            text = stringResource(id = R.string.gender),
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray500,
                            modifier = Modifier.padding(end = 4.dp),
                        )

                        Text(
                            text = workerProfile.gender.displayName,
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray900,
                        )

                        VerticalDivider(
                            thickness = 1.dp,
                            color = CareTheme.colors.gray100,
                            modifier = Modifier.padding(horizontal = 8.dp),
                        )

                        Text(
                            text = stringResource(id = R.string.phone_number),
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray500,
                            modifier = Modifier.padding(end = 4.dp),
                        )

                        Text(
                            text = workerProfile.phoneNumber,
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray900,
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 8.dp,
                    color = CareTheme.colors.gray050,
                    modifier = Modifier.padding(vertical = 24.dp),
                )

                if (isEditState) {
                    LabeledContent(
                        subtitle = stringResource(id = R.string.experience),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = 32.dp),
                    ) {
                        CareClickableTextField(
                            value = "",
                            hint = stringResource(id = R.string.year),
                            leftComponent = {
                                Image(
                                    painter = painterResource(R.drawable.ic_arrow_down),
                                    contentDescription = null,
                                )
                            },
                            onClick = {},
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                LabeledContent(
                    subtitle = stringResource(id = R.string.address),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 32.dp),
                ) {
                    if (!isEditState) {
                        Text(
                            text = "서울특별시 강남구 삼성동 512-3",
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray900,
                        )
                    } else {
                        CareTextField(
                            value = "서울특별시 강남구 삼성동 512-3",
                            onValueChanged = { },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                LabeledContent(
                    subtitle = stringResource(id = R.string.worker_introduce),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 32.dp),
                ) {
                    if (!isEditState) {
                        Text(
                            text = workerIntroduce.ifBlank { "-" },
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray900,
                        )
                    } else {
                        CareTextField(
                            value = workerIntroduce,
                            onValueChanged = onWorkerIntroduceChanged,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                LabeledContent(
                    subtitle = stringResource(id = R.string.specialty),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 52.dp),
                ) {
                    if (!isEditState) {
                        Text(
                            text = specialty.ifBlank { "-" },
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray900,
                        )
                    } else {
                        CareTextFieldLong(
                            value = specialty,
                            onValueChanged = onSpecialtyChanged,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}