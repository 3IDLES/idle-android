package com.idle.center.register.complete

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idle.binding.DeepLinkDestination.CenterHome
import com.idle.binding.DeepLinkDestination.CenterProfile
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.center.register.info.R
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareCard
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterRegisterCompleteFragment : BaseComposeFragment() {
    override val fragmentViewModel: RegisterCenterInfoCompleteViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val centerProfile by centerProfile.collectAsStateWithLifecycle()

            centerProfile?.let {
                CenterRegisterCompleteScreen(
                    centerProfile = it,
                    navigateToCenterProfile = {
                        baseEvent(
                            NavigateTo(
                                CenterProfile,
                                R.id.registerCenterInfoCompleteFragment
                            )
                        )
                    },
                    navigateToCenterHome = {
                        baseEvent(NavigateTo(CenterHome, R.id.registerCenterInfoCompleteFragment))
                    }
                )
            }
        }
    }
}

@Composable
internal fun CenterRegisterCompleteScreen(
    centerProfile: com.idle.domain.model.profile.CenterProfile,
    navigateToCenterProfile: () -> Unit,
    navigateToCenterHome: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.weight(3f))

        Text(
            text = "센터 회원으로 가입했어요.",
            style = CareTheme.typography.heading1,
            color = CareTheme.colors.gray900,
            modifier = Modifier.padding(bottom = 20.dp),
        )

        Image(
            painter = painterResource(id = com.idle.designresource.R.drawable.ic_check_with_circle),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.size(120.dp),
        )

        Text(
            text = "아래의 센터가 맞나요?",
            style = CareTheme.typography.body3,
            color = CareTheme.colors.gray300,
            modifier = Modifier.padding(top = 44.dp, bottom = 8.dp),
        )

        CareCard(
            title = centerProfile.centerName,
            description = centerProfile.roadNameAddress,
            onClick = { navigateToCenterProfile() },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(2f))

        CareButtonLarge(
            text = "시작하기",
            onClick = navigateToCenterHome,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }
}