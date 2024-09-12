@file:OptIn(ExperimentalMaterialApi::class)

package com.idle.worker.profile

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImage
import com.idle.analytics.helper.TrackScreenViewEvent
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareBottomSheetLayout
import com.idle.designsystem.compose.component.CareButtonCardMedium
import com.idle.designsystem.compose.component.CareButtonMedium
import com.idle.designsystem.compose.component.CareButtonRound
import com.idle.designsystem.compose.component.CareClickableTextField
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.component.CareTag
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.component.CareToggleText
import com.idle.designsystem.compose.component.CareWheelPicker
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.profile.JobSearchStatus
import com.idle.domain.model.profile.JobSearchStatus.NO
import com.idle.domain.model.profile.JobSearchStatus.UNKNOWN
import com.idle.domain.model.profile.JobSearchStatus.YES
import com.idle.domain.model.profile.WorkerProfile
import com.idle.post.code.PostCodeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class WorkerProfileFragment : BaseComposeFragment() {
    private val args: WorkerProfileFragmentArgs by navArgs()
    override val fragmentViewModel: WorkerProfileViewModel by viewModels()

    val postCodeDialog: PostCodeFragment? by lazy {
        PostCodeFragment().apply {
            onDismissCallback = {
                findNavController().currentBackStackEntry?.savedStateHandle?.let {
                    val roadName = it.get<String>("roadNameAddress")
                    val lotNumber = it.get<String>("lotNumberAddress")

                    fragmentViewModel.setRoadNameAddress(roadName ?: return@let)
                    fragmentViewModel.setLotNumberAddress(lotNumber ?: return@let)
                }
            }
        }
    }

    @Composable
    override fun ComposeLayout() {
        val isMyProfile = args.workerId == "default"

        fragmentViewModel.apply {
            val workerProfile by workerProfile.collectAsStateWithLifecycle()
            val workerIntroduce by workerIntroduce.collectAsStateWithLifecycle()
            val specialty by specialty.collectAsStateWithLifecycle()
            val isEditState by isEditState.collectAsStateWithLifecycle()
            val profileImageUri by profileImageUri.collectAsStateWithLifecycle()
            val experienceYear by experienceYear.collectAsStateWithLifecycle()
            val roadNameAddress by roadNameAddress.collectAsStateWithLifecycle()
            val jobSearchStatus by jobSearchStatus.collectAsStateWithLifecycle()

            val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = { uri -> setProfileImageUrl(uri) }
            )

            LaunchedEffect(true) {
                if (args.workerId == "default") {
                    getMyWorkerProfile()
                } else {
                    getWorkerProfile(args.workerId)
                }
            }

            workerProfile?.let { profile ->
                CareStateAnimator(
                    targetState = isEditState,
                    transitionCondition = isEditState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    WorkerProfileScreen(
                        snackbarHostState = snackbarHostState,
                        isMyProfile = isMyProfile,
                        isEditState = isEditState,
                        workerProfile = profile,
                        workerIntroduce = workerIntroduce,
                        specialty = specialty,
                        experienceYear = experienceYear,
                        profileImageUri = profileImageUri,
                        jobSearchStatus = jobSearchStatus,
                        singlePhotoPickerLauncher = singlePhotoPickerLauncher,
                        roadNameAddress = roadNameAddress,
                        showPostCodeDialog = {
                            if (!(postCodeDialog?.isAdded == true || postCodeDialog?.isVisible == true)) {
                                postCodeDialog?.show(parentFragmentManager, "PostCodeFragment")
                            }
                        },
                        onSpecialtyChanged = ::setSpecialty,
                        onWorkerIntroduceChanged = ::setWorkerIntroduce,
                        onExperienceYearChanged = ::setExperienceYear,
                        onJobSearchStatusChanged = ::setJobSearchStatus,
                        updateWorkerProfile = ::updateWorkerProfile,
                        setEditState = ::setEditState,
                    )
                }
            }
        }
    }
}

@Composable
internal fun WorkerProfileScreen(
    snackbarHostState: SnackbarHostState,
    isMyProfile: Boolean,
    workerProfile: WorkerProfile,
    workerIntroduce: String,
    specialty: String,
    profileImageUri: Uri?,
    experienceYear: Int?,
    roadNameAddress: String,
    jobSearchStatus: JobSearchStatus,
    isEditState: Boolean,
    singlePhotoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    showPostCodeDialog: () -> Unit,
    setEditState: (Boolean) -> Unit,
    onSpecialtyChanged: (String) -> Unit,
    onWorkerIntroduceChanged: (String) -> Unit,
    onExperienceYearChanged: (Int) -> Unit,
    onJobSearchStatusChanged: (JobSearchStatus) -> Unit,
    updateWorkerProfile: () -> Unit,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
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
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                var localExperienceYear by remember { mutableStateOf("1년차") }

                Text(
                    text = stringResource(id = R.string.experience),
                    style = CareTheme.typography.heading3,
                    color = CareTheme.colors.gray900,
                )

                Spacer(modifier = Modifier.height(80.dp))

                CareWheelPicker(
                    items = (1..20).map {
                        it.toString() + "년차"
                    }.toList(),
                    initIndex = experienceYear?.minus(1) ?: 0,
                    onItemSelected = { localExperienceYear = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(80.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    CareButtonMedium(
                        text = stringResource(id = R.string.cancel_short),
                        border = BorderStroke(
                            width = 1.dp,
                            color = CareTheme.colors.orange400
                        ),
                        containerColor = CareTheme.colors.white000,
                        textColor = CareTheme.colors.orange500,
                        onClick = {
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )

                    CareButtonMedium(
                        text = stringResource(id = R.string.save),
                        onClick = {
                            coroutineScope.launch {
                                onExperienceYearChanged(
                                    localExperienceYear.dropLast(2).toIntOrNull() ?: -1
                                )
                                sheetState.hide()
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier.background(
                        if (!isEditState) CareTheme.colors.gray050
                        else CareTheme.colors.white000
                    ),
                ) {
                    CareSubtitleTopBar(
                        title = if (isMyProfile) stringResource(id = R.string.my_profile)
                        else stringResource(id = R.string.carer_profile),
                        onNavigationClick = {
                            if (isEditState) setEditState(false)
                            else onBackPressedDispatcher?.onBackPressed()
                        },
                        leftComponent = {
                            if (isMyProfile) {
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
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, top = 48.dp, end = 20.dp, bottom = 12.dp),
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = { data ->
                        CareSnackBar(
                            data = data,
                            modifier = Modifier.padding(bottom = 20.dp)
                        )
                    }
                )
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
                            AsyncImage(
                                model = profileImageUri ?: R.drawable.ic_worker_photo_default,
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(96.dp)
                                    .clip(CircleShape),
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
                            text = workerProfile.workerName,
                            style = CareTheme.typography.heading2,
                            color = CareTheme.colors.gray900,
                            modifier = Modifier.padding(bottom = 8.dp),
                        )

                        CareToggleText(
                            rightChecked = jobSearchStatus == YES,
                            leftText = stringResource(id = R.string.is_working),
                            rightText = stringResource(id = R.string.is_job_searching),
                            onCheckedChanged = { onJobSearchStatusChanged(if (it) YES else NO) },
                            modifier = Modifier.padding(bottom = 16.dp),
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

                    if (!isMyProfile) {
                        CareButtonCardMedium(
                            text = stringResource(id = R.string.call),
                            containerColor = CareTheme.colors.white000,
                            textColor = CareTheme.colors.orange500,
                            border = BorderStroke(width = 1.dp, color = CareTheme.colors.orange400),
                            onClick = {
                                val number = "tel:${workerProfile.phoneNumber}"
                                val dialIntent = Intent(Intent.ACTION_DIAL, number.toUri())
                                context.startActivity(dialIntent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp, start = 35.dp, end = 35.dp),
                        )
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
                                value = experienceYear?.let {
                                    "${experienceYear}년차"
                                } ?: "-",
                                hint = stringResource(id = R.string.year),
                                leftComponent = {
                                    Image(
                                        painter = painterResource(R.drawable.ic_arrow_down),
                                        contentDescription = null,
                                    )
                                },
                                onClick = {
                                    coroutineScope.launch {
                                        sheetState.show()
                                    }
                                },
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
                                text = workerProfile.roadNameAddress,
                                style = CareTheme.typography.body3,
                                color = CareTheme.colors.gray900,
                            )
                        } else {
                            CareClickableTextField(
                                value = roadNameAddress,
                                onClick = showPostCodeDialog,
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

    TrackScreenViewEvent(screenName = "carer_profile_screen")
}