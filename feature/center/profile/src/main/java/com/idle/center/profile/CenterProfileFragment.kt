package com.idle.center.profile

import android.net.Uri
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImage
import com.idle.analytics.helper.TrackScreenViewEvent
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonRound
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.component.LoadingCircle
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.profile.CenterProfile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterProfileFragment : BaseComposeFragment() {
    private val args: CenterProfileFragmentArgs by navArgs()
    override val fragmentViewModel: CenterProfileViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        val isMyProfile = args.centerId == "default"

        fragmentViewModel.apply {
            val centerProfile by centerProfile.collectAsStateWithLifecycle()
            val centerOfficeNumber by centerOfficeNumber.collectAsStateWithLifecycle()
            val centerIntroduce by centerIntroduce.collectAsStateWithLifecycle()
            val isEditState by isEditState.collectAsStateWithLifecycle()
            val profileImageUri by profileImageUri.collectAsStateWithLifecycle()
            val isUpdateLoading by isUpdateLoading.collectAsStateWithLifecycle()

            LaunchedEffect(true) {
                if (args.centerId == "default") {
                    getMyCenterProfile()
                } else {
                    getCenterProfile(args.centerId)
                }
            }

            centerProfile?.let { profile ->
                CareStateAnimator(
                    targetState = isEditState,
                    transitionCondition = isEditState
                ) {
                    CenterProfileScreen(
                        isMyProfile = isMyProfile,
                        centerProfile = profile,
                        centerOfficeNumber = centerOfficeNumber,
                        centerIntroduce = centerIntroduce,
                        profileImageUri = profileImageUri,
                        isEditState = isEditState,
                        isUpdateLoading = isUpdateLoading,
                        onCenterOfficeNumberChanged = ::setCenterOfficeNumber,
                        onCenterIntroduceChanged = ::setCenterIntroduce,
                        onProfileImageUriChanged = ::setProfileImageUrl,
                        updateCenterProfile = ::updateCenterProfile,
                        setEditState = ::setEditState,
                    )
                }
            }
        }
    }
}

@Composable
internal fun CenterProfileScreen(
    isMyProfile: Boolean,
    centerProfile: CenterProfile,
    centerOfficeNumber: String,
    centerIntroduce: String,
    profileImageUri: Uri?,
    isEditState: Boolean,
    isUpdateLoading: Boolean,
    setEditState: (Boolean) -> Unit,
    onCenterOfficeNumberChanged: (String) -> Unit,
    onCenterIntroduceChanged: (String) -> Unit,
    onProfileImageUriChanged: (Uri?) -> Unit,
    updateCenterProfile: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val scrollState = rememberScrollState()
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            onProfileImageUriChanged(uri ?: return@rememberLauncherForActivityResult)
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CareSubtitleTopBar(
                    title = if (isMyProfile) stringResource(id = R.string.my_center_info)
                    else stringResource(id = R.string.center_info),
                    onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                    leftComponent = {
                        if (isMyProfile) {
                            if (isEditState) {
                                Text(
                                    text = stringResource(id = R.string.save),
                                    style = CareTheme.typography.subtitle2,
                                    color = CareTheme.colors.orange500,
                                    modifier = Modifier.clickable {
                                        updateCenterProfile()
                                    }
                                )
                            } else {
                                CareButtonRound(
                                    text = stringResource(id = R.string.edit),
                                    onClick = { setEditState(true) }
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, top = 48.dp, end = 20.dp, bottom = 12.dp),
                )
            },
            containerColor = CareTheme.colors.white000,
            modifier = Modifier.addFocusCleaner(focusManager),
        ) { paddingValues ->
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = centerProfile.centerName,
                        style = CareTheme.typography.heading1,
                        color = CareTheme.colors.black,
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_address_pin),
                            contentDescription = "위치를 알려주는 핀 이미지 입니다.",
                        )

                        Text(
                            text = centerProfile.lotNumberAddress,
                            style = CareTheme.typography.body2,
                            color = CareTheme.colors.black,
                        )
                    }
                }

                HorizontalDivider(
                    color = CareTheme.colors.gray050,
                    thickness = 8.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.center_detail_info),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.black,
                    )

                    LabeledContent(
                        subtitle = stringResource(id = R.string.office_number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    ) {
                        if (!isEditState) {
                            Text(
                                text = centerOfficeNumber,
                                style = CareTheme.typography.body3,
                                color = CareTheme.colors.black,
                            )
                        } else {
                            CareTextField(
                                value = centerOfficeNumber,
                                keyboardType = KeyboardType.Number,
                                onValueChanged = onCenterOfficeNumberChanged,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    LabeledContent(
                        subtitle = stringResource(id = R.string.center_introduce),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    ) {
                        if (!isEditState) {
                            Text(
                                text = centerIntroduce,
                                style = CareTheme.typography.body3,
                                color = CareTheme.colors.black,
                            )
                        } else {
                            CareTextFieldLong(
                                value = centerIntroduce.ifEmpty { "-" },
                                onValueChanged = onCenterIntroduceChanged,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(bottom = 52.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.center_image),
                            style = CareTheme.typography.subtitle4,
                            color = CareTheme.colors.gray500,
                        )

                        Box(
                            modifier = Modifier
                                .padding(bottom = 60.dp)
                                .clickable {
                                    if (isEditState) singlePhotoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                        ) {
                            if (centerProfile.profileImageUrl == null && profileImageUri == null) {
                                if (!isEditState) {
                                    Image(
                                        painter = painterResource(R.drawable.ic_profile_empty),
                                        contentDescription = null,
                                        contentScale = ContentScale.FillWidth,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(6.dp))
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(R.drawable.ic_profile_empty_edit),
                                        contentDescription = null,
                                        contentScale = ContentScale.FillWidth,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(6.dp))
                                    )
                                }
                            } else {
                                AsyncImage(
                                    model = profileImageUri ?: centerProfile.profileImageUrl,
                                    contentDescription = "",
                                    placeholder = painterResource(R.drawable.ic_profile_empty),
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier
                                        .height(243.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(6.dp))
                                )

                                if (isEditState) {
                                    Spacer(
                                        modifier = Modifier
                                            .matchParentSize()
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color.Black.copy(alpha = 0.4f))
                                    )

                                    Image(
                                        painter = painterResource(R.drawable.ic_edit_pencil),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (isUpdateLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CareTheme.colors.black.copy(alpha = 0.1f))
            ) {
                LoadingCircle(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    TrackScreenViewEvent(screenName = "center_profile_screen")
}