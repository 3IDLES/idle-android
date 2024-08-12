package com.idle.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.idle.analytics.AnalyticsHelper
import com.idle.binding.DeepLinkDestination.CenterSignIn
import com.idle.binding.DeepLinkDestination.CenterSignUp
import com.idle.binding.DeepLinkDestination.WorkerSignUp
import com.idle.binding.base.CareBaseEvent.NavigateTo
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designresource.R.string
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.designsystem.compose.foundation.PretendardMedium
import com.idle.domain.model.auth.UserRole
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class AuthFragment : BaseComposeFragment() {
    override val fragmentViewModel: AuthViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val userRole by userRole.collectAsStateWithLifecycle()

            AuthScreen(
                userRole = userRole,
                onUserRoleChanged = ::setUserRole,
                navigateToCenterSignIn = { fragmentViewModel.baseEvent(NavigateTo(CenterSignIn)) },
                navigateToCenterSignUp = { fragmentViewModel.baseEvent(NavigateTo(CenterSignUp)) },
                navigateToWorkerSignUp = { fragmentViewModel.baseEvent(NavigateTo(WorkerSignUp)) },
            )
        }
    }
}


@Composable
internal fun AuthScreen(
    userRole: UserRole?,
    onUserRoleChanged: (UserRole) -> Unit,
    navigateToCenterSignIn: () -> Unit,
    navigateToCenterSignUp: () -> Unit,
    navigateToWorkerSignUp: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.weight(3f))

            Text(
                text = stringResource(id = string.auth_title),
                style = CareTheme.typography.heading1,
                color = CareTheme.colors.gray900,
            )

            Spacer(modifier = Modifier.weight(2f))

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors().copy(
                        containerColor = if (userRole == UserRole.CENTER) CareTheme.colors.orange100
                        else CareTheme.colors.white000,
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (userRole == UserRole.CENTER) CareTheme.colors.orange400
                        else CareTheme.colors.gray100,
                    ),
                    modifier = Modifier
                        .wrapContentHeight()
                        .clickable { onUserRoleChanged(UserRole.CENTER) }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 28.dp),
                    ) {
                        Text(
                            text = stringResource(id = string.start_center),
                            style = CareTheme.typography.subtitle2,
                            color = CareTheme.colors.gray900,
                            textAlign = TextAlign.Center,
                        )

                        Image(
                            painter = painterResource(id = com.idle.designresource.R.drawable.ic_center),
                            contentDescription = "센터 일러스트 입니다."
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors().copy(
                        containerColor = if (userRole == UserRole.WORKER) CareTheme.colors.orange100
                        else CareTheme.colors.white000,
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (userRole == UserRole.WORKER) CareTheme.colors.orange400
                        else CareTheme.colors.gray100,
                    ),
                    modifier = Modifier
                        .wrapContentHeight()
                        .clickable { onUserRoleChanged(UserRole.WORKER) }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 28.dp),
                    ) {
                        Text(
                            text = stringResource(id = string.start_worker),
                            style = CareTheme.typography.subtitle2,
                            color = CareTheme.colors.gray900,
                            textAlign = TextAlign.Center,
                        )

                        Image(
                            painter = painterResource(id = com.idle.designresource.R.drawable.ic_carer),
                            contentDescription = "요양 보호사 일러스트 입니다."
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(6f))
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp),
        ) {
            when (userRole) {
                UserRole.WORKER -> {
                    CareButtonLarge(
                        text = stringResource(id = string.start_with_phone),
                        onClick = { navigateToWorkerSignUp() },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                UserRole.CENTER -> {
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
                        text = stringResource(id = string.start_with_signup),
                        onClick = { navigateToCenterSignUp() },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                else -> Unit
            }
        }
    }
}