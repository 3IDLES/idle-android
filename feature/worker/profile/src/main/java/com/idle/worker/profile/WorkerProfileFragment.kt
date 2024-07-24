package com.idle.worker.profile

import android.net.Uri
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designsystem.compose.component.CareButtonStrokeSmall
import com.idle.designsystem.compose.component.CareSubtitleTopAppBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.profile.CenterProfile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerProfileFragment : BaseComposeFragment() {
    override val fragmentViewModel: WorkerProfileViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val workerProfile by workerProfile.collectAsStateWithLifecycle()
            val workerIntroduce by workerIntroduce.collectAsStateWithLifecycle()
            val isEditState by isEditState.collectAsStateWithLifecycle()
            val profileImageUri by profileImageUri.collectAsStateWithLifecycle()

            val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = { uri -> setProfileImageUrl(uri) }
            )

            WorkerProfileScreen(
                isEditState = isEditState,
                workerProfile = workerProfile,
                workerIntroduce = workerIntroduce,
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
    workerProfile: CenterProfile,
    workerIntroduce: String,
    profileImageUri: Uri?,
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
            CareSubtitleTopAppBar(
                title = "내 프로필",
                onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                leftComponent = {
                    if (isEditState) {
                        Text(
                            text = "저장",
                            style = CareTheme.typography.subtitle2,
                            color = CareTheme.colors.orange500,
                            modifier = Modifier.clickable {
                                updateWorkerProfile()
                            }
                        )
                    } else {
                        CareButtonStrokeSmall(
                            text = "수정하기",
                            onClick = { setEditState(true) },
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp, bottom = 44.dp, end = 28.dp),
            )
        },
        containerColor = CareTheme.colors.white000,
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(paddingValues)
        ) {

        }
    }
}