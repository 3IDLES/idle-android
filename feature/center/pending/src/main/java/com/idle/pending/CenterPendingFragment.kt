@file:OptIn(ExperimentalFoundationApi::class)

package com.idle.pending

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.navArgs
import com.idle.center.pending.R
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareDialog
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.profile.CenterManagerAccountStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterPendingFragment : BaseComposeFragment() {
    private val args: CenterPendingFragmentArgs by navArgs()
    override val fragmentViewModel: CenterPendingViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val status by status.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                setStatus(args.status)
            }

            CenterPendingScreen(
                snackbarHostState = snackbarHostState,
                status = status,
                logout = ::logout,
                sendVerificationRequest = ::sendVerificationRequest,
            )
        }
    }
}

@Composable
private fun CenterPendingScreen(
    snackbarHostState: SnackbarHostState,
    status: CenterManagerAccountStatus,
    logout: () -> Unit,
    sendVerificationRequest: () -> Unit,
) {
    Scaffold(
        containerColor = CareTheme.colors.white000,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    CareSnackBar(
                        data = data,
                        modifier = Modifier.padding(bottom = 106.dp),
                    )
                }
            )
        },
    ) { paddingValue ->
        val actualPageCount = 3
        val pageCount = 600
        val maxNumOfRounds = 600 / actualPageCount
        val pagerState = rememberPagerState(
            initialPage = (maxNumOfRounds / 2) * actualPageCount,
            pageCount = { pageCount },
        )

        var showDialog by remember { mutableStateOf(false) }
        if (showDialog) {
            CareDialog(
                title = stringResource(id = com.idle.designresource.R.string.logout_dialog),
                leftButtonText = stringResource(id = com.idle.designresource.R.string.cancel),
                rightButtonText = stringResource(id = com.idle.designresource.R.string.logout),
                onDismissRequest = { showDialog = false },
                onLeftButtonClick = { showDialog = false },
                onRightButtonClick = {
                    showDialog = false
                    logout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                leftButtonTextColor = CareTheme.colors.gray300,
                leftButtonColor = CareTheme.colors.white000,
                leftButtonBorder = BorderStroke(1.dp, CareTheme.colors.gray100),
                rightButtonTextColor = CareTheme.colors.white000,
                rightButtonColor = CareTheme.colors.red,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 56.dp, bottom = 28.dp),
                ) {
                    PageIndicators(
                        totalPages = actualPageCount,
                        currentPage = pagerState.currentPage % actualPageCount,
                        modifier = Modifier.align(Alignment.Center),
                    )

                    Text(
                        text = stringResource(id = com.idle.designresource.R.string.logout),
                        style = CareTheme.typography.body3,
                        color = CareTheme.colors.gray300,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 20.dp)
                            .clickable { showDialog = true },
                    )
                }

                HorizontalPager(state = pagerState) { page ->
                    val actualPage = page % actualPageCount
                    when (actualPage) {
                        0 -> PendingPageContent(
                            headerText = "요양보호사 정보를 \n한눈에 확인할 수 있어요",
                            imageResId = R.drawable.ic_pending_screen_1
                        )

                        1 -> PendingPageContent(
                            headerText = "요양보호사 구인 공고를\n간편하게 등록할 수 있어요",
                            imageResId = R.drawable.ic_pending_screen_2
                        )

                        2 -> PendingPageContent(
                            headerText = "요양보호사를 즐겨찾기하고\n직접 연락해 능동적으로 구인해요",
                            imageResId = R.drawable.ic_pending_screen_3
                        )
                    }
                }
            }

            CareButtonLarge(
                text = if (status == CenterManagerAccountStatus.NEW) "인증 요청하기"
                else "관리자 인증 요청 중이에요",
                onClick = sendVerificationRequest,
                enable = status == CenterManagerAccountStatus.NEW,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CareTheme.colors.white000)
                    .align(Alignment.BottomCenter)
                    .padding(top = 12.dp, start = 20.dp, end = 20.dp, bottom = 28.dp),
            )
        }
    }
}

@Composable
private fun PageIndicators(
    totalPages: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier,
    ) {
        repeat(totalPages) { page ->
            val color = if (currentPage == page) CareTheme.colors.orange500
            else CareTheme.colors.gray100

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(8.dp)
                    .background(color),
            )
        }
    }
}

@Composable
private fun PendingPageContent(headerText: String, imageResId: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "센터 관리자 인증 시",
            style = CareTheme.typography.heading3,
            color = CareTheme.colors.orange500,
            modifier = Modifier.padding(bottom = 4.dp),
        )

        Text(
            text = headerText,
            style = CareTheme.typography.heading1,
            textAlign = TextAlign.Center,
            color = CareTheme.colors.gray700,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
        )
    }
}