package com.idle.center.jobposting.complete

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.idle.compose.base.BaseComposeFragment
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.foundation.CareTheme

class JobPostingCompleteFragment : BaseComposeFragment() {

    override val fragmentViewModel: JobPostingCompleteViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            JobPostingCompleteScreen()
        }
    }
}

@Composable
internal fun JobPostingCompleteScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            .background(CareTheme.colors.white000)
            .padding(start = 20.dp, end = 20.dp, bottom = 30.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.ic_check_with_circle),
            contentDescription = null,
            modifier = Modifier.padding(bottom = 36.dp),
        )

        Text(
            text = "요양보호사 구인 공고를\n" + "등록했어요!",
            style = CareTheme.typography.heading1,
            color = CareTheme.colors.gray900,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "다음",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}