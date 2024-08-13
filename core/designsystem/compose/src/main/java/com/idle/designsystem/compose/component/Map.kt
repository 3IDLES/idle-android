package com.idle.designsystem.compose.component

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.idle.designresource.R.drawable
import com.idle.designsystem.compose.foundation.CareTheme
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay


@Composable
fun CareMap(
    workspaceLatLng: Pair<Double, Double>? = null,
    homeLatLng: Pair<Double, Double>? = null,
    isLocationButtonEnabled: Boolean = false,
    isScaleBarEnabled: Boolean = false,
    isScrollGesturesEnabled: Boolean = false,
    isZoomControlEnabled: Boolean = false,
    isTiltGesturesEnabled: Boolean = false,
    isRotateGesturesEnabled: Boolean = false,
    pathColor: Color = CareTheme.colors.orange400,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val density = LocalDensity.current

    val mapView = MapView(context).apply {
        getMapAsync { naverMap ->
            if (workspaceLatLng != null && homeLatLng != null) {
                val wLatLng = LatLng(workspaceLatLng.first, workspaceLatLng.second)
                val hLatLng = LatLng(homeLatLng.first, homeLatLng.second)

                with(density) {
                    val cameraUpdate = CameraUpdate.fitBounds(
                        LatLngBounds(wLatLng, hLatLng),
                        60.dp.toPx().toInt(),
                        40.dp.toPx().toInt(),
                        60.dp.toPx().toInt(),
                        40.dp.toPx().toInt(),
                    )

                    naverMap.moveCamera(cameraUpdate)

                    PathOverlay().apply {
                        coords = listOf(wLatLng, hLatLng)
                        width = 3.dp.toPx().toInt()
                        outlineWidth = 0
                        color = pathColor.toArgb()
                        map = naverMap
                    }
                }

                Marker().apply {
                    position = hLatLng
                    icon = OverlayImage.fromResource(drawable.ic_marker_home)
                    map = naverMap
                }

                Marker().apply {
                    position = wLatLng
                    icon = OverlayImage.fromResource(drawable.ic_marker_workplace)
                    map = naverMap
                }
            }

            naverMap.uiSettings.apply {
                this.isLocationButtonEnabled = isLocationButtonEnabled
                this.isScaleBarEnabled = isScaleBarEnabled
                this.isScrollGesturesEnabled = isScrollGesturesEnabled
                this.isZoomControlEnabled = isZoomControlEnabled
                this.isTiltGesturesEnabled = isTiltGesturesEnabled
                this.isRotateGesturesEnabled = isRotateGesturesEnabled
            }
        }
    }

    val lifecycleObserver = remember {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                Lifecycle.Event.ON_ANY -> Unit
            }
        }
    }

    DisposableEffect(true) {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier,
    )
}