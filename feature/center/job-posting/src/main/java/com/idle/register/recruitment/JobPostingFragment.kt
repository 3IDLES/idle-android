package com.idle.register.recruitment

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareProgressBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopAppBar
import com.idle.post.code.PostCodeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class JobPostingFragment : BaseComposeFragment() {

    override val fragmentViewModel: JobPostingViewModel by viewModels()

    private val postCodeDialog: PostCodeFragment? by lazy {
        PostCodeFragment().apply {
            onDismissCallback = {
                findNavController().currentBackStackEntry?.savedStateHandle?.let {
                    val roadNameAddress = it.get<String>("roadNameAddress")
                    val lotNumberAddress = it.get<String>("lotNumberAddress")

                    fragmentViewModel.setRoadNameAddress(roadNameAddress ?: "")
                    fragmentViewModel.setLotNumberAddress(lotNumberAddress ?: "")
                }
            }
        }
    }

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val registerProcess by registerProcess.collectAsStateWithLifecycle()

            JobPostingScreen(
                jobPostingStep = registerProcess,
                showPostCodeDialog = {
                    if (!(postCodeDialog?.isAdded == true || postCodeDialog?.isVisible == true)) {
                        postCodeDialog?.show(parentFragmentManager, "PostCodeFragment")
                    }
                },
            )
        }
    }
}

@Composable
internal fun JobPostingScreen(
    jobPostingStep: JobPostingStep,
    showPostCodeDialog: () -> Unit,
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            CareSubtitleTopAppBar(
                title = "센터 회원가입",
                onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp)
            )
        },
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(paddingValue)
                .padding(start = 20.dp, end = 20.dp, top = 8.dp),
        ) {
            CareProgressBar(
                currentStep = jobPostingStep.step,
                totalSteps = JobPostingStep.entries.size,
                modifier = Modifier.fillMaxWidth(),
            )

            CareStateAnimator(
                targetState = jobPostingStep,
                label = "센터 정보 입력을 관리하는 애니메이션",
            ) { verificationProcess ->
                when (verificationProcess) {
                    else -> Unit
                }
            }
        }
    }
}