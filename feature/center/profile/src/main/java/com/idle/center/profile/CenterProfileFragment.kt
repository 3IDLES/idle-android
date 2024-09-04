package com.idle.center.profile

import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonRound
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.component.SnackBarType
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.profile.CenterProfile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterProfileFragment : BaseComposeFragment() {
    override val fragmentViewModel: CenterProfileViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val centerProfile by centerProfile.collectAsStateWithLifecycle()
            val centerOfficeNumber by centerOfficeNumber.collectAsStateWithLifecycle()
            val centerIntroduce by centerIntroduce.collectAsStateWithLifecycle()
            val isEditState by isEditState.collectAsStateWithLifecycle()
            val profileImageUri by profileImageUri.collectAsStateWithLifecycle()

            centerProfile?.let {
                CenterProfileScreen(
                    snackbarHostState = snackbarHostState,
                    centerProfile = it,
                    centerOfficeNumber = centerOfficeNumber,
                    centerIntroduce = centerIntroduce,
                    profileImageUri = profileImageUri,
                    isEditState = isEditState,
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

@Composable
internal fun CenterProfileScreen(
    snackbarHostState: SnackbarHostState,
    centerProfile: CenterProfile,
    centerOfficeNumber: String,
    centerIntroduce: String,
    profileImageUri: Uri?,
    isEditState: Boolean,
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
        onResult = { uri -> onProfileImageUriChanged(uri) }
    )

    Scaffold(
        topBar = {
            CareSubtitleTopBar(
                title = stringResource(id = R.string.my_center_info),
                onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                leftComponent = {
                    if (isEditState) {
                        Text(
                            text = stringResource(id = R.string.save),
                            style = CareTheme.typography.subtitle2,
                            color = CareTheme.colors.orange500,
                            modifier = Modifier.clickable {
                                updateCenterProfile()
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp, end = 20.dp, bottom = 12.dp),
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    CareSnackBar(
                        data = data,
                        modifier = Modifier.padding(bottom = 138.dp)
                    )
                }
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
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(
                    text = centerProfile.centerName,
                    style = CareTheme.typography.heading1,
                    color = CareTheme.colors.gray900,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_address_pin),
                        contentDescription = "위치를 알려주는 핀 이미지 입니다.",
                    )

                    Text(
                        text = centerProfile.lotNumberAddress,
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )
                }
            }

            HorizontalDivider(color = CareTheme.colors.gray050, thickness = 8.dp)

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 4.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.center_detail_info),
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.gray900,
                    )

                    if (!isEditState) {
                        CareButtonRound(
                            text = stringResource(id = R.string.edit),
                            onClick = { setEditState(true) }
                        )
                    }
                }

                LabeledContent(
                    subtitle = stringResource(id = R.string.phone_number),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (!isEditState) {
                        Text(
                            text = centerOfficeNumber,
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray900,
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
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (!isEditState) {
                        Text(
                            text = centerIntroduce,
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray900,
                        )
                    } else {
                        CareTextFieldLong(
                            value = centerIntroduce,
                            onValueChanged = onCenterIntroduceChanged,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(id = R.string.center_image),
                        style = CareTheme.typography.subtitle4,
                        color = CareTheme.colors.gray500,
                    )

                    Box(modifier = Modifier
                        .padding(bottom = 60.dp)
                        .clickable {
                            if (isEditState) singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }) {
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
}