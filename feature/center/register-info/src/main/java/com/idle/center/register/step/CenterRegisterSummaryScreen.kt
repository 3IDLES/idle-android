package com.idle.center.register.step

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.idle.center.register.LogRegistrationStep
import com.idle.center.register.RegistrationStep
import com.idle.center.register.RegistrationStep.SUMMARY
import com.idle.compose.addFocusCleaner
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonMedium
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
internal fun CenterRegisterSummaryScreen(
    snackbarHostState: SnackbarHostState,
    centerName: String,
    centerNumber: String,
    centerIntroduce: String,
    centerProfileImageUri: Uri?,
    roadNameAddress: String,
    setRegistrationStep: (RegistrationStep) -> Unit,
    registerCenterProfile: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    BackHandler {
        setRegistrationStep(RegistrationStep.findStep(SUMMARY.step - 1))
    }

    Scaffold(
        topBar = {
            CareSubtitleTopBar(
                title = stringResource(id = R.string.register_center_info),
                onNavigationClick = {
                    setRegistrationStep(
                        RegistrationStep.findStep(
                            RegistrationStep.SUMMARY.step - 1
                        )
                    )
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
                        modifier = Modifier.padding(bottom = 116.dp)
                    )
                }
            )
        },
        containerColor = CareTheme.colors.white000,
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .padding(top = 24.dp)
                .verticalScroll(scrollState),
        ) {
            Text(
                text = "다음의 센터 정보가 맞는지\n확인해주세요.",
                style = CareTheme.typography.heading1,
                color = CareTheme.colors.gray900,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 32.dp),
            )

            Text(
                text = centerName,
                style = CareTheme.typography.heading1,
                color = CareTheme.colors.gray900,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 8.dp),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_address_pin),
                    contentDescription = null,
                )

                Text(
                    text = roadNameAddress,
                    style = CareTheme.typography.body2,
                    color = CareTheme.colors.gray900,
                )
            }

            HorizontalDivider(
                thickness = 8.dp,
                color = CareTheme.colors.gray050,
                modifier = Modifier.padding(vertical = 24.dp),
            )

            Text(
                text = stringResource(id = R.string.center_detail_info),
                style = CareTheme.typography.subtitle1,
                color = CareTheme.colors.gray900,
                modifier = Modifier.padding(bottom = 20.dp, start = 20.dp, end = 20.dp),
            )

            LabeledContent(
                subtitle = stringResource(id = R.string.phone_number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 32.dp),
            ) {
                Text(
                    text = centerNumber,
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray900,
                )
            }

            LabeledContent(
                subtitle = stringResource(id = R.string.center_introduce),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 32.dp),
            ) {
                Text(
                    text = centerIntroduce.ifBlank { "-" },
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray900,
                )
            }

            LabeledContent(
                subtitle = stringResource(id = R.string.center_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
            ) {
                if (centerProfileImageUri == null) {
                    Image(
                        painter = painterResource(R.drawable.ic_profile_empty),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                    )
                } else {
                    AsyncImage(
                        model = centerProfileImageUri,
                        contentDescription = "",
                        placeholder = painterResource(R.drawable.ic_profile_empty),
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .height(243.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 28.dp),
            ) {
                CareButtonMedium(
                    text = stringResource(id = R.string.previous),
                    textColor = CareTheme.colors.gray300,
                    containerColor = CareTheme.colors.white000,
                    border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray200),
                    onClick = { setRegistrationStep(RegistrationStep.findStep(RegistrationStep.SUMMARY.step - 1)) },
                    modifier = Modifier.weight(1f),
                )

                CareButtonMedium(
                    text = stringResource(id = R.string.confirm),
                    onClick = registerCenterProfile,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }

    LogRegistrationStep(step = SUMMARY)
}