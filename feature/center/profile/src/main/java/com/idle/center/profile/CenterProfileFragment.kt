package com.idle.center.profile

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.idle.common.ui.binding.R
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designsystem.compose.component.CareButtonStrokeSmall
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.component.CareTopAppBar
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CenterProfileFragment : BaseComposeFragment() {
    override val fragmentViewModel: CenterProfileViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val (isEditState, setEditState) = remember { mutableStateOf(false) }
            CenterProfileScreen(
                isEditState = isEditState,
                setEditState = setEditState,
            )
        }
    }
}

@Composable
internal fun CenterProfileScreen(
    isEditState: Boolean,
    setEditState: (Boolean) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CareTopAppBar(
                title = "내 센터 정보",
                onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                leftComponent = {
                    if (isEditState) {
                        Text(
                            text = "저장",
                            style = CareTheme.typography.subtitle2,
                            color = CareTheme.colors.orange500,
                            modifier = Modifier.clickable {
                                setEditState(false)
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 12.dp, top = 20.dp, bottom = 25.dp, end = 28.dp),
            )
        },
        containerColor = CareTheme.colors.white000,
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(paddingValues)
                .fillMaxWidth()
                .verticalScroll(scrollState),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "네 얼간이 방문요양센터",
                    style = CareTheme.typography.heading1,
                    color = CareTheme.colors.gray900,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_address_pin),
                        contentDescription = "위치를 알려주는 핀 이미지 입니다.",
                    )

                    Text(
                        text = "강남구 삼성동 512-3",
                        style = CareTheme.typography.body2,
                        color = CareTheme.colors.gray900,
                    )
                }
            }

            HorizontalDivider(color = CareTheme.colors.gray050, thickness = 8.dp)

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 4.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "센터 상세 정보",
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.gray900,
                    )

                    if (!isEditState) {
                        CareButtonStrokeSmall(
                            text = "수정하기",
                            onClick = { setEditState(true) }
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "전화번호",
                        style = CareTheme.typography.subtitle4,
                        color = CareTheme.colors.gray500,
                    )

                    if (!isEditState) {
                        Text(
                            text = "(02) 123-4567",
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray900,
                        )
                    } else {
                        CareTextField(
                            value = "(02) 123-4567",
                            onValueChanged = {},
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "센터 소개",
                        style = CareTheme.typography.subtitle4,
                        color = CareTheme.colors.gray500,
                    )

                    if (!isEditState) {
                        Text(
                            text = "안녕하세요 반갑습니다!",
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray900,
                        )
                    } else {
                        CareTextFieldLong(
                            value = "안녕하세요 반갑습니다!",
                            onValueChanged = {},
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "센터 사진",
                        style = CareTheme.typography.subtitle4,
                        color = CareTheme.colors.gray500,
                    )

                    Box(
                        modifier = Modifier.wrapContentSize()
                            .clip(RoundedCornerShape(6.dp))
                            .padding(bottom = 60.dp),
                    ) {
                        Image(
                            painter = painterResource(com.idle.center.profile.R.drawable.ic_temp_center),
                            contentDescription = "",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (isEditState) {
                            Image(
                                painter = painterResource(R.drawable.ic_edit_pencil),
                                contentDescription = "",
                                modifier = Modifier.align(Alignment.BottomEnd)
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}