package com.idle.signup.center.step

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.idle.center.register.RegistrationStep
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
internal fun CenterIntroduceScreen(
    centerIntroduce: String,
    centerProfileImageUri: Uri?,
    onCenterIntroduceChanged: (String) -> Unit,
    onProfileImageUriChanged: (Uri?) -> Unit,
    setRegistrationStep: (RegistrationStep) -> Unit,
    registerCenterProfile: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val scrollState = rememberScrollState()

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> onProfileImageUriChanged(uri) }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler { setRegistrationStep(RegistrationStep.ADDRESS) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize()
            .verticalScroll(scrollState),
    ) {

        Text(
            text = "우리 센터를 소개해주세요!",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
            modifier = Modifier.padding(bottom = 6.dp),
        )

        Text(
            text = "센터 소개글을 작성하면 보호사 매칭 성공률이 높아져요.",
            style = CareTheme.typography.body3,
            color = CareTheme.colors.gray300,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        LabeledContent(
            subtitle = "센터 소개",
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 20.dp),
        ) {
            CareTextFieldLong(
                value = centerIntroduce,
                hint = "추가적으로 요구사항이 있다면 작성해주세요.(예: 어쩌고저쩌고)",
                onValueChanged = onCenterIntroduceChanged,
                onDone = {},
                modifier = Modifier.fillMaxWidth()
                    .focusRequester(focusRequester)
            )
        }

        LabeledContent(
            subtitle = "센터 사진",
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 24.dp),
        ) {
            AsyncImage(
                model = centerProfileImageUri ?: R.drawable.ic_profile_empty_edit,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .clickable {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            )
        }

        CareButtonLarge(
            text = "다음",
            onClick = { if (centerIntroduce.isNotBlank()) registerCenterProfile() },
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 30.dp),
        )
    }
}