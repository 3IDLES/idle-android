package com.idle.auth.center

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.idle.binding.DeepLinkDestination.CenterSignIn
import com.idle.binding.DeepLinkDestination.CenterSignUp
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.designsystem.compose.foundation.PretendardMedium
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterAuthFragment : BaseComposeFragment() {

    override val fragmentViewModel: CenterAuthViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {

        CenterAuthScreen(
            navigateToCenterSignIn = { fragmentViewModel.baseEvent(NavigateTo(CenterSignIn)) },
            navigateToCenterSignUp = { fragmentViewModel.baseEvent(NavigateTo(CenterSignUp)) },
        )
    }
}


@Composable
internal fun CenterAuthScreen(
    navigateToCenterSignIn: () -> Unit,
    navigateToCenterSignUp: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 30.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "센터장님, 환영합니다!",
            style = CareTheme.typography.heading1,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        Spacer(
            modifier = Modifier
                .size(120.dp)
                .background(CareTheme.colors.gray900),
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = CareTheme.colors.gray300,
                        fontFamily = PretendardMedium,
                    )
                ) {
                    append("이미 아이디가 있으신가요? ")
                }
                withStyle(style = SpanStyle(color = CareTheme.colors.orange500)) {
                    append("로그인하기")
                }
            },
            style = CareTheme.typography.subtitle4,
            modifier = Modifier.clickable { navigateToCenterSignIn() }
        )

        CareButtonLarge(
            text = stringResource(id = R.string.start_with_signup),
            onClick = navigateToCenterSignUp,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}