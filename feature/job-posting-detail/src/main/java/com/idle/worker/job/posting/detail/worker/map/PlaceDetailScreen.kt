package com.idle.worker.job.posting.detail.worker.map

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.component.CareMap
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun PlaceDetailScreen(
    callback: () -> Unit,
    lotNumberAddress: String,
    workspaceLatLng: Pair<Double, Double>,
    homeLatLng: Pair<Double, Double>,
) {
    BackHandler { callback() }

    Scaffold(
        containerColor = CareTheme.colors.white000,
        topBar = {
            CareSubtitleTopBar(
                title = lotNumberAddress,
                onNavigationClick = callback,
                modifier = Modifier.padding(
                    start = 12.dp,
                    top = 48.dp,
                    end = 20.dp,
                    bottom = 12.dp
                ),
            )
        }
    ) { paddingValue ->
        CareMap(
            workspaceLatLng = workspaceLatLng,
            homeLatLng = homeLatLng,
            isScrollGesturesEnabled = true,
            isZoomControlEnabled = true,
            isRotateGesturesEnabled = true,
            modifier = Modifier
                .padding(paddingValue)
                .fillMaxSize(),
        )
    }
}