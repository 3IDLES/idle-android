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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonRound
import com.idle.designsystem.compose.component.CareChipBasic
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.component.CareTag
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.CareTextFieldLong
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
                    title = "내 프로필",
                    onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
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
                        .height(32.dp)
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
                .padding(top = 24.dp)
                .verticalScroll(scrollState),
        ) {
            if (!isEditState) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.size(24.dp))

                    Box(modifier = Modifier.clickable {
                        if (isEditState) singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }) {
                        Image(
                            painter = painterResource(com.idle.designresource.R.drawable.ic_worker_empty),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 7.dp),
                        )

                        if (isEditState) {
                            Image(
                                painter = painterResource(com.idle.designresource.R.drawable.ic_edit_pencil_big),
                                contentDescription = null,
                                modifier = Modifier.align(Alignment.BottomEnd)
                            )
                        }
                    }

                    Image(
                        painter = painterResource(com.idle.designresource.R.drawable.ic_star_gray),
                        contentDescription = null,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                if (!isEditState) {
                    when (workerProfile.jobSearchStatus) {
                        YES -> CareTag(
                            text = workerProfile.jobSearchStatus.displayName,
                            textColor = CareTheme.colors.orange500,
                            backgroundColor = CareTheme.colors.orange100,
                            modifier = Modifier.padding(top = 16.dp),
                        )

                        NO -> CareTag(
                            text = workerProfile.jobSearchStatus.displayName,
                            textColor = CareTheme.colors.gray300,
                            backgroundColor = CareTheme.colors.gray050,
                            modifier = Modifier.padding(top = 16.dp),
                        )

                        UNKNOWN -> CareTag(
                            text = workerProfile.jobSearchStatus.displayName,
                            textColor = CareTheme.colors.gray300,
                            backgroundColor = CareTheme.colors.gray100,
                            modifier = Modifier.padding(top = 16.dp),
                        )
                    }
                }

                if (!isEditState) {
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
                } else {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = 24.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.name),
                            style = CareTheme.typography.subtitle4,
                            color = CareTheme.colors.gray500,
                        )

                        CareTextField(
                            value = workerProfile.workerName,
                            onValueChanged = { },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                if (!isEditState) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 28.dp)
                            .height(IntrinsicSize.Min)
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

                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.gender),
                            style = CareTheme.typography.subtitle4,
                            color = CareTheme.colors.gray500,
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CareChipBasic(
                                text = Gender.WOMAN.displayName,
                                enable = gender == Gender.WOMAN,
                                modifier = Modifier.width(104.dp),
                            )

                            CareChipBasic(
                                text = Gender.MAN.displayName,
                                enable = gender == Gender.MAN,
                                modifier = Modifier.width(104.dp),
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = 24.dp),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(bottom = 6.dp),
                        ) {
                            Text(
                                text = stringResource(id = R.string.gender),
                                style = CareTheme.typography.subtitle4,
                                color = CareTheme.colors.gray500,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.weight(1f),
                            )

                            Text(
                                text = stringResource(id = R.string.experience),
                                style = CareTheme.typography.subtitle4,
                                color = CareTheme.colors.gray500,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.weight(1f),
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CareTextField(
                                value = "58",
                                onValueChanged = {},
                                leftComponent = {
                                    Text(
                                        text = "세",
                                        style = CareTheme.typography.body3,
                                        color = CareTheme.colors.gray500,
                                    )
                                },
                                modifier = Modifier.weight(1f),
                            )

                            CareTextField(
                                value = "연차",
                                onValueChanged = {},
                                leftComponent = {
                                    Image(
                                        painter = painterResource(com.idle.designresource.R.drawable.ic_arrow_down),
                                        contentDescription = null,
                                    )
                                },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }

                HorizontalDivider(
                    thickness = 8.dp,
                    color = CareTheme.colors.gray050,
                    modifier = Modifier.padding(bottom = 24.dp),
                )

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.address),
                        style = CareTheme.typography.subtitle4,
                        color = CareTheme.colors.gray500,
                    )

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

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.worker_introduce),
                        style = CareTheme.typography.subtitle4,
                        color = CareTheme.colors.gray500,
                    )

                    if (!isEditState) {
                        Text(
                            text = workerIntroduce,
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

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.specialty),
                        style = CareTheme.typography.subtitle4,
                        color = CareTheme.colors.gray500,
                    )

                    if (!isEditState) {
                        Text(
                            text = specialty,
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