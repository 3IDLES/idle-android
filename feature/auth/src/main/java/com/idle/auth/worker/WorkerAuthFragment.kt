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
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.idle.binding.DeepLinkDestination.WorkerSignUp
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerAuthFragment : BaseComposeFragment() {
    override val fragmentViewModel: WorkerAuthViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        WorkerAuthScreen(
            navigateToWorkerSignUp = { fragmentViewModel.baseEvent(NavigateTo(WorkerSignUp)) }
        )
    }
}


@Composable
internal fun WorkerAuthScreen(
    navigateToWorkerSignUp: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 30.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "요양 보호사님, 환영합니다!",
            style = CareTheme.typography.heading1,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        Spacer(
            modifier = Modifier.size(120.dp)
                .background(CareTheme.colors.gray900),
        )

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "휴대폰 번호로 시작하기",
            onClick = navigateToWorkerSignUp,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}