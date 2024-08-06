package com.idle.worker.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.idle.binding.DeepLinkDestination.WorkerJobDetail
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareHeadingTopAppBar
import com.idle.designsystem.compose.component.CareTag
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerHomeFragment : BaseComposeFragment() {
    override val fragmentViewModel: WorkerHomeViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            WorkerHomeScreen(
                navigateToJobDetail = { jobId ->
                    fragmentViewModel.baseEvent(NavigateTo(WorkerJobDetail))
                }
            )
        }
    }
}

@Composable
internal fun WorkerHomeScreen(
    navigateToJobDetail: (String) -> Unit,
) {
    Scaffold(
        containerColor = CareTheme.colors.white000,
        topBar = {
            Column {
                CareHeadingTopAppBar(
                    title = "강남구 신사동",
                    rightComponent = {
                        Image(
                            painter = painterResource(com.idle.designresource.R.drawable.ic_address_pin_big),
                            contentDescription = null,
                        )
                    },
                    modifier = Modifier.padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 50.dp,
                        bottom = 20.dp
                    ),
                )

                HorizontalDivider(thickness = 1.dp, color = CareTheme.colors.gray100)
            }
        }
    ) { paddingValue ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(paddingValue)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxSize()
        ) {
            items(listOf(1, 2, 3, 4, 5)) {
                WorkerRecruitmentCard(navigateToJobDetail = navigateToJobDetail)
            }
        }
    }
}

@Composable
private fun WorkerRecruitmentCard(
    navigateToJobDetail: (String) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardColors(
            containerColor = CareTheme.colors.white000,
            contentColor = CareTheme.colors.white000,
            disabledContainerColor = CareTheme.colors.white000,
            disabledContentColor = CareTheme.colors.white000,
        ),
        border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray100),
        modifier = Modifier.clickable { navigateToJobDetail("") }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                CareTag(
                    text = "초보가능",
                    textColor = CareTheme.colors.orange500,
                    backgroundColor = CareTheme.colors.orange100,
                )

                CareTag(
                    text = "D-10",
                    textColor = CareTheme.colors.gray300,
                    backgroundColor = CareTheme.colors.gray050,
                )

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(com.idle.designresource.R.drawable.ic_star_gray),
                    contentDescription = null,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp),
            ) {
                Text(
                    text = "서울특별시 강남구 신사동",
                    style = CareTheme.typography.subtitle2,
                    color = CareTheme.colors.gray900,
                    overflow = TextOverflow.Clip,
                    maxLines = 1,
                    modifier = Modifier.weight(1f),
                )

                Text(
                    text = "도보 15분~20분",
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray500,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }

            Text(
                text = "1등급 78세 여성",
                style = CareTheme.typography.body2,
                color = CareTheme.colors.gray900,
                modifier = Modifier.padding(end = 8.dp, bottom = 4.dp),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)
            ) {
                Image(
                    painter = painterResource(com.idle.designresource.R.drawable.ic_clock),
                    contentDescription = null,
                )

                Text(
                    text = "월, 화, 수, 목, 금 | 09:00 - 15:00",
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray500,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            ) {
                Image(
                    painter = painterResource(com.idle.designresource.R.drawable.ic_money),
                    contentDescription = null,
                )

                Text(
                    text = "시급 12,500 원",
                    style = CareTheme.typography.body3,
                    color = CareTheme.colors.gray500,
                )
            }

            Button(
                onClick = {},
                contentPadding = PaddingValues(vertical = 10.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonColors(
                    containerColor = CareTheme.colors.orange500,
                    contentColor = CareTheme.colors.orange500,
                    disabledContentColor = CareTheme.colors.gray200,
                    disabledContainerColor = CareTheme.colors.gray200,
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.recruit),
                    style = CareTheme.typography.subtitle4,
                    color = CareTheme.colors.white000,
                )
            }
        }
    }
}