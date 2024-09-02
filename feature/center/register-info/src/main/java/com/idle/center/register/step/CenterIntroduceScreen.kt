package com.idle.center.register.step

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.idle.center.register.RegistrationStep
import com.idle.center.register.RegistrationStep.INTRODUCE
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareProgressBar
import com.idle.designsystem.compose.component.CareSubtitleTopBar
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

    BackHandler { setRegistrationStep(RegistrationStep.findStep(INTRODUCE.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {

        Text(
            text = stringResource(id = R.string.introduce_my_center),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
            modifier = Modifier.padding(bottom = 6.dp),
        )

        Text(
            text = stringResource(id = R.string.center_introduce_title),
            style = CareTheme.typography.body3,
            color = CareTheme.colors.gray300,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        LabeledContent(
            subtitle = stringResource(id = R.string.center_introduce),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
        ) {
            CareTextFieldLong(
                value = centerIntroduce,
                hint = stringResource(id = R.string.center_introduce_hint),
                onValueChanged = onCenterIntroduceChanged,
                onDone = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )
        }

        LabeledContent(
            subtitle = stringResource(id = R.string.center_image),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
        ) {
            AsyncImage(
                model = centerProfileImageUri ?: R.drawable.ic_profile_empty_edit,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(254.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .clickable {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            )
        }

        CareButtonLarge(
            text = stringResource(id = R.string.next),
            onClick = { if (centerIntroduce.isNotBlank()) registerCenterProfile() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }
}

@Preview
@Composable
fun PreviewIntroduceScreen() {
    Scaffold(
        topBar = {
            Column(modifier = Modifier.padding(start = 12.dp, top = 48.dp, end = 20.dp)) {
                CareSubtitleTopBar(
                    title = "센터 정보 등록",
                    onNavigationClick = {},
                    modifier = Modifier.fillMaxWidth(),
                )

                CareProgressBar(
                    currentStep = INTRODUCE.ordinal,
                    totalSteps = RegistrationStep.entries.size,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
                )
            }
        },
    ) { paddingValue ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValue)
                .padding(start = 20.dp, end = 20.dp, top = 24.dp),
        ) {
            CenterIntroduceScreen(
                centerIntroduce = "반갑습니다.",
                centerProfileImageUri = "https://cdn.pixabay.com/photo/2016/10/20/18/35/earth-1756274_640.jpg".toUri(),
                onCenterIntroduceChanged = {},
                onProfileImageUriChanged = {},
                setRegistrationStep = {},
                registerCenterProfile = {},
            )
        }
    }
}