package com.idle.center.register.complete

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.idle.binding.DeepLinkDestination.CenterHome
import com.idle.binding.DeepLinkDestination.CenterProfile
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.center.register.R
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareCard
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterRegisterCompleteFragment : BaseComposeFragment() {
    override val fragmentViewModel: CenterRegisterCompleteViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            CenterRegisterCompleteScreen(
                navigateToCenterProfile = {
                    baseEvent(
                        NavigateTo(
                            CenterProfile,
                            R.id.centerRegisterCompleteFragment,
                        )
                    )
                },
                navigateToCenterHome = {
                    baseEvent(
                        NavigateTo(
                            CenterHome,
                            R.id.centerRegisterCompleteFragment,
                        )
                    )
                }

            )
        }
    }
}

@Composable
internal fun CenterRegisterCompleteScreen(
    navigateToCenterProfile: () -> Unit,
    navigateToCenterHome: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            .padding(20.dp),
    ) {
        Spacer(modifier = Modifier.weight(3f))

        Text(
            text = "센터 회원으로 가입했어요.",
            style = CareTheme.typography.heading1,
            color = CareTheme.colors.gray900,
            modifier = Modifier.padding(bottom = 20.dp),
        )

        Box(
            modifier = Modifier.size(120.dp)
                .background(CareTheme.colors.gray050),
        )

        Text(
            text = "아래의 센터가 맞나요?",
            style = CareTheme.typography.body3,
            color = CareTheme.colors.gray300,
            modifier = Modifier.padding(top = 44.dp, bottom = 8.dp),
        )

        CareCard(
            name = "네얼간이 요양보호소",
            address = "용인시 어쩌고 저쩌고",
            onClick = { navigateToCenterProfile() },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(2f))

        CareButtonLarge(
            text = "시작하기",
            onClick = navigateToCenterHome,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}