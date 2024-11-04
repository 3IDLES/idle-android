package com.idle.chatting_detail.seeallchat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class SeeAllChatFragment : BaseComposeFragment() {
    private val args: SeeAllChatFragmentArgs by navArgs()
    override val fragmentViewModel: SeeAllChatViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        val allChatContents = args.allChatContents

        fragmentViewModel.apply {
            SeeAllChatScreen(
                allChatContents = allChatContents,
                navigateUp = { findNavController().navigateUp() }
            )
        }
    }
}

@Composable
internal fun SeeAllChatScreen(
    allChatContents: String,
    navigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            CareSubtitleTopBar(
                title = "",
                onNavigationClick = navigateUp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp, end = 20.dp, bottom = 12.dp),
            )
        },
        containerColor = CareTheme.colors.white000,
    ) { paddingValue ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 36.dp)
                .verticalScroll(scrollState),
        ) {
            Text(
                text = allChatContents,
                style = CareTheme.typography.body3,
                color = CareTheme.colors.black,
            )
        }
    }
}